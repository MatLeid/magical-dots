package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.donmatheo.game.ui.PlayHardcoreButton;
import de.donmatheo.game.ui.PlayNormalButton;
import de.donmatheo.game.ui.TitleText;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class MainMenuScreen implements Screen {

    final MagicalDots game;
    private PlayHardcoreButton playHardcoreButton;
    private PlayNormalButton playNormalButton;
    private Stage stage;
    private Music music;
    OrthographicCamera camera;
    private FitViewport viewport;

    public MainMenuScreen(final MagicalDots game) {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/Anima_-_03_-_Gli_Spiriti.mp3"));

        this.game = game;
        camera = new OrthographicCamera();

        setupViewportAndCamera(game.getScreen_width(), game.getScreen_height());

        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        TitleText titleText = new TitleText(camera);
        playNormalButton = new PlayNormalButton(camera);
        playHardcoreButton = new PlayHardcoreButton(camera);

        stage.addActor(titleText);
        stage.addActor(playNormalButton);
        stage.addActor(playHardcoreButton);

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Actor touched = stage.hit(event.getStageX(), event.getStageY(), true);
                if (touched instanceof PlayNormalButton) {
                    touched.addAction(Actions.sequence(Actions.scaleTo(0.98f, 0.98f, 0.1f),Actions.scaleTo(1f, 1f, 0.5f, Interpolation.elasticOut), run(new Runnable() {
                        public void run() {
                            game.setScreen(new GameScreen(game, false));
                            dispose();
                        }
                    })));
                } else if (touched instanceof PlayHardcoreButton) {
                        touched.addAction(Actions.sequence(Actions.scaleTo(0.98f, 0.98f, 0.1f),Actions.scaleTo(1f, 1f, 0.5f, Interpolation.elasticOut), run(new Runnable() {
                            public void run() {
                                Gdx.audio.newMusic(Gdx.files.internal("sound/play-hardcore.mp3")).play();
                                game.setScreen(new GameScreen(game, true));
                                dispose();
                            }
                        })));
                }
            }
        });

    }

    private void setupViewportAndCamera(double width, float height) {
        if (width<1280) {
            camera.setToOrtho(false, 800, 480);
            viewport = new FitViewport(800, 480);
        } else { // set to WXGA resolution (1280x800)
            camera.setToOrtho(false, 1280, 800);
            viewport = new FitViewport(1280, 800);
        }

        //important command to keep aspec ratio
        viewport.update((int)width, (int)height);

        viewport.setCamera(camera);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(MagicalDots.DARKGREY.r, MagicalDots.DARKGREY.g, MagicalDots.DARKGREY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        setupViewportAndCamera(width,height);
        playNormalButton.calculatePosition(camera.viewportWidth, camera.viewportHeight);
        playHardcoreButton.calculatePosition(camera.viewportWidth, camera.viewportHeight);
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void dispose() {
        music.dispose();
        stage.dispose();
    }

}