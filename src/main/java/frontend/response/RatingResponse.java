package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 28.02.15.
 */
public class RatingResponse extends SuccessResponse {
    public RatingResponse() {
    }

    public static class serializer implements JsonSerializer<RatingResponse> {
        public JsonElement serialize(RatingResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", JsonNull.INSTANCE);

            JsonObject responseObject = new JsonObject();

            JsonArray usersArray = new JsonArray();
            for(int i = 0; i < 20; ++i) {
                JsonObject user = new JsonObject();
                user.addProperty("username", "user" + i);
                user.addProperty("global_rating", 100 + i * 5);
                usersArray.add(user);
            }
            responseObject.add("users", usersArray);

            jsonObject.add("response", responseObject);

            return jsonObject;
        }
    }
}
