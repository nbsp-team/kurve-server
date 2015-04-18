package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    private long userCount;
    private long sessionCount;

    public ServerStatusResponse(long userCount, long sessionCount) {
        this.userCount = userCount;
        this.sessionCount = sessionCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public long getSessionCount() {
        return sessionCount;
    }
}
