package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import model.UserProfile;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * nickolay, 05.05.15.
 */
public class UsersDao {
    public static final String USERS_COLLECTION = "users";

    public static final String USER_ID_FIELD_NAME = "_id";
    public static final String FIRST_NAME_FIELD_NAME = "first_name";
    public static final String LAST_NAME_FIELD_NAME = "last_name";
    public static final String AVATAR_URL_FIELD_NAME = "avatar";
    public static final String AUTH_PROVIDER_FIELD_NAME = "auth_provider";
    public static final String SOCIAL_ID_FIELD_NAME = "social_id";

    private final DBCollection users;
    private ScoresDao scoresDao;

    public UsersDao(DB db) {
        this.users = db.getCollection(USERS_COLLECTION);
        this.scoresDao = new ScoresDao(db);
    }

    public UserProfile insert(UserProfile user) {

        BasicDBObject userQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject(SOCIAL_ID_FIELD_NAME, user.getSocialID()));
        obj.add(new BasicDBObject(AUTH_PROVIDER_FIELD_NAME, user.getAuthProvider()));
        userQuery.put("$and", obj);

        DBObject oldUser = users.findOne(userQuery);

        BasicDBObject userObject = new BasicDBObject();
        userObject.append(FIRST_NAME_FIELD_NAME, user.getFirstName());
        userObject.append(LAST_NAME_FIELD_NAME, user.getLastName());
        userObject.append(AVATAR_URL_FIELD_NAME, user.getAvatarUrl());
        userObject.append(AUTH_PROVIDER_FIELD_NAME, user.getAuthProvider());
        userObject.append(SOCIAL_ID_FIELD_NAME, user.getSocialID());

        String id;
        if (oldUser != null) {
            // TODO: set access token using $set
            //users.update(userQuery, userObject);
            id = oldUser.get("_id").toString();
        } else {
            users.insert(userObject);
            id = userObject.get("_id").toString();
        }

        user.setId(id);
        return user;
    }


    public void remove(String id) {
        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        DBObject userObject = users.findOne(searchQuery);

        users.remove(userObject);
    }

    public UserProfile getById(String id) {
        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        DBObject userObject = users.findOne(searchQuery);

        if (userObject != null) {
            return new UserProfile(
                    userObject.get(USER_ID_FIELD_NAME).toString(),
                    userObject.get(FIRST_NAME_FIELD_NAME).toString(),
                    userObject.get(LAST_NAME_FIELD_NAME).toString(),
                    userObject.get(AVATAR_URL_FIELD_NAME).toString(),
                    (int) userObject.get(AUTH_PROVIDER_FIELD_NAME),
                    userObject.get(SOCIAL_ID_FIELD_NAME).toString(),
                    scoresDao.getPoints(id)
            );
        } else {
            return null;
        }
    }

    public long getCount() {
        return users.getCount();
    }

    public void clear() {
        users.drop();
    }
}
