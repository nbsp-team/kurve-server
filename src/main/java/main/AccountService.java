package main;

import model.UserProfile;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userProfile);
        return true;
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public int getUserCount() {
        return users.size();
    }
}
