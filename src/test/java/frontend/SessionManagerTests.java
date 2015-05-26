package frontend;

import model.UserProfile;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.HashedSession;
import org.junit.Before;
import org.junit.Test;
import utils.SocialAuthHelper;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * nickolay, 22.05.15.
 */
public class SessionManagerTests {
    private SessionManager sessionManager;

    @Before
    public void init() {
        sessionManager = new SessionManager();
    }

    @Test
    public void testGetSessionById(){
        String sessionId = "abc";

        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn(sessionId);

        sessionManager.addSession(session);
        Optional<HttpSession> actualSession = sessionManager.getSessionById(sessionId);

        assertTrue(actualSession.isPresent());
        assertEquals(session, actualSession.get());
    }
}
