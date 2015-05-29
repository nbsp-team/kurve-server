package websocket.message;

import game.Room;

/**
 * nickolay, 17.03.15.
 */
public class RoomPlayersMessage extends AbstractMessageToRoom {

    public RoomPlayersMessage(Room room) {
        super(room);
    }
}
