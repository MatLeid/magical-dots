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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.donmatheo.game.entities.Dot;
import de.donmatheo.game.ui.StartButton;
import de.donmatheo.game.ui.Title;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // setup viewport and stage
        viewport = new FitViewport(800, 480);
        viewport.setCamera(camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Title title = new Title(game, camera);
        StartButton startButton = new StartButton(game, camera);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
            }
        });
        stage.addActor(title);
        stage.addActor(startButton);

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
    }



}

