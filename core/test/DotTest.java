import com.badlogic.gdx.utils.Array;
import de.donmatheo.game.Dot;
import de.donmatheo.game.DotController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

/**
 * Created by Eceeb on 26.10.2014.
 */
public class DotTest {

    private  Array<Dot> dots;
    DotController dotController;

    @Before
    public void setup(){

        dotController = new DotController();
        dots = dotController.createDots(5);
        dotController.setRandomRelations();
        dotController.setRandomLayout(800, 600);
    }

    @Test public void dots_have_two_external_relations(){

        for(Dot dot : dots){
            assertNotSame(dot, dot.getRelation1());
            assertNotSame(dot, dot.getRelation2());
        }
    }

    @Test public void dots_relations_are_different_dots(){
        for(Dot dot : dots){
            assertNotSame(dot.getRelation1(), dot.getRelation2());
        }
    }

    @Test public void dots_do_not_overlap_after_setup(){

        boolean overlaps = false;
        for(int i = 0; i < dots.size; i++){
            for(int j = i + 1; j < dots.size; j++){
                overlaps |= dots.get(i).overlaps(dots.get(j));
            }
        }
        assertFalse(overlaps);
    }

}
