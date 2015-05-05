package frontend.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.response.serializer.*;
import model.UserProfile;

/**
 * nickolay, 25.02.15.
 */
public class Response {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ErrorResponse.class, new ErrorResponseSerializer())
            .registerTypeAdapter(PermissionDeniedErrorResponse.class, new ErrorResponseSerializer())
            .registerTypeAdapter(AuthErrorResponse.class, new ErrorResponseSerializer())
            .registerTypeAdapter(GetUserResponse.class, new GetUserResponseSerializer())
            .registerTypeAdapter(RatingResponse.class, new RatingResponseSerializer())
            .registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponseSerializer())
            .registerTypeAdapter(SignInResponse.class, new SignInResponseSerializer())
            .registerTypeAdapter(SignUpResponse.class, new SingUpResponseSerializer())
            .registerTypeAdapter(SuccessResponse.class, new SuccessResponseSerializer())

            .registerTypeAdapter(UserProfile.class, new UserProfileSerializer())

            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);
    }

    public static String getContentType() {
        return "application/json";
    }
}
