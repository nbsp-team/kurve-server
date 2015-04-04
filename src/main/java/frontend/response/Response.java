package frontend.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.UserProfile;

/**
 * nickolay, 25.02.15.
 */
public class Response {
    private static final Gson gson = new GsonBuilder()
            // Response adapters
            .registerTypeAdapter(ErrorResponse.class, new ErrorResponse.serializer())
            .registerTypeAdapter(AuthErrorResponse.class, new ErrorResponse.serializer())
            .registerTypeAdapter(PermissionDeniedErrorResponse.class, new ErrorResponse.serializer())
            .registerTypeAdapter(GetUserResponse.class, new GetUserResponse.serializer())
            .registerTypeAdapter(RatingResponse.class, new RatingResponse.serializer())
            .registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponse.serializer())
            .registerTypeAdapter(SignInResponse.class, new SignInResponse.serializer())
            .registerTypeAdapter(SignUpResponse.class, new SignUpResponse.serializer())
            .registerTypeAdapter(SuccessResponse.class, new SuccessResponse.serializer())
            // Model adapters
            .registerTypeAdapter(UserProfile.class, new UserProfile.serializer())
            // Configure Gson
            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);
    }

    public static String getContentType() {
        return "application/json";
    }
}
