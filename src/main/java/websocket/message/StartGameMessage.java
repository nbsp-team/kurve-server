package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartGameMessage extends AbstractStartMessage {
    private final int roundCount;
    private final int currentRound;

    public StartGameMessage(Room room, int playerId, int currentRound, int roundCount) {
        super(room, playerId);
        this.currentRound = currentRound;
        this.roundCount = roundCount;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
