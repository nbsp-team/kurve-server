package frontend.response;

import frontend.response.SuccessResponse;
import model.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * nickolay, 28.02.15.
 */
public class ServerStatusResponse extends SuccessResponse {
    public ServerStatusResponse(int userCount, int sessionCount) {
        Map<String, Object> data = new HashMap<>();

        data.put("userCount", userCount);
        data.put("sessionCount", sessionCount);

        setData(data);
    }
}
