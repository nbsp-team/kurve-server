package websocket.message.serializer;

import com.google.gson.*;
import websocket.GameWebSocketHandler;
import websocket.message.SnakePatchMessage;
import websocket.message.SnakeUpdateMessage;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class SnakePatchMessageSerializer implements JsonSerializer<SnakePatchMessage> {

    public JsonElement serialize(SnakePatchMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_SNAKE_PATCH_RESPONSE.ordinal());
        List<SnakeUpdateMessage> updates = src.getUpdates();
        JsonArray array = new JsonArray();
        for (SnakeUpdateMessage msg : updates) {
            array.add(context.serialize(msg));
        }
        responseObject.add("updates", array);


        return responseObject;
    }
}