package websocket.message;

import game.Room;

/**
 * nickolay, 17.03.15.
 */
public class RoomPlayersMessage extends Message {
    private Room room;

    public RoomPlayersMessage(Room room) {
        this.room = room;
        buildBody();
    }

    public Room getRoom() {
        return room;
    }
}
