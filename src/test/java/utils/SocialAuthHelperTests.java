package utils;

import model.UserProfile;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * nickolay, 22.05.15.
 */
public class SocialAuthHelperTests {
    @Test
    public void testGuestUser() {
        UserProfile user = SocialAuthHelper.auth(SocialAuthHelper.AuthProvider.AUTH_PROVIDER_GUEST, "test");
        assertNotNull(user);

        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
    }
}
