package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    private long userCount;
    private long sessionCount;

    public ServerStatusResponse(long userCount, long sessionCount) {
        this.userCount = userCount;
        this.sessionCount = sessionCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public long getSessionCount() {
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
