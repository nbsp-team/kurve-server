package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.SnakeUpdateMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class SnakeUpdateMessageSerializer implements JsonSerializer<SnakeUpdateMessage> {
    public JsonElement serialize(SnakeUpdateMessage src, Type typeOfSrc, JsonSerializationContext context) {
        if(src.getJsonElement() == null) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_SNAKE_ARC_RESPONSE.ordinal());

            responseObject.add("snake", context.serialize(src.getSnake()));
            responseObject.addProperty("id", src.getId());
            src.setJsonElement(responseObject);
        }
        return src.getJsonElement();
    }
}
