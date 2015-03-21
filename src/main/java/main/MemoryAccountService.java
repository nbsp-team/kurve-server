package main;

import interfaces.AccountService;
import model.UserProfile;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * v.chibrikov, 13.09.2014.
 */
public class MemoryAccountService implements AccountService {
    private Map<String, UserProfile> users = new HashMap<>();

    public MemoryAccountService() {
        // TODO: for debugging only
        addUser("admin", new UserProfile("admin", "admin", "didika914@gmail.com"));
    }

    @Override
    public boolean addUser(String userName, UserProfile userProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userProfile);
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
}
