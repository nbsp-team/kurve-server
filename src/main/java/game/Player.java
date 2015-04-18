package game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.UserProfile;
import websocket.WebSocketConnection;
import websocket.message.Message;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * nickolay, 21.02.15.
 */
public class Player {
    public static final Color playerColors[] = {
            new Color(244, 67, 54),
            new Color(76, 175, 80),
            new Color(33, 150, 243),
            new Color(233, 30, 99),
            new Color(103, 58, 183),
            new Color(255, 235, 59),
            new Color(255, 152, 0),
            new Color(156, 39, 176),
    };

    private String id;
    private int points = 0;
    private Set<WebSocketConnection> connections;
    private Color color;
    private UserProfile userProfile;
//  private Snake snake;
    private boolean isReady = false;

    public Player(Color color, UserProfile userProfile) {
        this.id = UUID.randomUUID().toString();
        this.connections = new HashSet<>();
        this.color = color;
        this.userProfile = userProfile;
        //this.snake = new Snake();
    }

    public void addConnection(WebSocketConnection connection) {
        connections.add(connection);
    }

    public void sendMessage(Message message) {
        for(WebSocketConnection connection : connections) {
            connection.sendMessage(message);
        }
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

//    public Snake getSnake() {
//        return snake;
//    }

    public String getId() {
        return id;
    }
}
