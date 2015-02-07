package de.donmatheo.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collections;

public class GameScreen implements Screen {

    private final UnstableRelations game;

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

    private ArrayList<Music> songs;

    public GameScreen(final UnstableRelations game) {
        this.game = game;
        // setup camera
        final float screen_width = Gdx.graphics.getWidth();
        final float screen_height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen_width, screen_height);

        // setup viewport and stage
        FitViewport viewport = new FitViewport(800, 480);
        viewport.setCamera(camera);
        stage = new Stage(viewport);

        initaliseInputProcessors();

        stage.addListener(new InputListener() {

            private Dot touched;
            Vector2 touchPosition = new Vector2();

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched = (Dot) stage.hit(event.getStageX(), event.getStageY(), true);
                if (touched != null) {
                    touched.setTouched(true);
                    offsetX = x - touched.getX();
                    offsetY = y - touched.getY();
                }
                touchPosition.set(x, y);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (touched != null)
                    touched.setTouched(false);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                // move dot when touched
                if (touched != null) {
                    touched.updatePosition(event.getStageX() - offsetX, event.getStageY() - offsetY);
                }
                // otherwise move camera
                else {
                    camera.translate(touchPosition.x - x , touchPosition.y - y );

                    // bring camera back into screen when out of boundary
                    if (camera.position.x < 0) {
                        camera.translate(Math.abs(camera.position.x), 0);
                    }
                    if (camera.position.y < 0) {
                        camera.translate(0, Math.abs(camera.position.y));
                    }
                    if (camera.position.x > screen_width) {
                        camera.translate(screen_width -camera.position.x , 0);
                    }
                    if (camera.position.y > screen_height) {
                        camera.translate(0, screen_height- camera.position.y);
                    }
                }
            }

        });

        // setup lighting
        world = new World(new Vector2(0, -9.8f), false);
        lighting = new RayHandler(world);
        lighting.setCombinedMatrix(camera.combined);
        lighting.setAmbientLight(UnstableRelations.DARKGREY);

        // setup dots
        dots = dotController.createDots(5, lighting);
        dotController.setRandomRelations();
        dotController.setRandomLayout(camera.viewportWidth, camera.viewportHeight);
        dotController.addAllToStage(stage);

    }

    public void initaliseInputProcessors() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        gestureHandler = new MyGestureHandler(this);
        inputProcessor = new MyInputProcessor(this);
        inputMultiplexer.addProcessor(inputProcessor);
        inputMultiplexer.addProcessor(new GestureDetector(gestureHandler));
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // set zoom and update camera
        camera.zoom = zoom;
        camera.update();

        // render background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render lights
        lighting.setCombinedMatrix(camera.combined);
        lighting.updateAndRender();

        // render dots
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        Collections.shuffle(songs);
        for(Music m : songs){
            m.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    int index = songs.indexOf(music);
                    if(index + 1 == songs.size() )
                        songs.get(0).play();
                    else
                        songs.get(index + 1).play();
                }
            });
        }
        songs.get(0).play();
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
        stage.dispose();
        for(Music m : songs)
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
}
