package main;

import interfaces.AccountService;
import model.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * v.chibrikov, 13.09.2014.
 */
public class AccountServiceInMemory implements AccountService {
    private Map<String, UserProfile> users = new HashMap<>();

    public AccountServiceInMemory() {
        // TODO: for debugging only
        addUser(new UserProfile("admin", "admin", "didika914@gmail.com"));
        addUser(new UserProfile("q", "q", "q@gmail.com"));
    }

    @Override
    public boolean addUser(UserProfile userProfile) {
        if (users.containsKey(userProfile.getLogin()))
            return false;
        users.put(userProfile.getLogin(), userProfile);
        return true;
    }

    @Override
    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    @Override
    public long getUserCount() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }
}
