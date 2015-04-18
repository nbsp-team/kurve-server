package websocket.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Player;
import websocket.message.serializer.*;

/**
 * nickolay, 17.03.15.
 */
public abstract class Message {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RoomPlayersMessage.class, new RoomPlayersMessageSerializer())
            .registerTypeAdapter(ConnectedPlayerMessage.class, new ConnectedPlayerMessageSerializer())
            .registerTypeAdapter(DisconnectedPlayerMessage.class, new DisconnectedPlayerMesageSerializer())
            .registerTypeAdapter(ReadyMessage.class, new ReadyMessageSerializer())
            .registerTypeAdapter(StartGameMessage.class, new StartGameMessageSerializer())
            .registerTypeAdapter(Player.class, new PlayerSerializer())
            .serializeNulls()
            .create();

    public String getBody() {
        return gson.toJson(this);
    }
}
