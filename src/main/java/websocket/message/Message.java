package websocket.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Player;
import model.Snake.SnakePartArc;
import model.Snake.SnakePartLine;
import websocket.message.serializer.*;

/**
 * nickolay, 17.03.15.
 */
public abstract class Message {
    private String body;

    protected void buildBody(){body = gson.toJson(this);}
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ControlMessage.class, new ControlMessageSerializer())
            .registerTypeAdapter(SnakePartArc.class, new SnakePartArcSerializer())
            .registerTypeAdapter(SnakePartLine.class, new SnakePartLineSerializer())
            .registerTypeAdapter(SnakeUpdateMessage.class, new SnakeUpdateMessageSerializer())
            .registerTypeAdapter(GameOverMessage.class, new GameOverMessageSerializer())
            .registerTypeAdapter(RoomPlayersMessage.class, new RoomPlayersMessageSerializer())
            .registerTypeAdapter(ConnectedPlayerMessage.class, new ConnectedPlayerMessageSerializer())
            .registerTypeAdapter(DisconnectedPlayerMessage.class, new DisconnectedPlayerMesageSerializer())
            .registerTypeAdapter(ReadyMessage.class, new ReadyMessageSerializer())
            .registerTypeAdapter(StartGameMessage.class, new StartGameMessageSerializer())
            .registerTypeAdapter(Player.class, new PlayerSerializer())
            .serializeNulls()
            .create();

    public String getBody() {
        return body;

    }
}
