package frontend.response;

import com.google.gson.*;
import frontend.response.SuccessResponse;
import model.UserProfile;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    private int userCount;
    private int sessionCount;

    public ServerStatusResponse(int userCount, int sessionCount) {
        this.userCount = userCount;
        this.sessionCount = sessionCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public static class serializer implements JsonSerializer<ServerStatusResponse> {
        public JsonElement serialize(ServerStatusResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", JsonNull.INSTANCE);

            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("userCount", src.getUserCount());
            responseObject.addProperty("sessionCount", src.getSessionCount());

            jsonObject.add("response", responseObject);
            return jsonObject;
        }
    }
}
