package de.donmatheo.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

/**
 * Created by Eceeb on 02.11.2014.
 */
public class DotController {

    private Array<Dot> dots = new Array<Dot>();
    private Random random = new Random();

    public Array<Dot> createDots(int numberOfDots, RayHandler rayHandler, Stage stage) {
        for (int i = 0; i < numberOfDots; i++) {
            Dot dot = new Dot(rayHandler);
            dots.add(dot);
            stage.addActor(dot);
        }
        return dots;
    }

    public void setRandomRelations() {
        for (int i = 0; i < dots.size; i++) {
            int number1 = i;
            int number2 = i;
            while (number1 == i) {
                number1 = random.nextInt(dots.size);
            }
            while (number2 == i || number2 == number1) {
                number2 = random.nextInt(dots.size);
            }
            dots.get(i).setRelation1(dots.get(number1));
            dots.get(i).setRelation2(dots.get(number2));
        }
    }

    public void setRandomLayout(float width, float height) {
        for (Dot dot : dots) {
            do {
                dot.setX( Dot.DEFAULTRADIUS + random.nextInt((int) (width - 2 * Dot.DEFAULTRADIUS)));
                dot.setY(Dot.DEFAULTRADIUS + random.nextInt((int) (height - 2 * Dot.DEFAULTRADIUS)));
                dot.getPointLight().setPosition(dot.getX(), dot.getY());
            } while(dotIntersectingOtherDots(dot));
        }
    }

    private boolean dotIntersectingOtherDots(Dot standalone) {
        for(int i = dots.indexOf(standalone, true) - 1; i > -1; i--){
            //if(standalone.overlaps(dots.get(i)))
              //  return true;
        }
        return false;
    }
}
