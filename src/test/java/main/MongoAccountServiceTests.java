package main;

import auth.MongoAccountService;
import auth.SocialAccountService;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import configuration.DatabaseConfig;
import configuration.XmlLoader;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.SocialAuthHelper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * nickolay, 28.05.15.
 */
public class MongoAccountServiceTests {
    private static SocialAccountService accountService;

    public static final String DB_CONFIG_FILE = "config/test_db.xml";
    public static final DatabaseConfig dbConfig =
            (DatabaseConfig) XmlLoader.getInstance()
                    .load(DatabaseConfig.class, DB_CONFIG_FILE);

    @BeforeClass
    public static void init() throws UnknownHostException {
        ServerAddress mongoServer = new ServerAddress(dbConfig.host, Integer.valueOf(dbConfig.port));
        MongoCredential credential = MongoCredential.createCredential(
                dbConfig.username,
                dbConfig.name,
                dbConfig.password.toCharArray()
        );

        MongoClient mongoClient = new MongoClient(mongoServer, new ArrayList<MongoCredential>() {{
            add(credential);
        }});
        DB db = mongoClient.getDB(dbConfig.name);

        accountService = new MongoAccountService( db);
    }

    @Test
    public void testUser() {
        UserProfile testUser = accountService.addUser(new UserProfile(
                "Test",
                "Test user",
                "http://example.org/",
                SocialAuthHelper.AuthProvider.AUTH_PROVIDER_GUEST.ordinal(),
                "0"
        ));
        assertNotNull(testUser);

        UserProfile dbUser = accountService.getUserById(testUser.getId());
        assertNotNull(dbUser);

        assertEquals("Test", dbUser.getFirstName());

        accountService.removeUser(dbUser.getId());
        testUser = accountService.getUserById(dbUser.getId());
        assertNull(testUser);
    }

    @Test
    public void testCount() {
        long userCount = 10;
        long beforeCount = accountService.getUserCount();

        List<UserProfile> userProfileList = new ArrayList<>();
        for(int i = 0; i < userCount; ++i) {
            userProfileList.add(accountService.addUser(new UserProfile(
                    "Test",
                    "Test user",
                    "http://example.org/",
                    SocialAuthHelper.AuthProvider.AUTH_PROVIDER_GUEST.ordinal(),
                    UUID.randomUUID().toString()
            )));
        }

        long afterCount = accountService.getUserCount();

        assertEquals(afterCount - beforeCount, userCount);

        for(UserProfile user : userProfileList) {
            accountService.removeUser(user.getId());
        }

        long afterRemoveCount = accountService.getUserCount();
        assertEquals(beforeCount, afterRemoveCount);
    }
}