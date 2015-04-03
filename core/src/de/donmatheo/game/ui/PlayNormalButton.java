package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class PlayNormalButton extends Actor {
    private final Texture startButtonImageWVGA;
    private final Texture startButtonImageWXGA;
    OrthographicCamera camera;


    public PlayNormalButton(OrthographicCamera camera) {
        this.camera = camera;

        startButtonImageWXGA = new Texture(Gdx.files.internal("button_normal_WXGA.png"));
        startButtonImageWVGA = new Texture(Gdx.files.internal("button_normal_WVGA.png"));

        calculatePosition(camera.viewportWidth, camera.viewportHeight);

        setColor(new Color(1f, 1f, 1f, 0));
        addAction(Actions.fadeIn(.5f));
    }

    public void calculatePosition(float viewportWidth, float viewportHeight) {
        float xMiddle = viewportWidth/2;
        float yLowerThird = viewportHeight / 3;
        float imageWidth = startButtonImageWVGA.getWidth();
        float imageHeight = startButtonImageWVGA.getHeight();

        if (viewportWidth>=1280) {
            imageWidth = startButtonImageWXGA.getWidth();
            imageHeight = startButtonImageWXGA.getHeight();
        }
        setPosition(xMiddle - imageWidth * 1.25f, yLowerThird-imageHeight);
        setBounds(getX(), getY(), imageWidth, imageHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setScale(this.getScaleX(), this.getScaleY());
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        if (camera.viewportWidth < 1280)
            batch.draw(startButtonImageWVGA, getX(), getY(), startButtonImageWVGA.getWidth()*getScaleX(), startButtonImageWVGA.getHeight()*getScaleY());
        else
            batch.draw(startButtonImageWXGA, getX(), getY(), startButtonImageWXGA.getWidth()*getScaleX(), startButtonImageWXGA.getHeight()*getScaleY());
    }


}
