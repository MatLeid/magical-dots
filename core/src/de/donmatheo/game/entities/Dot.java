package de.donmatheo.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


/**
 * Created by donmatheo on 18.10.2014.
 */
public class Dot extends Actor {

    public static int DEFAULT_RADIUS = 35;
    protected static Sound getsStableSound = Gdx.audio.newSound(Gdx.files.internal("sound/gets-stable.ogg"));

    protected final PointLight pointLight;
    protected Relation relation1;
    protected Relation relation2;
    private Circle circleBounds;
    private Vector2 center;
    private Texture dotImageBlue;
    private Texture dotImageYellow;
    protected boolean touched;

    public Dot(RayHandler rayHandler) {
        pointLight = new PointLight(rayHandler, 200, Color.ORANGE, 250, getX(), getY());
        pointLight.setActive(false);
        circleBounds = new Circle(DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS);
        center = new Vector2();
        setBounds(0, 0, DEFAULT_RADIUS * 2, DEFAULT_RADIUS * 2);
        setOrigin(0, 0);
        setScale(0.1f);
        setColor(new Color(1f, 1f, 1f, 0f));
        dotImageBlue = new Texture(Gdx.files.internal("dot_blue.png"));
        dotImageYellow = new Texture(Gdx.files.internal("dot_yellow.png"));
        addAction(Actions.fadeIn(1.2f));
        addAction(Actions.scaleTo(1f, 1f, 2f, Interpolation.elasticOut));
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        updatePosition(getX(), getY());
        setScale(this.getScaleX(), this.getScaleY());
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(getStage().getCamera().combined);
        batch.setColor(this.getColor());
        if (isStable()) {
            batch.draw(dotImageYellow, getX(), getY(), 2 * DEFAULT_RADIUS * getScaleX(), 2 * DEFAULT_RADIUS * getScaleY());
        } else {
            batch.draw(dotImageBlue, getX(), getY(), 2 * DEFAULT_RADIUS * getScaleX(), 2 * DEFAULT_RADIUS * getScaleY());
        }
    }

    public boolean hasIsoscelesRelations() {
        double dist1 = distance(relation1.getTarget());
        double dist2 = distance(relation2.getTarget());
        double median = (dist1 + dist2) / 2;
        double absoluteDiff = Math.abs(dist1 - dist2);
        if (absoluteDiff / median < 0.1) {
            if (!isStable()) {
                pointLight.setActive(true);
                getsStableSound.play();
            }
            return true;
        } else {
            if (isStable()) {
                pointLight.setActive(false);
            }
            return false;
        }
    }

    public boolean isStable(){
        return pointLight.isActive();
    }

    protected double distance(Dot neighbour) {
        double dx = getX() - neighbour.getX();         //horizontal difference
        double dy = getY() - neighbour.getY();         //vertical difference
        return Math.sqrt(dx * dx + dy * dy);           //distance using Pythagoras theorem
    }


    public Actor hit(float x, float y, boolean touchable) {
        if (!this.isVisible() || this.getTouchable() == Touchable.disabled)
            return null;

        if (circleBounds.contains(x, y))
            return this;

        return null;
    }

    public boolean isTouched() {
        return touched;
    }

    public void updatePosition(float x, float y) {
        if (getStage() != null) {
            if (x < 0)
                x = 0;
            if (x > getStage().getCamera().viewportWidth)
                x = getStage().getCamera().viewportWidth - (2 * DEFAULT_RADIUS);
            if (y < 0)
                y = 0;
            if (y > getStage().getCamera().viewportHeight)
                y = getStage().getCamera().viewportHeight - (2 * DEFAULT_RADIUS);

        }
        setPosition(x, y);
        center.set(x + getWidth() / 2, y + getHeight() / 2);
        pointLight.setPosition(center);
    }

    public Relation getRelation1() {
        return relation1;
    }

    public void setRelation1(Relation relation1) {
        this.relation1 = relation1;
    }

    public Relation getRelation2() {
        return relation2;
    }

    public void setRelation2(Relation relation2) {
        this.relation2 = relation2;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public Vector2 getCenter() {
        return center;
    }
}
