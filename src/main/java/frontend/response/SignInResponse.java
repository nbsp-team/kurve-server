package frontend.response;

import model.UserProfile;

/**
 * nickolay, 25.02.15.
 */
public class SignInResponse extends SuccessResponse {
    private UserProfile userProfile;

    public SignInResponse(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

}
