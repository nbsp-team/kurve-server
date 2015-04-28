package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.NewBonusMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 23.04.15.
 */
public class NewBonusMessageSerializer implements JsonSerializer<NewBonusMessage> {
    public JsonElement serialize(NewBonusMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_NEW_BONUS_RESPONSE.ordinal());

        responseObject.add("bonus", context.serialize(src.getBonus()));


        return responseObject;
    }
}