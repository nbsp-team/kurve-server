package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.SignInResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class SignInResponseSerializer implements JsonSerializer<SignInResponse> {
    public JsonElement serialize(SignInResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();
        responseObject.add("user", context.serialize(src.getUserProfile()));
        jsonObject.add("response", responseObject);

        return jsonObject;
    }
}