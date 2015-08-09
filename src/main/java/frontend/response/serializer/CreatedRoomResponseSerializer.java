package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.CreatedRoomResponse;
import frontend.response.GetMobileUrlResponse;
import main.Main;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class CreatedRoomResponseSerializer implements JsonSerializer<CreatedRoomResponse> {
    public JsonElement serialize(CreatedRoomResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();

        responseObject.add("room_id", new JsonPrimitive(src.getRoomId()));

        jsonObject.add("response", responseObject);

        return jsonObject;
    }
}
