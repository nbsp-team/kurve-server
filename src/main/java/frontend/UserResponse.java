package frontend;

import model.UserProfile;

import javax.jws.soap.SOAPBinding;

/**
 * nickolay, 29.05.15.
 */
public class UserResponse {
    private UserProfile user;
    private boolean empty = true;

    public UserResponse() {
    }

    public UserResponse(UserProfile userProfile) {
        this.user = userProfile;
        empty = false;
    }

    public void setUser(UserProfile user) {
        this.user = user;
        empty = false;
    }

    public UserProfile getUser() {
        return user;
    }

    public boolean isEmpty() {
        return empty;
    }
}
