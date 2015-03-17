package websocket.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.response.*;
import game.Player;
import model.UserProfile;

/**
 * nickolay, 17.03.15.
 */
public abstract class Message {
    private static final Gson gson = new GsonBuilder()
            // Response adapters
            .registerTypeAdapter(RoomPlayersMessage.class, new RoomPlayersMessage.serializer())
            // Model adapters
            .registerTypeAdapter(Player.class, new Player.serializer())
            // Configure Gson
            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);
    }
}
