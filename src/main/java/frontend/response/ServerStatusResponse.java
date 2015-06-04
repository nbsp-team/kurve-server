package frontend.response;

import game.GameService;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    private final GameService gameService;
    private long userCount;
    private long sessionCount;
    private long roomCount;

    public ServerStatusResponse(long userCount, long sessionCount, GameService gameService) {
        this.userCount = userCount;
        this.sessionCount = sessionCount;
        this.roomCount = gameService.getRoomCount();
        this.gameService = gameService;
    }

    public long getUserCount() {
        return userCount;
    }

    public long getSessionCount() {
        return sessionCount;
    }

    public long getRoomCount() {
        return roomCount;
    }

    public GameService getGameService() {
        return gameService;
    }
}
