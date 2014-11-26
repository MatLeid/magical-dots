package de.donmatheo.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by donmatheo on 18.10.2014.
 */
public class Dot extends Actor {

    public static int DEFAULTRADIUS = 35;
    private final PointLight pointLight;
    private Dot relation1;
    private Dot relation2;

    private Color yellow = UnstableRelations.parseColor("FFF675", 1);
    private Color blue = UnstableRelations.parseColor("14D6D6", 1);
    private Color lightblue = UnstableRelations.parseColor("BAFFFF", 1);

    private ShapeRenderer renderer;
    private boolean touched;
    private float offsetX, offsetY;

    public Dot(RayHandler rayHandler) {
        pointLight = new PointLight(rayHandler, 200, Color.ORANGE, 250, getX(), getY());
        pointLight.setActive(false);
        renderer = new ShapeRenderer();

        setBounds(0, 0, DEFAULTRADIUS * 2, DEFAULTRADIUS * 2);
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                offsetX = x;
                offsetY = y;
                touched = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched = false;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updatePosition(event.getStageX() - offsetX, event.getStageY() - offsetY);
            }
        });

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(yellow);
        if (hasIsoscelesRelations()) {
            renderer.rectLine(getX(), getY(), getRelation1().getX(), getRelation1().getY(), 5);
            renderer.rectLine(getX(), getY(), getRelation2().getX(), getRelation2().getY(), 5);
            renderer.circle(getX(), getY(), DEFAULTRADIUS + 4);
        }

        renderer.setColor(blue);
        if (isTouched() && !hasIsoscelesRelations()) {
            renderer.rectLine(getX(), getY(), getRelation1().getX(), getRelation1().getY(), 3);
            renderer.rectLine(getX(), getY(), getRelation2().getX(), getRelation2().getY(), 3);
        }
        if (hasIsoscelesRelations()) {
            renderer.setColor(lightblue);
        }
        renderer.circle(getX(), getY(), DEFAULTRADIUS);
        renderer.end();
        batch.begin();
    }

    private boolean isTouched() {
        return touched;
    }

    public void updatePosition(float x, float y){
        setPosition(x, y);
        pointLight.setPosition(x, y);
    }
    public Dot getRelation1() {
        return relation1;
    }

    public void setRelation1(Dot relation1) {
        this.relation1 = relation1;
    }

    public Dot getRelation2() {
        return relation2;
    }

    public void setRelation2(Dot relation2) {
        this.relation2 = relation2;
    }

    private double distance(Dot neighbour) {
        double dx = getX() - neighbour.getX();         //horizontal difference
        double dy = getY() - neighbour.getY();         //vertical difference
        return Math.sqrt(dx * dx + dy * dy); //distance using Pythagoras theorem
    }

    public boolean hasIsoscelesRelations() {
        double dist1 = distance(relation1);
        double dist2 = distance(relation2);

        double median = (dist1 + dist2) / 2;
        double absoluteDiff = Math.abs(dist1 - dist2);
        if (absoluteDiff / median < 0.1) {
            pointLight.setActive(true);
            return true;
        } else {
            pointLight.setActive(false);
            return false;
        }

    }

}
