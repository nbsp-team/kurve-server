package websocket.message;

import com.google.gson.*;
import game.Player;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * Created by Dimorinny on 20.03.15.
 */
public class ConnectedPlayerMessage extends Message {

    private Player player;
    private int playerId;

    public ConnectedPlayerMessage(Player player, int playerId) {
        this.player = player;
        this.playerId = playerId;
        buildBody();
    }

    public Player getPlayer() {
        return player;
    }
    public int getPlayerId() {
        return playerId;
    }
}
