package frontend.response;

import com.google.gson.GsonBuilder;
import frontend.AbstractJsonMessage;
import frontend.response.serializer.*;
import model.UserProfile;

/**
 * nickolay, 25.02.15.
 */
public class Response  extends AbstractJsonMessage{
    Response() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(PermissionDeniedErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(AuthErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(GetUserResponse.class, new GetUserResponseSerializer())
                .registerTypeAdapter(RatingResponse.class, new RatingResponseSerializer())
                .registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponseSerializer())
                .registerTypeAdapter(SuccessResponse.class, new SuccessResponseSerializer())
                .registerTypeAdapter(GetMobileUrlResponse.class, new GetMobileUrlResponseSerializer())

                .registerTypeAdapter(UserProfile.class, new UserProfileSerializer())

                .serializeNulls()
                .create();
    }
}
