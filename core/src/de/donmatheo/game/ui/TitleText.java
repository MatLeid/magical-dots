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
public class TitleText extends Actor {
    private final MagicalDots game;    OrthographicCamera camera;
    private Texture titleImage;


    public TitleText(MagicalDots game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        titleImage = new Texture(Gdx.files.internal("title.png"));

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(titleImage, getX(), getY(), camera.viewportWidth, camera.viewportHeight);
    }

}
