package auth;

import model.UserProfile;
import service.Address;

import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class AccountServiceInMemory extends SocialAccountService {
    private final Address address = new Address();
    HashMap<String, UserProfile> users;

    public AccountServiceInMemory(){
        users = new HashMap<>();
    }

    @Override
    public UserProfile addUser(UserProfile userProfile) {
        String id = UUID.randomUUID().toString();
        userProfile.setId(id);
        users.put(id, userProfile);
        return userProfile;
    }

    @Override
    public UserProfile getUserById(String id) {
        return users.get(id);
    }

    @Override
    public void removeUser(String id) {
        users.remove(id);
    }

    @Override
    public long getUserCount() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
