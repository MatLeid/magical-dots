package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.donmatheo.game.ui.PlayHardcoreButton;
import de.donmatheo.game.ui.PlayNormalButton;
import de.donmatheo.game.ui.TitleText;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class MainMenuScreen implements Screen {

    final MagicalDots game;
    private Stage stage;
    private Music music;
    OrthographicCamera camera;
    private FitViewport viewport;

    public MainMenuScreen(final MagicalDots game) {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/start-screen.mp3"));

        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.getScreen_width(), game.getScreen_height());

        // setup viewport and stage
        viewport = new FitViewport(game.getScreen_width(), game.getScreen_height());
        viewport.setCamera(camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        TitleText titleText = new TitleText(game, camera);
        PlayNormalButton playNormalButton = new PlayNormalButton(game, camera);
        PlayHardcoreButton playHardcoreButton = new PlayHardcoreButton(game, camera);
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
                    game.setScreen(new GameScreen(game, false));
                    dispose();
                } else if (touched instanceof PlayHardcoreButton) {
                    game.setScreen(new GameScreen(game, true));
                    dispose();
                }
            }
        });

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
        viewport.update(width, height);
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        music.dispose();
        stage.dispose();
    }

}