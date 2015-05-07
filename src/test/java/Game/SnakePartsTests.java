package Game;

import model.Snake.SnakePartArc;
import model.Snake.SnakePartLine;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * egor, 07.05.15.
 */

public class SnakePartsTests {


    @Test
    public void testArcCollisions() {
        for(int i=0; i<2;i++) {
            boolean inPi2 = i == 0;
            SnakePartArc arc = new SnakePartArc(0, 0, 100, 0, 10, 0, !inPi2, 0);
            arc.updateHead(Math.PI / 2);
            assertTrue(inPi2 == arc.isInside(110 * Math.cos(Math.PI / 3), 110 * Math.sin(Math.PI / 3), 10));
            assertTrue(inPi2 == arc.isInside(110 * Math.cos(Math.PI / 5), 110 * Math.sin(Math.PI / 5), 10));
            assertTrue(inPi2 == arc.isInside(150 * Math.cos(Math.PI / 5), 150 * Math.sin(Math.PI / 5), 50));
            assertTrue(inPi2 != arc.isInside(110 * Math.cos(Math.PI / 1.5), 110 * Math.sin(Math.PI / 1.5), 10));
            assertTrue(inPi2 != arc.isInside(110 * Math.cos(Math.PI * 1.5), 110 * Math.sin(Math.PI * 1.5), 10));
        }

    }
    @Test
    public void testLineCollisions() {
        double sqrt2 = Math.sqrt(2);
        SnakePartLine line = new SnakePartLine(0, 0, 0.1, 0.1, 10, 0);
        line.updateHead(100, 100, 100*sqrt2);
        assertTrue(line.isInside(11, 11, 10));
        assertTrue(!line.isInside(-10, -10, 10));
        assertTrue(line.isInside(91, 91, 10));
        assertTrue(!line.isInside(110, 110, 1));
        assertTrue(line.isInside(10, 15, 10));
        assertTrue(!line.isInside(-12/sqrt2, 12/sqrt2, 1));
    }
}
