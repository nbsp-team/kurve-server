package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.SuccessResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class SuccessResponseSerializer implements JsonSerializer<SuccessResponse> {
    public JsonElement serialize(SuccessResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);
        jsonObject.add("response", new JsonObject());
        return jsonObject;
    }
}