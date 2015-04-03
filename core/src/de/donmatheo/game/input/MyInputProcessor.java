package de.donmatheo.game.input;

import com.badlogic.gdx.InputProcessor;
import de.donmatheo.game.GameScreen;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 04.02.2015.
 */

public class MyInputProcessor implements InputProcessor {

    private final MagicalDots game;
    private GameScreen gameScreen;

    public MyInputProcessor(GameScreen gameScreen, MagicalDots game) {
        this.gameScreen = gameScreen;
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        if (!game.isFinished()) {

            //Zoom out
            if (amount > 0 && gameScreen.getZoom() < gameScreen.getMaxZoom()) {
                gameScreen.setZoom(gameScreen.getZoom() + 0.1f);
            }

            //Zoom in
            if (amount < 0 && gameScreen.getZoom() > gameScreen.getMinZoom()) {
                gameScreen.setZoom(gameScreen.getZoom() - 0.1f);
            }
        }

        return true;
    }

}