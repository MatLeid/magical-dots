package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import de.donmatheo.game.GameScreen;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class StartButton extends Actor {
    private final MagicalDots game;
    private final Texture startButtonImage;
    OrthographicCamera camera;


    public StartButton(MagicalDots game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;

        startButtonImage = new Texture(Gdx.files.internal("button_start.png"));
        setPosition(camera.viewportWidth / 2 - startButtonImage.getWidth() / 2, 0);
        setBounds(getX(), getY(), startButtonImage.getWidth(), startButtonImage.getHeight());
        //setScale(0.1f);
        setColor(new Color(1f, 1f, 1f, 0));
        addAction(Actions.fadeIn(1f));
        addAction(Actions.moveTo(camera.viewportWidth / 2 - startButtonImage.getWidth() / 2, camera.viewportHeight / 2 - 140, 1f, Interpolation.elasticOut));
        //addAction(Actions.scaleTo(1f, 1f, 1.5f, Interpolation.elasticOut));
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()) {
            //StartButton touched = (StartButton) hit(Gdx.input.getX()-getX(),(camera.viewportHeight - Gdx.input.getY())-getY(), true);
            //if (touched != null) {
                game.setScreen(new GameScreen(game, false));
                game.getMainMenuScreen().dispose();
            //}
        }
        super.act(delta);
        game.mainfont.setColor(this.getColor());
        setScale(this.getScaleX(), this.getScaleY());
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        batch.draw(startButtonImage, getX(), getY(), startButtonImage.getWidth()*getScaleX(), startButtonImage.getHeight()*getScaleY());
    }

}
