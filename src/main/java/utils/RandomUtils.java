package utils;

import java.util.Random;

/**
 * nickolay, 21.05.15.
 */
public class RandomUtils {
    public static int randInt(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
