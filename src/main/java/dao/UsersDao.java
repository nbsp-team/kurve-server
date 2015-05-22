package dao;

import com.mongodb.*;
import model.UserProfile;

/**
 * nickolay, 05.05.15.
 */
public class UsersDao {
    private static final String USERS_COLLECTION = "users";

    private static final String LOGIN_FIELD_NAME = "login";
    private static final String PASSWORD_FIELD_NAME = "password";
    private static final String EMAIL_FIELD_NAME = "email";

    private DB db;

    public UsersDao(DB db) {
        this.db = db;
    }

    public boolean insertUser(UserProfile user) {
        DBCollection users = db.getCollection(USERS_COLLECTION);

        BasicDBObject userObject = new BasicDBObject();
        userObject.append(LOGIN_FIELD_NAME, user.getLogin());
        userObject.append(EMAIL_FIELD_NAME, user.getEmail());
        userObject.append(PASSWORD_FIELD_NAME, user.getPassword());

        CommandResult result = users.insert(userObject).getLastError();
        return result.getBoolean("ok");
    }

    public UserProfile getUser(String userName) {
        DBCollection users = db.getCollection(USERS_COLLECTION);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(LOGIN_FIELD_NAME, userName);
        DBObject userObject = users.findOne(searchQuery);

        if (userObject != null) {
            return new UserProfile(
                    userObject.get(LOGIN_FIELD_NAME).toString(),
                    userObject.get(PASSWORD_FIELD_NAME).toString(),
                    userObject.get(EMAIL_FIELD_NAME).toString()
            );
        } else {
            return null;
        }
    }

    public long getCount() {
        return db.getCollection(USERS_COLLECTION).getCount();
    }

    public void clear() {
        db.getCollection(USERS_COLLECTION).drop();
    }
}
