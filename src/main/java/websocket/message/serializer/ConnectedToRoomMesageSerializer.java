package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.ConnectedToRoomMessage;
import websocket.message.DisconnectedPlayerMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class ConnectedToRoomMesageSerializer implements JsonSerializer<ConnectedToRoomMessage> {
    public JsonElement serialize(ConnectedToRoomMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_CONNECTED_TO_ROOM_RESPONSE.ordinal());

        JsonObject roomJson = new JsonObject();
        roomJson.addProperty("id", src.getRoomId());
        responseObject.add("room", roomJson);

        return responseObject;
    }
}