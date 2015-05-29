package auth;

import com.mongodb.DB;
import dao.UsersDao;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import model.UserProfile;

import java.net.UnknownHostException;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class MongoAccountService extends SocialAccountService {
    private UsersDao usersDao;

    public MongoAccountService(MessageSystem messageSystem, DB db) throws UnknownHostException {
        super(messageSystem);
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
}
