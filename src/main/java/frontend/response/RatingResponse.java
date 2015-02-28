package frontend.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * nickolay, 28.02.15.
 */
public class RatingResponse extends SuccessResponse {
    public RatingResponse() {
        Map<String, Object> data = new HashMap<>();
        ArrayList<Map<String, Object>> users = new ArrayList<>();

        for(int i = 0; i < 20; ++i) {
            Map<String, Object> user = new HashMap<>();
            user.put("username", "user" + i);
            user.put("global_rating", 100 + i * 5);
            users.add(user);
        }
        data.put("users", users);

        setData(data);
    }
}
