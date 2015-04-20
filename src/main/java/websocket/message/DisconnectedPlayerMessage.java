package websocket.message;

import com.google.gson.*;
import game.Player;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * Created by Dimorinny on 20.03.15.
 */
public class DisconnectedPlayerMessage extends Message {

    private Player player;

    public DisconnectedPlayerMessage(Player player) {
        this.player = player;
        buildBody();
    }

    public Player getPlayer() {
        return player;
    }
}
