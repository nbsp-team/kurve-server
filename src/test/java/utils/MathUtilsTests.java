package utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

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
}
