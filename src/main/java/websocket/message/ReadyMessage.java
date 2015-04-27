package websocket.message;

import game.Player;

/**
 * nickolay, 17.03.15.
 */
public class ReadyMessage extends Message {
    private boolean isReady;
    private Player player;

    public ReadyMessage(Player player, boolean isReady) {
        this.isReady = isReady;
        this.player = player;
    }

    public boolean isReady() {
        return isReady;
    }

    public Player getPlayer() {
        return player;
    }
}
