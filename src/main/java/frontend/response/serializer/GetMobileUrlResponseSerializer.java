package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.GetMobileUrlResponse;
import main.Main;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class GetMobileUrlResponseSerializer implements JsonSerializer<GetMobileUrlResponse> {
    public JsonElement serialize(GetMobileUrlResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();

        String url = "http://" + Main.networkConfig.getString("domain") + "/api/v" + Main.API_VERSION + "/mobile/auth?hash=" + src.getSession();
        responseObject.add("mobile_url", new JsonPrimitive(url));

        jsonObject.add("response", responseObject);

        return jsonObject;
    }
}
