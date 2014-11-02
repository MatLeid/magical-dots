package de.donmatheo.game;

import com.badlogic.gdx.math.Circle;

/**
 * Created by donmatheo on 18.10.2014.
 */
public class Dot extends Circle {

    public static int DEFAULTRADIUS = 35;

    private Dot relation1;
    private Dot relation2;

    public Dot getRelation1() { return relation1; }

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
        double dx = x - neighbour.x;         //horizontal difference
        double dy = y - neighbour.y;         //vertical difference
        return Math.sqrt(dx * dx + dy * dy); //distance using Pythagoras theorem
    }

    public boolean hasIsoscelesRelations() {
        double dist1 = distance(relation1);
        double dist2 = distance(relation2);

        double median = (dist1 + dist2) / 2;
        double absoluteDiff = Math.abs(dist1 - dist2);

        return (absoluteDiff / median < 0.1);
    }

    public Dot() {
        super.radius = DEFAULTRADIUS;
    }


}
