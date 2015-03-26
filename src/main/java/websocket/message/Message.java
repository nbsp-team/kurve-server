package websocket.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Player;

/**
 * nickolay, 17.03.15.
 */
public abstract class Message {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RoomPlayersMessage.class, new RoomPlayersMessage.serializer())
            .registerTypeAdapter(ConnectedPlayerMessage.class, new ConnectedPlayerMessage.serializer())
            .registerTypeAdapter(DisconnectedPlayerMessage.class, new DisconnectedPlayerMessage.serializer())
            .registerTypeAdapter(ReadyMessage.class, new ReadyMessage.serializer())
            .registerTypeAdapter(Player.class, new Player.serializer())
            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);
    }
}
