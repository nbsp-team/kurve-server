import interfaces.AccountService;
import main.MemoryAccountService;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * nickolay, 01.04.15.
 */
public class AccountServiceTests {
    public static AccountService accountService;

    @BeforeClass
    public static void init() {
        accountService = new MemoryAccountService();
    }

    @Test
    public void testRegister() {
        boolean result = accountService.addUser(new UserProfile("user", "test", "email@email.com"));
        assertTrue(result);

        UserProfile user = accountService.getUser("user");
        assertNotNull(user);
    }

    @Test
    public void testRegisterWhenUserAlreadyExists() {
        boolean result = accountService.addUser(new UserProfile("rty", "test", "email@email.com"));
        assertTrue(result);

        result = accountService.addUser(new UserProfile("rty", "test", "email@email.com"));
        assertFalse(result);
    }

    @Test
    public void testUserThatNotExist() {
        UserProfile test = accountService.getUser("abcdefg");
        assertNull(test);
    }

    @Test
    public void testCount() {
        accountService.clear();
        accountService.addUser(new UserProfile("test1", "test", "email1@email.com"));
        accountService.addUser(new UserProfile("test2", "test", "email2@email.com"));
        accountService.addUser(new UserProfile("test3", "test", "email3@email.com"));

        assertEquals(accountService.getUserCount(), 3);
    }

    @Test
    public void testClear() {
        accountService.addUser(new UserProfile("wtest1", "test", "email1@email.com"));
        accountService.addUser(new UserProfile("wtest2", "test", "email2@email.com"));
        accountService.addUser(new UserProfile("wtest3", "test", "email3@email.com"));

        accountService.clear();
        assertEquals(accountService.getUserCount(), 0);
    }
}
