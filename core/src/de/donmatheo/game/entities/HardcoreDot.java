package de.donmatheo.game.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Eceeb on 06.04.2015.
 */
public class HardcoreDot extends Dot {

    public static float movementTimer = 500f;
    public static DelayAction delay = new DelayAction(movementTimer);

    private SequenceAction forever;

    public HardcoreDot(RayHandler rayHandler) {
        super(rayHandler);
        addDotAction();
    }

    public void addDotAction() {
        if (!isTouched() && !isStable()) {
            forever = sequence(delay, Actions.moveBy(randomFloat(), randomFloat(), 0.8f,
                    Interpolation.bounceOut), run(new Runnable() {
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
        Random rand = new Random();
        int sign = rand.nextBoolean() ? 1 : -1;
        return rand.nextInt(40) + 10 * sign;
    }

    public boolean hasIsoscelesRelations() {
        if (calculateIsoscelesRelations()) {
            if (!isStable()) {
                pointLight.setActive(true);
                delay.setDuration(movementTimer /= 5);
                removeDotAction();
                getsStableSound.play();
            }
            return true;
        } else {
            if (isStable()) {
                pointLight.setActive(false);
                delay.setDuration(movementTimer *= 5);
                addDotAction();
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
