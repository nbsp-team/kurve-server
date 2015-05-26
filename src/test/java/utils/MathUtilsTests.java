package utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * nickolay, 22.05.15.
 */
public class MathUtilsTests {
    private static final long SEED = 0;

    @Before
    public void before() {
        MathUtils.rand = new Random(SEED);
    }

    @Test
    public void testMaxLimit(){
        for(int i = 0; i < 10; ++i) {
            assertTrue(MathUtils.randInt(0, 100) < 101);
            assertTrue(MathUtils.randInt(0, 5) < 6);
        }
    }

    @Test
    public void testMinLimit(){
        for(int i = 0; i < 10; ++i) {
            assertTrue(MathUtils.randInt(0, 100) >= 0);
            assertTrue(MathUtils.randInt(10, 100) >= 10);
        }
    }

    @Test
    public void shortDouble(){
        double result1 = MathUtils.shortDouble(0.123456789);
        assertEquals(0.0625, result1, 0.001);

        double result2 = MathUtils.shortDouble(-0.123456789);
        assertEquals(-0.125, result2, 0.001);

        double result3 = MathUtils.shortDouble(3455.34534534);
        assertEquals(3455.3125, result3, 0.001);

        double result4 = MathUtils.shortDouble(-3455.34534534);
        assertEquals(-3455.375, result4, 0.001);
    }
}
