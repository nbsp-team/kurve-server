package utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * nickolay, 22.05.15.
 */
public class RandomUtilsTests {
    private static final long SEED = 0;
    private Random random;

    @Before
    public void before() {
        random = new Random(SEED);
    }

    @Test
    public void testMaxLimit(){
        for(int i = 0; i < 10; ++i) {
            assertTrue(RandomUtils.randInt(random, 0, 100) < 101);
            assertTrue(RandomUtils.randInt(random, 0, 5) < 6);
        }
    }

    @Test
    public void testMinLimit(){
        for(int i = 0; i < 10; ++i) {
            assertTrue(RandomUtils.randInt(random, 0, 100) >= 0);
            assertTrue(RandomUtils.randInt(random, 10, 100) >= 10);
        }
    }
}
