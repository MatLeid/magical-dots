package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import de.donmatheo.game.GameScreen;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class StartButton extends Actor {
    private final MagicalDots game;    OrthographicCamera camera;


    public StartButton(MagicalDots game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;

        //this.setBounds(0, 0, 200, 100);
        this.setScale(0.1f);
        this.setColor(new Color(1f, 1f, 1f, 0));
        addAction(Actions.fadeIn(2f));
        addAction(Actions.scaleTo(1f, 1f, 2.5f, Interpolation.elasticOut));
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            game.getMainMenuScreen().dispose();
        }
        super.act(delta);
        game.mainfont.setColor(this.getColor());
        game.mainfont.setScale(this.getScaleX(), this.getScaleY());
    }

    public void draw(Batch batch, float parentAlpha) {
        game.mainfont.draw(batch, "Tap anywhere to begin!", getX()+ 250, getY()+camera.viewportHeight/2 - 50);
    }

}
