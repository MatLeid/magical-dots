package de.donmatheo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class StartButton extends Actor {
    private final UnstableRelations game;    OrthographicCamera camera;


    public StartButton(UnstableRelations game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }

        addAction(Actions.scaleTo(1f,1f,0.5f, Interpolation.elasticOut));
        this.setScale(0.1f);
    }

    public void draw(Batch batch, float parentAlpha) {
        game.mainfont.setScale(this.getScaleX());
        game.mainfont.draw(batch, "Tap anywhere to begin!", getX()+ 250, getY()+camera.viewportHeight/2 - 50);
    }

}
