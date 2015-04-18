package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.GetUserResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */

public class GetUserResponseSerializer implements JsonSerializer<GetUserResponse> {
    public JsonElement serialize(GetUserResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();
        responseObject.add("user", context.serialize(src.getUserProfile()));
        jsonObject.add("response", responseObject);
        return jsonObject;
    }
}
