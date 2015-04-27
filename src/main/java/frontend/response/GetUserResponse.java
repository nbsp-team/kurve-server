package frontend.response;

import model.UserProfile;

/**
 * nickolay, 25.02.15.
 */
public class GetUserResponse extends SuccessResponse {
    private UserProfile userProfile;

    public GetUserResponse(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
