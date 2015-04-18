package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.ConnectedPlayerMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class ConnectedPlayerMessageSerializer implements JsonSerializer<ConnectedPlayerMessage> {
    public JsonElement serialize(ConnectedPlayerMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_PLAYER_CONNECTED_RESPONSE.ordinal());
        JsonObject playerObject = (JsonObject) context.serialize(src.getPlayer());
        responseObject.add("player", playerObject);

        return responseObject;
    }
}