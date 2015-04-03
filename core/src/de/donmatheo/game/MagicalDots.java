package de.donmatheo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sun.applet.Main;

/**
 * Created by donmatheo on 19.11.2014.
 */
public class MagicalDots extends Game {

    public static final Color DARKGREY = parseColor("202020", 1);
    public static final Color YELLOW = parseColor("FFF675", 1);
    public static final Color BLUE = parseColor("14D6D6", 1);

    private boolean finished;

    private  float screen_width;
    private  float screen_height;

    private MainMenuScreen menuScreen;

    public void create() {
        screen_width = Gdx.graphics.getWidth();
        screen_height = Gdx.graphics.getHeight();

        menuScreen = new MainMenuScreen(this);
        setScreen(menuScreen);
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
    }

    public static Color parseColor(String hex, float alpha) {
        String s1 = hex.substring(0, 2);
        int v1 = Integer.parseInt(s1, 16);
        float f1 = (float) v1 / 255f;
        String s2 = hex.substring(2, 4);
        int v2 = Integer.parseInt(s2, 16);
        float f2 = (float) v2 / 255f;
        String s3 = hex.substring(4, 6);
        int v3 = Integer.parseInt(s3, 16);
        float f3 = (float) v3 / 255f;
        return new Color(f1, f2, f3, alpha);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public float getScreen_height() {
        return screen_height;
    }

    public float getScreen_width() {
        return screen_width;
    }

}
