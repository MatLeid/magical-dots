package de.donmatheo.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by donmatheo on 29.11.2014.
 */
public class Relation extends Actor {

    private final Texture dotImageBlue;
    private final Texture dotImageYellow;
    private Dot source, target;
    Array<Vector2> points;
    private float scaleValue;
    private float scaleSlope;

    public Relation(Dot source, Dot target) {
        this.source = source;
        this.target = target;

        dotImageBlue = new Texture(Gdx.files.internal("dot_blue.png"));
        dotImageYellow = new Texture(Gdx.files.internal("dot_yellow.png"));

        // Create Array to hold all drawable points
        points = new Array<Vector2>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        calculatePoints();
    }

    public void calculatePoints() {
        points.clear();

        // calculate formular of line equation: y = m*x + b
        Vector2 pointA = new Vector2(source.getCenter());
        Vector2 pointB = new Vector2(target.getCenter());
        if (source.getCenter().x > target.getCenter().x) {
            pointA = new Vector2(target.getCenter());
            pointB = new Vector2(source.getCenter());
        }
        float m = (pointB.y - pointA.y) / (pointB.x - pointA.x);
        float b = pointA.y - (m * pointA.x);

        // add possible points to list
        // add first point (starting dot)
        points.add(new Vector2((int) pointA.x, (int) pointA.y));

        float x = pointA.x;
        while (x < pointB.x) {
            float y = m * x + b;

            Vector2 temp = new Vector2(x,y);
            Vector2 last = points.get(points.size-1);

            if (distance(last, temp) > 20) {
                points.add(new Vector2(x, y));
            }
            x+=0.01;
        }

        //calculate new scale slope
        scaleSlope = (20 - 5 ) / points.size;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(getStage().getCamera().combined);
        batch.setColor(this.getColor());

        for (int i = 0; i<points.size; i++) {
            Vector2 point = points.get(i);
            scaleValue = (scaleSlope * i) + 5;
            System.out.println(scaleValue);
            if (source.hasIsoscelesRelations()) {
                batch.draw(dotImageYellow, point.x, point.y, scaleValue * getScaleX(), scaleValue * getScaleY());
            }
            if (source.isTouched() && !source.hasIsoscelesRelations()){
                batch.draw(dotImageBlue, point.x, point.y, scaleValue * getScaleX(), scaleValue * getScaleY());
            }
        }
    }

    private double distance(Vector2 pointA, Vector2 pointB) {
        double dx = pointA.x - pointB.x;        //horizontal difference
        double dy = pointA.y - pointB.y;        //vertical difference
        return Math.sqrt(dx * dx + dy * dy);    //distance using Pythagoras theorem
    }

    public Dot getTarget() {
        return target;
    }

}
