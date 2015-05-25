package interfaces;

import model.UserProfile;

/**
 * nickolay, 21.03.15.
 */
public interface SocialAccountService {
    UserProfile addUser(UserProfile userProfile);

    UserProfile getUserById(String id);

    long getUserCount();

    void clear();
}
