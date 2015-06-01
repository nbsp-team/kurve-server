package frontend.response.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class UserProfileSerializer implements JsonSerializer<UserProfile> {
    public JsonElement serialize(UserProfile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("user_id", src.getId());
        jsonObject.addProperty("first_name", src.getFirstName());
        jsonObject.addProperty("last_name", src.getLastName());
        jsonObject.addProperty("avatar", src.getAvatarUrl());
        jsonObject.addProperty("global_rating", src.getGlobalRating());

        if (src.isAdmin()) {
            jsonObject.addProperty("isAdmin", true);
        }

        return jsonObject;
    }
}