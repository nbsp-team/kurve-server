package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.RatingResponse;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class RatingResponseSerializer implements JsonSerializer<RatingResponse> {
    public JsonElement serialize(RatingResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonArray ratingArray = new JsonArray();
        for (UserProfile user : src.getRatings()) {
            ratingArray.add(context.serialize(user));
        }

        JsonObject response = new JsonObject();
        response.add("rating", ratingArray);

        jsonObject.add("response", response);

        return jsonObject;
    }
}
