package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartGameMessage extends Message {
    private Room room;
    private int playerId;

    public StartGameMessage(Room room, int playerId) {
        this.playerId = playerId;

        this.room = room;
        buildBody();
    }

    public Room getRoom() {
        return room;
    }

    public int getPlayerId() {
        return playerId;
    }


}
