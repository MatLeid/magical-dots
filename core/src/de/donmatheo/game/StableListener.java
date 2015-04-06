package de.donmatheo.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Created by Eceeb on 04.04.2015.
 */
abstract public class StableListener implements EventListener {
    public boolean handle (Event event) {
        if (!(event instanceof ChangeEvent)) return false;
        changed((ChangeEvent)event, event.getTarget());
        return false;
    }

    /** @param actor The event target, which is the actor that emitted the change event. */
    abstract public void changed (ChangeEvent event, Actor actor);

    /** Fired when something in an actor has changed. This is a generic event, exactly what changed in an actor will vary.
     * @author Nathan Sweet */
    static public class ChangeEvent extends Event {
    }
}
