package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.RoomIdResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class RoomIdResponseSerializer implements JsonSerializer<RoomIdResponse> {
    public JsonElement serialize(RoomIdResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();

        if (src.getRoomId() != null) {
            responseObject.add("room_id", new JsonPrimitive(src.getRoomId()));
        } else {
            responseObject.add("room_id", JsonNull.INSTANCE);
        }

        jsonObject.add("response", responseObject);

        return jsonObject;
    }
}
