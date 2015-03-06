package game;

import model.UserProfile;
import model.snake.Snake;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * nickolay, 21.02.15.
 */
public class Player {
    private int points = 0;
    private Set<Session> sessions;
    private Color color;
    private UserProfile userProfile;
    private Snake snake;
    private boolean isReady = false;

    public Player(Color color, UserProfile userProfile) {
        this.sessions = new HashSet<>();
        this.color = color;
        this.userProfile = userProfile;
        this.snake = new Snake();
    }

    public void connectSession(Session session) {
        sessions.add(session);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public Color getColor() {
        return color;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Snake getSnake() {
        return snake;
    }
}
