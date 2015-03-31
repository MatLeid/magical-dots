package de.donmatheo.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by donmatheo on 31.03.2015.
 */
public class Title extends Actor {
    private final UnstableRelations game;    OrthographicCamera camera;


    public Title(UnstableRelations game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }

    public void draw(Batch batch, float parentAlpha) {
        game.mainfont.draw(batch, "Welcome to", this.getOriginX()+330, camera.viewportHeight / 2 + 130);
        game.titlefont.draw(batch, "Magical", this.getX()+ 160, camera.viewportHeight / 2 + 60);
        game.titlefont.draw(batch, "Dots", 450, camera.viewportHeight / 2 + 60);
    }

}
