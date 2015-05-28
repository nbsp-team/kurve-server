package main;

import com.mongodb.DB;
import dao.UsersDao;
import interfaces.SocialAccountService;
import model.UserProfile;
import service.Address;
import service.Request;
import service.Response;
import service.Service;
import utils.Bundle;

import java.net.UnknownHostException;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class MongoAccountService extends Service implements SocialAccountService {
    private final Address address = new Address();

    private UsersDao usersDao;

    public MongoAccountService(DB db) throws UnknownHostException {
        this.usersDao = new UsersDao(db);
    }

    @Override
    public UserProfile addUser(UserProfile userProfile) {
        return usersDao.insert(userProfile);
    }

    @Override
    public UserProfile getUserById(String userName) {
        return usersDao.getById(userName);
    }

    @Override
    public void removeUser(String id) {
        usersDao.remove(id);
    }

    @Override
    public long getUserCount() {
        return usersDao.getCount();
    }

    @Override
    public void clear() {
        usersDao.clear();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    protected Response processRequest(Request request) {
        switch (request.getMethod()) {
            case "add_user":
                addUser((UserProfile) request.getArgs().getSerializable("user"));
                return null;
            case "get_user":
                UserProfile userProfile = getUserById(request.getArgs().getString("id"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userProfile);
                return new Response(bundle);
            case "remove_user":
                removeUser(request.getArgs().getString("id"));
                return null;
            case "clear":
                clear();
                return null;
        }

        return null;
    }
}
