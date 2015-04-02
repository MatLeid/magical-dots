package de.donmatheo.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.donmatheo.game.MagicalDots;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class EndingText extends Actor {
    private final MagicalDots game;
    private final ShapeRenderer renderer;
    OrthographicCamera camera;

    public EndingText(MagicalDots game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        renderer = new ShapeRenderer();
    }

    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0, 0, 0, 0.4f));
        renderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

}
