package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * egor, 18.04.15.
 */
public class KeyMessage extends Message {
    private boolean isLeft, isUp;
    private int playerId;

    public KeyMessage(boolean isLeft, boolean isUp, int playerId) {
        this.isLeft = isLeft; this.isUp = isUp; this.playerId = playerId;
    }

    public boolean getIsLeft() {
        return isLeft;
    }

    public boolean getIsUp() {
        return isUp;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static class serializer implements JsonSerializer<KeyMessage> {
        public JsonElement serialize(KeyMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_KEY_RESPONSE.ordinal());

            responseObject.addProperty("sender", src.getPlayerId());
            responseObject.addProperty("isLeft", src.getIsLeft());
            responseObject.addProperty("isUp", src.getIsUp());

            return responseObject;
        }
    }
}
