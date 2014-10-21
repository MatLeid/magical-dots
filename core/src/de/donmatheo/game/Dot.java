package de.donmatheo.game;

import com.badlogic.gdx.math.Circle;

/**
 * Created by Fireball on 18.10.2014.
 */
public class Dot extends Circle {

    private Dot relation1;
    private Dot relation2;

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

    public Dot(float x, float y) {
        super.x = x;
        super.y = y;
        super.radius = 35;
    }


}
