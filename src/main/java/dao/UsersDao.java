package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import model.UserProfile;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 05.05.15.
 */
public class UsersDao {
    private static final String USERS_COLLECTION = "users";

    private static final String ID_FIELD_NAME = "_id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String LAST_NAME_FIELD_NAME = "last_name";
    private static final String AVATAR_URL_FIELD_NAME = "avatar";
    private static final String AUTH_PROVIDER_FIELD_NAME = "auth_provider";
    private static final String SOCIAL_ID_FIELD_NAME = "social_id";

    private DB db;

    public UsersDao(DB db) {
        this.db = db;
    }

    public UserProfile insert(UserProfile user) {
        DBCollection users = db.getCollection(USERS_COLLECTION);

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
            users.update(userQuery, userObject);
            id = oldUser.get("_id").toString();

        } else {
            users.insert(userObject);
            id = userObject.get("_id").toString();
        }

        user.setId(id);
        return user;
    }


    public void remove(String id) {
        DBCollection users = db.getCollection(USERS_COLLECTION);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        DBObject userObject = users.findOne(searchQuery);

        users.remove(userObject);
    }

    public UserProfile getById(String id) {
        DBCollection users = db.getCollection(USERS_COLLECTION);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        DBObject userObject = users.findOne(searchQuery);

        if (userObject != null) {
            return new UserProfile(
                    userObject.get(ID_FIELD_NAME).toString(),
                    userObject.get(FIRST_NAME_FIELD_NAME).toString(),
                    userObject.get(LAST_NAME_FIELD_NAME).toString(),
                    userObject.get(AVATAR_URL_FIELD_NAME).toString(),
                    Integer.valueOf(userObject.get(AUTH_PROVIDER_FIELD_NAME).toString()),
                    userObject.get(SOCIAL_ID_FIELD_NAME).toString()
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
