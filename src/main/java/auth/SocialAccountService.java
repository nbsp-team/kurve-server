package auth;

import model.UserProfile;

/**
 * nickolay, 21.03.15.
 */
public abstract class SocialAccountService {
    public abstract UserProfile addUser(UserProfile userProfile);

    public abstract UserProfile getUserById(String id);

    public abstract void removeUser(String id);

    public abstract long getUserCount();

    public abstract void clear();
}
