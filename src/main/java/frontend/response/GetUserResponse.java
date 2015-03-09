package frontend.response;

import com.google.gson.*;
import frontend.response.SuccessResponse;
import model.UserProfile;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * nickolay, 25.02.15.
 */
public class GetUserResponse extends SuccessResponse {
    private UserProfile userProfile;

    public GetUserResponse(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public static class serializer implements JsonSerializer<GetUserResponse> {
        public JsonElement serialize(GetUserResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", JsonNull.INSTANCE);

            JsonObject responseObject = new JsonObject();
            responseObject.add("user", context.serialize(src.getUserProfile()));
            jsonObject.add("response", responseObject);
            return jsonObject;
        }
    }
}
