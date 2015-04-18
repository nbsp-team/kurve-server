package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.ReadyMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class ReadyMessageSerializer implements JsonSerializer<ReadyMessage> {
    public JsonElement serialize(ReadyMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_READY_RESPONSE.ordinal());
        responseObject.addProperty("ready", src.isReady());
        responseObject.addProperty("player_id", src.getPlayer().getId());
        return responseObject;
    }
}
