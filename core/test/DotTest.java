import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import de.donmatheo.game.Dot;
import de.donmatheo.game.DotController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;

/**
 * Created by Eceeb on 26.10.2014.
 */
public class DotTest {

    private  Array<Dot> dots;
    DotController dotController;

    @Before
    public void setup(){
        World world = new World(new Vector2(0, -9.8f), false);
        RayHandler lighting = new RayHandler(world);
        dotController = new DotController();
        dots = dotController.createDots(5, lighting);
        dotController.setRandomRelations();
        dotController.setRandomLayout(800, 600);
    }

    @Test public void dots_have_two_external_relations(){

        for(Dot dot : dots){
            assertNotSame(dot, dot.getRelation1().getTarget());
            assertNotSame(dot, dot.getRelation2().getTarget());
        }
    }

    @Test public void dots_relations_are_different_dots(){
        for(Dot dot : dots){
            assertNotSame(dot.getRelation1().getTarget(), dot.getRelation2().getTarget());
        }
    }

//    @Test public void dots_do_not_overlap_after_setup(){
//
//        boolean overlaps = false;
//        for(int i = 0; i < dots.size; i++){
//            for(int j = i + 1; j < dots.size; j++){
//                overlaps |= dots.get(i).overlaps(dots.get(j));
//            }
//        }
//        assertFalse(overlaps);
//    }
}
