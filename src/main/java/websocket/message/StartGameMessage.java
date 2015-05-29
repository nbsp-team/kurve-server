package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartGameMessage extends AbstractStartMessage {


    public StartGameMessage(Room room, int playerId) {
        super(room, playerId);
    }
}
