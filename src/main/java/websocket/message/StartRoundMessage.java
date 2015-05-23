package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartRoundMessage extends Message {
    private Room room;
    private int playerId;

    public StartRoundMessage(Room room, int playerId) {
        this.playerId = playerId;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public int getPlayerId() {
        return playerId;
    }
}
