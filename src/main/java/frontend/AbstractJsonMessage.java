package frontend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.response.*;
import frontend.response.serializer.*;
import model.UserProfile;

/**
 * Created by egor on 30.05.15.
 */
public abstract class AbstractJsonMessage {
    protected static Gson gson;

    public String getBody() {
        return gson.toJson(this);
    }

    public static String getContentType() {
        return "application/json";
    }
}
