package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class BackToMenuButton extends Actor {
    private final Texture buttonImageWVGA;
    private final Texture buttonImageWXGA;
    OrthographicCamera camera;

    public BackToMenuButton(OrthographicCamera camera) {
        this.camera = camera;

        buttonImageWXGA = new Texture(Gdx.files.internal("button_backtomenu_WXGA.png"));
        buttonImageWVGA = new Texture(Gdx.files.internal("button_backtomenu_WVGA.png"));

        calculatePosition(camera.viewportWidth, camera.viewportHeight);

        setColor(new Color(1f, 1f, 1f, 0));
        addAction(Actions.fadeIn(.5f));
    }

    public void calculatePosition(float viewportWidth, float viewportHeight) {
        float xMiddle = viewportWidth/2;
        float yLowerQuarter = viewportHeight / 4;
        float imageWidth = buttonImageWVGA.getWidth();
        float imageHeight = buttonImageWVGA.getHeight();

        if (viewportWidth>=1280) {
            imageWidth = buttonImageWXGA.getWidth();
            imageHeight = buttonImageWXGA.getHeight();
        }
        setPosition(xMiddle - imageWidth * .5f, yLowerQuarter-imageHeight);
        float screenX = camera.unproject(new Vector3(getX(),getY(),0)).x;
        float screenY = camera.unproject(new Vector3(getX(),viewportHeight-getY(),0)).y;
        setBounds(screenX, screenY, imageWidth *camera.zoom, imageHeight*camera.zoom);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setScale(this.getScaleX(), this.getScaleY());
        calculatePosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        if (camera.viewportWidth < 1280)
            batch.draw(buttonImageWVGA, getX(), getY(), buttonImageWVGA.getWidth()*getScaleX()*camera.zoom, buttonImageWVGA.getHeight()*getScaleY()*camera.zoom);
        else
            batch.draw(buttonImageWXGA, getX(), getY(), buttonImageWXGA.getWidth()*getScaleX()*camera.zoom, buttonImageWXGA.getHeight()*getScaleY()*camera.zoom);
    }



}
