package websocket.message;

import game.Player;

/**
 * Created by Dimorinny on 20.03.15.
 */
public class DisconnectedPlayerMessage extends Message {

    private Player player;

    public DisconnectedPlayerMessage(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
