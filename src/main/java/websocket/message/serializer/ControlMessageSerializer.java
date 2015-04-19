package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.ControlMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class ControlMessageSerializer implements JsonSerializer<ControlMessage> {
    public JsonElement serialize(ControlMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_KEY_RESPONSE.ordinal());

        responseObject.addProperty("sender", src.getPlayerId());
        responseObject.addProperty("isLeft", src.getIsLeft());
        responseObject.addProperty("isUp", src.getIsUp());

        return responseObject;
    }
}