package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static org.junit.Assert.assertTrue;

/**
 * nickolay, 26.05.15.
 */
public class TestUtils {
    public static void assertEqualsJSON(String json1, String json2) {
        JsonParser parser = new JsonParser();
        JsonElement o1 = parser.parse(json1);
        JsonElement o2 = parser.parse(json2);

        assertTrue(o1.equals(o2));
    }
}
