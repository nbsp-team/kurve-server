package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.DisconnectedPlayerMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class DisconnectedPlayerMesageSerializer implements JsonSerializer<DisconnectedPlayerMessage> {
    public JsonElement serialize(DisconnectedPlayerMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_PLAYER_DISCONNECTED_RESPONSE.ordinal());

        JsonObject playerObject = (JsonObject) context.serialize(src.getPlayer());

        responseObject.add("player", playerObject);

        return responseObject;
    }
}