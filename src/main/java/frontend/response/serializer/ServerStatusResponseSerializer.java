package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.ServerStatusResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */

public class ServerStatusResponseSerializer implements JsonSerializer<ServerStatusResponse> {
    public JsonElement serialize(ServerStatusResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("userCount", src.getUserCount());
        responseObject.addProperty("sessionCount", src.getSessionCount());
        responseObject.addProperty("roomCount", src.getRoomCount());

        jsonObject.add("response", responseObject);
        return jsonObject;
    }
}