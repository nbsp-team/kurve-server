package frontend.response;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    private long userCount;
    private long sessionCount;
    private long roomCount;

    public ServerStatusResponse(long userCount, long sessionCount, long roomCount) {
        this.userCount = userCount;
        this.sessionCount = sessionCount;
        this.roomCount = roomCount;
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
}
