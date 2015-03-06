package utils;

import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Optional;

/**
 * nickolay, 06.03.15.
 */
public class SessionManager extends HashSessionIdManager {
    public Optional<HttpSession> getSessionById(String id) {
        Collection<HttpSession> session = getSession(id);
        return session.stream().findFirst();
    }
}
