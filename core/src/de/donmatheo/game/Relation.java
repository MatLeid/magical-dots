package de.donmatheo.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by donmatheo on 29.11.2014.
 */
public class Relation extends Actor {

    private final ShapeRenderer renderer;
    private Dot source, target;

    public Relation(Dot source, Dot target) {
        this.source = source;
        this.target = target;
        renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if (source.hasIsoscelesRelations()) {
            renderer.setColor(UnstableRelations.YELLOW);
            renderer.rectLine(source.getCenter(), target.getCenter(), 5);
        }

        if (source.isTouched() && !source.hasIsoscelesRelations()) {
            renderer.setColor(UnstableRelations.BLUE);
            renderer.rectLine(source.getCenter(), target.getCenter(), 3);
        }
        renderer.end();
        batch.begin();
    }

    public Dot getTarget() {
        return target;
    }

}
