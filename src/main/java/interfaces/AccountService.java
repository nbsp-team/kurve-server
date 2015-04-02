package interfaces;

import model.UserProfile;

/**
 * nickolay, 21.03.15.
 */
public interface AccountService {
    public boolean addUser(UserProfile userProfile);
    public UserProfile getUser(String userName);
    public long getUserCount();
    public void clear();
}
