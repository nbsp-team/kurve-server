package websocket.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Player;
import model.Snake.Snake;
import model.Snake.SnakePartArc;
import model.Snake.SnakePartLine;

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
            .registerTypeAdapter(StartGameMessage.class, new StartGameMessage.serializer())
            .registerTypeAdapter(KeyMessage.class, new KeyMessage.serializer())
            .registerTypeAdapter(SnakePartArc.class, new SnakePartArc.serializer())
            .registerTypeAdapter(SnakePartLine.class, new SnakePartLine.serializer())
            .registerTypeAdapter(SnakeUpdateMessage.class, new SnakeUpdateMessage.serializer())
            .registerTypeAdapter(GameOverMessage.class, new GameOverMessage.serializer())
            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);

    }
}
