package de.donmatheo.game.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.donmatheo.game.GameScreen;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 04.02.2015.
 */
public class MyGestureHandler implements GestureDetector.GestureListener {

    private final MagicalDots game;
    private GameScreen gameScreen;
    private float initialScale;

    public MyGestureHandler(GameScreen gameScreen, MagicalDots game) {
        this.gameScreen = gameScreen;
        this.game = game;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (gameScreen.getTouchedDot() == null && !game.isFinished() ) {
            OrthographicCamera camera = gameScreen.getCamera();

            camera.translate(-deltaX, deltaY);
            camera.update();

            // bring camera back into screen when out of boundary
            if (camera.position.x < 0) {
                camera.translate(Math.abs(camera.position.x), 0);
            }
            if (camera.position.y < 0) {
                camera.translate(0, Math.abs(camera.position.y));
            }
            if (camera.position.x > 800) {
                camera.translate(800 - camera.position.x, 0);
            }
            if (camera.position.y > 480) {
                camera.translate(0, 480 - camera.position.y);
            }
        }

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        initialScale = gameScreen.getZoom();

        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {

        //Calculate pinch to zoom
        float ratio = initialDistance / distance;

        //Clamp range and set zoom
        if (!game.isFinished())
            gameScreen.setZoom(MathUtils.clamp(initialScale * ratio, gameScreen.getMinZoom(), gameScreen.getMaxZoom()));

        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

}