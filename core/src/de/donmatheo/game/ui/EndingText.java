package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class EndingText extends Actor {
    private final OrthographicCamera camera;
    private final Texture endingImageWXGA;
    private final Texture endingImageWVGA;
    private float screenWidth;
    private float screenHeight;
    private float x;
    private float y;


    public EndingText(OrthographicCamera camera) {
        this.camera = camera;
        endingImageWXGA = new Texture(Gdx.files.internal("background_ending_WXGA.png"));
        endingImageWVGA = new Texture(Gdx.files.internal("background_ending_WVGA.png"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        x = camera.unproject(new Vector3(0,0,0)).x;
        y = camera.unproject(new Vector3(0,screenHeight,0)).y;
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        if (camera.viewportWidth < 1280)
            batch.draw(endingImageWVGA, x, y, screenWidth *camera.zoom, screenHeight*camera.zoom);
        else
            batch.draw(endingImageWXGA, x, y, screenWidth *camera.zoom, screenHeight*camera.zoom);
    }

}
