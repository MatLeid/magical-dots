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


    public Dot(RayHandler rayHandler) {
        pointLight = new PointLight(rayHandler, 200, Color.ORANGE, 250, getX(), getY());
        pointLight.setActive(false);
        renderer = new ShapeRenderer();

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

//        if (selectedDot == i && !dot.hasIsoscelesRelations()) {
//            renderer.rectLine(dot.x, dot.y, dot.getRelation1().x, dot.getRelation1().y, 3);
//            renderer.rectLine(dot.x, dot.y, dot.getRelation2().x, dot.getRelation2().y, 3);
//        }
        if (hasIsoscelesRelations()) {
            renderer.setColor(lightblue);
        }
        renderer.circle(getX(), getY(), DEFAULTRADIUS);
        renderer.end();
        batch.begin();
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

    public PointLight getPointLight() {
        return pointLight;
    }

}
