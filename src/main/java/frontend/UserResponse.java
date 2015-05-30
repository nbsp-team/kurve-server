package frontend;

import model.UserProfile;

import javax.jws.soap.SOAPBinding;

/**
 * nickolay, 29.05.15.
 */
public class UserResponse {
    private UserProfile user;

    public UserResponse() {
    }

    public UserResponse(UserProfile userProfile) {
        this.user = userProfile;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public UserProfile getUser() {
        return user;
    }
}
