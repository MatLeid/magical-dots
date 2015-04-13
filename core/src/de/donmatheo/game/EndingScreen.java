package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.donmatheo.game.ui.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class EndingScreen implements Screen {

    final MagicalDots game;
    private BackToMenuButton backToMenuButton;
    private Stage stage;
    private Music music;
    OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private TextureRegion background;

    public EndingScreen(final MagicalDots game, TextureRegion background) {
        this.background = background;
        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/start-screen.mp3"));

        this.game = game;
        camera = new OrthographicCamera();

        setupViewportAndCamera(game.getScreen_width(), game.getScreen_height());

        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        EndingText endingText = new EndingText(camera);
        backToMenuButton = new BackToMenuButton(camera);

        stage.addActor(endingText);
        stage.addActor(backToMenuButton);

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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

        batch.begin();
        batch.draw(background,0,0);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        setupViewportAndCamera(width,height);
        backToMenuButton.calculatePosition(camera.viewportWidth, camera.viewportHeight);
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