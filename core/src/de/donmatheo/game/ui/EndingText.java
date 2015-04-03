package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class EndingText extends Actor {
    private final OrthographicCamera camera;
    private final Texture endingImageWXGA;
    private final Texture endingImageWVGA;


    public EndingText( OrthographicCamera camera) {
        this.camera = camera;
        endingImageWXGA = new Texture(Gdx.files.internal("background_ending_WXGA.png"));
        endingImageWVGA = new Texture(Gdx.files.internal("background_ending_WVGA.png"));

    }

    public void draw(Batch batch, float parentAlpha) {
        if (camera.viewportWidth<1280)
            batch.draw(endingImageWVGA, getX(), getY(), camera.viewportWidth, camera.viewportHeight);
        else
            batch.draw(endingImageWXGA, getX(), getY(), camera.viewportWidth, camera.viewportHeight);

    }

}
