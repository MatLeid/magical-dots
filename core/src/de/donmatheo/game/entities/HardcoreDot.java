package de.donmatheo.game.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import de.donmatheo.game.StableListener;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Eceeb on 06.04.2015.
 */
public class HardcoreDot extends Dot {

    public static float movementTimer = 500f;
    private SequenceAction forever;

    public HardcoreDot(RayHandler rayHandler) {
        super(rayHandler);
        addDotAction();
    }

    public void addDotAction() {
        if (!isTouched() && !isStable()) {
            forever = sequence(delay(movementTimer), Actions.moveBy(randomFloat(), randomFloat(), 0.8f, Interpolation.bounceOut), run(new Runnable() {
                public void run() {
                    addDotAction();
                }
            }));
            addAction(forever);
        }
    }

    public void removeDotAction() {
        removeAction(forever);
    }

    private float randomFloat() {
        int sign = 1;
        Random rand = new Random();
        if (rand.nextBoolean())
            sign = -1;
        return rand.nextInt(50) * sign;
    }

    public boolean hasIsoscelesRelations() {
        double dist1 = distance(relation1.getTarget());
        double dist2 = distance(relation2.getTarget());
        double median = (dist1 + dist2) / 2;
        double absoluteDiff = Math.abs(dist1 - dist2);
        if (absoluteDiff / median < 0.1) {
            if (!isStable()) {
                pointLight.setActive(true);
                movementTimer /= 5;
                fire(new StableListener.ChangeEvent());
                getsStableSound.play();
            }
            return true;
        } else {
            if (isStable()) {
                pointLight.setActive(false);
                movementTimer *= 5;
                fire(new StableListener.ChangeEvent());
            }
            return false;
        }
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
        if (touched) {
            removeDotAction();
        } else {
            addDotAction();
        }
    }

}
