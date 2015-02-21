package game;

import model.UserProfile;
import model.snake.Snake;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * nickolay, 21.02.15.
 */
public class Player {
    private int points = 0;
    private Set<String> sessions;
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

    public void connectSession(String sessionId) {
        sessions.add(sessionId);
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
