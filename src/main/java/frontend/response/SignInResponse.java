package frontend.response;

import frontend.response.SuccessResponse;
import model.UserProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * nickolay, 25.02.15.
 */
public class SignInResponse extends SuccessResponse {
    public SignInResponse(UserProfile profile) {
        Map<String, Object> data = new HashMap<>();

        data.put("username", profile.getLogin());
        data.put("email", profile.getEmail());

        setData(data);
    }
}
