package frontend.response;

import model.UserProfile;

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
