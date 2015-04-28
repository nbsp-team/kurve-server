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

        jsonObject.addProperty("username", src.getLogin());
        jsonObject.addProperty("email", src.getEmail());
        jsonObject.addProperty("global_rating", 0);

        if (src.isAdmin()) {
            jsonObject.addProperty("isAdmin", true);
        }

        return jsonObject;
    }
}