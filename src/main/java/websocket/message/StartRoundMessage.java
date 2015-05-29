package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartRoundMessage extends AbstractStartMessage {

    public StartRoundMessage(Room room, int playerId) {
        super(room, playerId);
    }
}
