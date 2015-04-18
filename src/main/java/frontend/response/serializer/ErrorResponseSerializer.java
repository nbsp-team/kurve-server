package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.ErrorResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */

public class ErrorResponseSerializer implements JsonSerializer<ErrorResponse> {
    public JsonElement serialize(ErrorResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject errorObject = new JsonObject();
        errorObject.add("code", new JsonPrimitive(src.getErrorCode().ordinal()));
        errorObject.add("description", new JsonPrimitive(src.getErrorDescription()));

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", errorObject);
        return jsonObject;
    }
}
