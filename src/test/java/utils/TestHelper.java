package utils;

import frontend.AbstractJsonMessage;
import websocket.message.Message;

import static utils.TestUtils.assertEqualsJSON;

/**
 * Created by egor on 30.05.15.
 */
public class TestHelper {
    public static void testMessage(AbstractJsonMessage message, String expectedResult) {
        String actualResult = message.getBody();
        assertEqualsJSON(actualResult, expectedResult);
    }
}
