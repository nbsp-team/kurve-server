package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * nickolay, 17.03.15.
 */
public class ReadyMessage extends Message {
    private boolean isReady;
    private int playerId;

    public ReadyMessage(int playerId, boolean isReady) {
        this.isReady = isReady;
        this.playerId = playerId;
    }

    public boolean isReady() {
        return isReady;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static class serializer implements JsonSerializer<ReadyMessage> {
        public JsonElement serialize(ReadyMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_READY_RESPONSE.ordinal());
            responseObject.addProperty("ready", src.isReady());
            responseObject.addProperty("player_id", src.getPlayerId());
            return responseObject;
        }
    }
}
