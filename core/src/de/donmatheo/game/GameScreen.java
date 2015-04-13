package de.donmatheo.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.donmatheo.game.entities.Dot;
import de.donmatheo.game.input.MyGestureHandler;
import de.donmatheo.game.input.MyInputProcessor;
import de.donmatheo.game.ui.BackToMenuButton;
import de.donmatheo.game.ui.EndingText;

import java.util.ArrayList;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class GameScreen implements Screen {

    private final MagicalDots game;
    private final EndingText endingText;
    private final FitViewport viewport;
    private final BackToMenuButton backToMenuButton;

    private OrthographicCamera camera;

    private DotController dotController = new DotController();
    private Array<Dot> dots = new Array<Dot>();

    private float offsetX;
    private float offsetY;
    private float zoom = 1.0f;
    private float maxZoom = 2.0f;
    private float minZoom = 0.5f;

    private World world;

    private RayHandler lighting;
    private Stage stage;

    private InputMultiplexer inputMultiplexer;
    private MyGestureHandler gestureHandler;
    private MyInputProcessor inputProcessor;

    private Dot touchedDot;

    private ArrayList<Music> songs;

    public GameScreen(final MagicalDots game, boolean hardcoreMode) {

        this.game = game;

        // setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.getScreen_width(), game.getScreen_height());

        // setup viewport and stage
        viewport = new FitViewport(game.getScreen_width(), game.getScreen_height());
        viewport.setCamera(camera);
        stage = new Stage(viewport);

        initaliseInputProcessors();

        stage.addListener(new InputListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!game.isFinished()) {
                    Actor touched = stage.hit(event.getStageX(), event.getStageY(), true);
                    if (touched instanceof Dot) {
                        touchedDot = (Dot) stage.hit(event.getStageX(), event.getStageY(), true);
                        if (touchedDot != null) {
                            touchedDot.setTouched(true);
                            offsetX = x - touchedDot.getX();
                            offsetY = y - touchedDot.getY();
                        }
                    }
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!game.isFinished()) {
                    if (touchedDot != null)
                        touchedDot.setTouched(false);
                }
                if (game.isFinished()) {
                    Actor touched = stage.hit(event.getStageX(), event.getStageY(), true);
                    if (touched instanceof BackToMenuButton) {
                        touched.addAction(Actions.sequence(Actions.scaleTo(0.98f, 0.98f, 0.1f), Actions.scaleTo(1f, 1f, 0.5f, Interpolation.elasticOut), run(new Runnable() {
                            public void run() {
                                game.setFinished(false);
                                game.setScreen(new MainMenuScreen(game));
                                dispose();
                            }
                        })));
                    }
                }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!game.isFinished()) {
                    // move dot when touched
                    if (touchedDot != null) {
                        touchedDot.updatePosition(event.getStageX() - offsetX, event.getStageY() - offsetY);
                    }
                }
            }
        });

        // setup lighting
        world = new World(new Vector2(0, -9.8f), false);
        lighting = new RayHandler(world);
        lighting.setCombinedMatrix(camera.combined);
        lighting.setAmbientLight(MagicalDots.DARKGREY);

        // setup dots
        dots = dotController.createDots(5, lighting, hardcoreMode);
        dotController.setRandomRelations();
        dotController.setRandomLayout(camera.viewportWidth, camera.viewportHeight);
        dotController.addAllToStage(stage);

        // setup UI Elements
        endingText = new EndingText(camera);
        backToMenuButton = new BackToMenuButton(camera);
        stage.addActor(endingText);
        stage.addActor(backToMenuButton);
        endingText.setVisible(false);
        backToMenuButton.setVisible(false);
    }

    public void initaliseInputProcessors() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        gestureHandler = new MyGestureHandler(this, game);
        inputProcessor = new MyInputProcessor(this, game);
        inputMultiplexer.addProcessor(inputProcessor);
        inputMultiplexer.addProcessor(new GestureDetector(gestureHandler));
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (!game.isFinished()) {
            checkWinCondition();
        }
        // set zoom and update camera
        camera.zoom = zoom;
        camera.update();

        // render background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render lights
        lighting.setCombinedMatrix(camera.combined);
        lighting.updateAndRender();

        // render actors (dots and ui elements)
        stage.act(delta);
        stage.draw();
    }

    private void checkWinCondition() {
        boolean allIsolescent = true;
        for (Dot dot : dots) {
            if (!dot.hasIsoscelesRelations()) {
                allIsolescent = false;
            }
        }
        if (allIsolescent) {
            game.setFinished(true);
            dotController.resetHardcoreAction();

            stage.addAction(Actions.sequence(Actions.delay(3f, Actions.run(new Runnable() {
                @Override
                public void run() {
                    endingText.setVisible(true);
                    endingText.setColor(new Color(1,1,1,0));
                    endingText.addAction(Actions.fadeIn(.5f));
                    backToMenuButton.setVisible(true);
                    backToMenuButton.setColor(new Color(1, 1, 1, 0));
                    backToMenuButton.addAction(Actions.sequence(Actions.delay(2f),Actions.fadeIn(.5f)));
                }
            }))));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        initialiseMusic();
    }

    private void initialiseMusic() {
        songs = new ArrayList<Music>();
        songs.add(Gdx.audio.newMusic(Gdx.files.internal("music/game-01.mp3")));
        songs.add(Gdx.audio.newMusic(Gdx.files.internal("music/game-02.mp3")));
        songs.add(Gdx.audio.newMusic(Gdx.files.internal("music/game-03.mp3")));
        shuffleSongs();
        for (Music m : songs) {
            m.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    int index = songs.indexOf(music);
                    if (index + 1 == songs.size())
                        songs.get(0).play();
                    else
                        songs.get(index + 1).play();
                }
            });
        }
        songs.get(0).play();
    }

    private void shuffleSongs() {
        int n = songs.size();
        Random random = new Random();
        for (int i = 0; i < n; i++)
            swap(i, random.nextInt(n));
    }

    private void swap(int i, int change) {
        Music swapper = songs.get(i);
        songs.set(i, songs.get(change));
        songs.set(change, swapper);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        lighting.dispose();
        for (Music m : songs)
            m.dispose();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getMinZoom() {
        return minZoom;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Dot getTouchedDot() {
        return touchedDot;
    }
}
