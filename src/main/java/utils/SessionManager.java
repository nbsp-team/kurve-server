package utils;

import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * nickolay, 06.03.15.
 */
public class SessionManager extends HashSessionIdManager {
    public Optional<HttpSession> getSessionById(String id) {
        Collection<HttpSession> sessions = getSession(id);

        if(!sessions.isEmpty()) {
            return Optional.ofNullable(sessions.iterator().next());
        } else {
            return null;
        }
    }
}
