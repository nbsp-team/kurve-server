package frontend.response;

import com.google.gson.*;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * nickolay, 25.02.15.
 */
public class SignUpResponse extends SuccessResponse {
    private UserProfile userProfile;

    public SignUpResponse(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
