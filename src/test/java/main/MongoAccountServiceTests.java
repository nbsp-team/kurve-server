package main;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import configuration.DatabaseConfig;
import configuration.XmlLoader;
import interfaces.SocialAccountService;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.SocialAuthHelper;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * nickolay, 28.05.15.
 */
public class MongoAccountServiceTests {
    private static SocialAccountService accountService;

    public static final String DB_CONFIG_FILE = "config/db.xml";
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

        accountService = new MongoAccountService(db);
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

        testUser = accountService.getUserById(testUser.getId());
        assertNotNull(testUser);

        accountService.removeUser(testUser.getId());
        testUser = accountService.getUserById(testUser.getId());
        assertNull(testUser);
    }
}
