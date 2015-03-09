package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 25.02.15.
 */
public class SuccessResponse extends Response {
    public SuccessResponse() {
    }

    public static class serializer implements JsonSerializer<SuccessResponse> {
        public JsonElement serialize(SuccessResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", JsonNull.INSTANCE);
            jsonObject.add("response", new JsonObject());
            return jsonObject;
        }
    }
}
