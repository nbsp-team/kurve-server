package main;

import com.mongodb.DB;
import dao.UsersDao;
import interfaces.AccountService;
import model.UserProfile;

import java.net.UnknownHostException;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class MongoAccountService implements AccountService {
    private DB db;
    private UsersDao usersDao;

    public MongoAccountService(DB db) throws UnknownHostException {
        this.db = db;
        this.usersDao = new UsersDao(db);
    }

    @Override
    public boolean addUser(UserProfile userProfile) {
        return usersDao.insertUser(userProfile);
    }

    @Override
    public UserProfile getUser(String userName) {
        return usersDao.getUser(userName);
    }

    @Override
    public long getUserCount() {
        return usersDao.getCount();
    }

    @Override
    public void clear() {
        usersDao.clear();
    }
}
