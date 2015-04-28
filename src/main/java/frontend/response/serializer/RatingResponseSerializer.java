package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.RatingResponse;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class RatingResponseSerializer implements JsonSerializer<RatingResponse> {
    public JsonElement serialize(RatingResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();

        JsonArray usersArray = new JsonArray();
        for (int i = 0; i < 20; ++i) {
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
