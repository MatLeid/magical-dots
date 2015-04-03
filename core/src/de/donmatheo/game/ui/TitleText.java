package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class TitleText extends Actor {
    private final  OrthographicCamera camera;
    private final Texture titleImageWXGA;
    private final Texture titleImageWVGA;


    public TitleText( OrthographicCamera camera) {
        this.camera = camera;
        titleImageWVGA = new Texture(Gdx.files.internal("background_WVGA.png"));
        titleImageWXGA = new Texture(Gdx.files.internal("background_WXGA.png"));

    }

    public void draw(Batch batch, float parentAlpha) {
        if (camera.viewportWidth >= 1280)
            batch.draw(titleImageWXGA, getX(), getY(), camera.viewportWidth, camera.viewportHeight);
        else
            batch.draw(titleImageWVGA, getX(), getY(), camera.viewportWidth, camera.viewportHeight);
    }

}
