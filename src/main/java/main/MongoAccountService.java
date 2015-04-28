package main;

import com.mongodb.*;
import interfaces.AccountService;
import model.UserProfile;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;


/**
 * Created by Dimorinny on 29.04.15.
 */
public class MongoAccountService implements AccountService, Closeable {

    private MongoClient mongoClient;
    private DBCollection usersCollection;

    private static final String USERS_DB_NAME = "users";
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String PASSWORD_FIELD_NAME = "password";
    private static final String EMAIL_FIELD_NAME = "email";

    public MongoAccountService(String host, int port, String dbName) throws UnknownHostException {
        mongoClient = new MongoClient(host, port);
        usersCollection = mongoClient.getDB(dbName)
                .getCollection(USERS_DB_NAME);
    }

    @Override
    public boolean addUser(UserProfile userProfile) {

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(LOGIN_FIELD_NAME, userProfile.getLogin());
        long usersCount = usersCollection.getCount(searchQuery);

        if(usersCount == 0) {
            BasicDBObject userObject = new BasicDBObject();
            userObject.append(LOGIN_FIELD_NAME, userProfile.getLogin());
            userObject.append(EMAIL_FIELD_NAME, userProfile.getEmail());
            userObject.append(PASSWORD_FIELD_NAME, userProfile.getPassword());
            usersCollection.insert(userObject);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserProfile getUser(String userName) {

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(LOGIN_FIELD_NAME, userName);

        DBObject userObject = usersCollection.findOne(searchQuery);

        if(userObject != null) {
            return new UserProfile(userObject.get(LOGIN_FIELD_NAME).toString(),
                    userObject.get(PASSWORD_FIELD_NAME).toString(), userObject.get(EMAIL_FIELD_NAME).toString());
        } else {
            return null;
        }
    }

    @Override
    public long getUserCount() {
        return usersCollection.getCount();
    }

    @Override
    public void clear() {
        usersCollection.drop();
    }

    @Override
    public void close() throws IOException {
        mongoClient.close();
    }
}
