package websocket.message;

import game.Room;

/**
 * Created by egor on 30.05.15.
 */
public class AbstractStartMessage extends Message {
    private Room room;
    private int playerId;

    public AbstractStartMessage(Room room, int playerId) {
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
