package game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import main.Main;
import model.UserProfile;
import websocket.WebSocketConnection;
import websocket.message.Message;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * nickolay, 21.02.15.
 */
public class Player {
    public static final List<String> playerColors = Main.mechanicsConfig.colors;

    private String id;
    private int points = 0;
    private Set<WebSocketConnection> connections;
    private String color;
    private UserProfile userProfile;
    private boolean isReady = false;

    public Player(String color, UserProfile userProfile) {
        this.id = UUID.randomUUID().toString();
        this.connections = new HashSet<>();
        this.color = color;
        this.userProfile = userProfile;
    }

    public void addConnection(WebSocketConnection connection) {
        connections.add(connection);
    }

    public int getConnectionCount() {
        return connections.size();
    }

    public void removeConnection(WebSocketConnection connection) {
        connections.remove(connection);
    }

    public void sendMessage(Message message) {
        for (WebSocketConnection connection : connections) {
            connection.sendMessage(message);
        }
    }

    public void sendMessageExceptConnection(Message message, WebSocketConnection exceptedConnection) {
        for (WebSocketConnection connection : connections) {
            if (!connection.equals(exceptedConnection)) {
                connection.sendMessage(message);
            }
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public String getColor() {
        return color;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }


    public String getId() {
        return id;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public static class serializer implements JsonSerializer<Player> {
        public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("user_id", src.getUserProfile().getId());
            jsonObject.addProperty("first_name", src.getUserProfile().getFirstName());
            jsonObject.addProperty("last_name", src.getUserProfile().getLastName());
            jsonObject.addProperty("avatar", src.getUserProfile().getAvatarUrl());
            jsonObject.addProperty("player_id", src.getId());
            jsonObject.addProperty("global_rating", 0);
            jsonObject.addProperty("is_ready", src.isReady());
            jsonObject.addProperty("color",
                    src.getColor()
            );

            return jsonObject;
        }
    }
}
