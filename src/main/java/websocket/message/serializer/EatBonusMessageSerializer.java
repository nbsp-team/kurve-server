package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import websocket.GameWebSocketHandler;
import websocket.message.EatBonusMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 23.04.15.
 */
public class EatBonusMessageSerializer implements JsonSerializer<EatBonusMessage> {
    public JsonElement serialize(EatBonusMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_BONUS_EAT_RESPONSE.ordinal());

        responseObject.add("bonus_id", context.serialize(src.getBonusId()));
        responseObject.addProperty("eater_id", src.getEater().getId());

        return responseObject;
    }
}