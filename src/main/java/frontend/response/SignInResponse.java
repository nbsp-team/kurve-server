package frontend.response;

import com.google.gson.*;
import frontend.response.SuccessResponse;
import model.UserProfile;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * nickolay, 25.02.15.
 */
public class SignInResponse extends SuccessResponse {
    private UserProfile userProfile;

    public SignInResponse(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public static class serializer implements JsonSerializer<SignInResponse> {
        public JsonElement serialize(SignInResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", JsonNull.INSTANCE);

            JsonObject responseObject = new JsonObject();
            responseObject.add("user", context.serialize(src.getUserProfile()));
            jsonObject.add("response", responseObject);

            return jsonObject;
        }
    }
}
