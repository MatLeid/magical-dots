package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class MainMenuScreen implements Screen {

    final UnstableRelations game;

    OrthographicCamera camera;

    public MainMenuScreen(final UnstableRelations game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(UnstableRelations.PURPLE.r,UnstableRelations.PURPLE.g,UnstableRelations.PURPLE.b,1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font.draw(game.batch, "Welcome to the Game!", 300, camera.viewportHeight/2 + 50);
        game.font.draw(game.batch, "Tap anywhere to begin!", 300, camera.viewportHeight/2 - 40);
        game.batch.end();

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

    }
}
