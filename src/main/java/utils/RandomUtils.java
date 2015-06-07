package utils;

import java.util.Random;

/**
 * nickolay, 07.06.15.
 */
public class RandomUtils {
    static final String alphabet = "023456789abcdefghiklmnopqrstuwxyz";
    static Random rand = new Random();

    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        return sb.toString();
    }
}
