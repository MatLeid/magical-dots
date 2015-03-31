package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class MainMenuScreen implements Screen {

    final UnstableRelations game;
    private Stage stage;
    private Music music;
    OrthographicCamera camera;

    public MainMenuScreen(final UnstableRelations game) {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/start-screen.mp3"));

        this.game = game;

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(UnstableRelations.DARKGREY.r, UnstableRelations.DARKGREY.g, UnstableRelations.DARKGREY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // setup viewport and stage
        FitViewport viewport = new FitViewport(800, 480);
        viewport.setCamera(camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Title title = new Title(game, camera);
        StartButton startButton = new StartButton(game, camera);

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

