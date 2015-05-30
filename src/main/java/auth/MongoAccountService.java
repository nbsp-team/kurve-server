package auth;

import com.mongodb.DB;
import dao.UsersDao;
import model.UserProfile;

import java.net.UnknownHostException;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class MongoAccountService extends SocialAccountService {
    private UsersDao usersDao;

    public MongoAccountService(DB db) throws UnknownHostException {
        this.usersDao = new UsersDao(db);
    }

    @Override
    public UserProfile addUser(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
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
