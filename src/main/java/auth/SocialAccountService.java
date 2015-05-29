package auth;

import model.UserProfile;
import service.Request;
import service.Response;
import service.Service;
import utils.Bundle;

/**
 * nickolay, 21.03.15.
 */
public abstract class SocialAccountService extends Service {
    public abstract UserProfile addUser(UserProfile userProfile);

    public abstract UserProfile getUserById(String id);

    public abstract void removeUser(String id);

    public abstract long getUserCount();

    public abstract void clear();

    @Override
    protected Response processRequest(Request request) {
        switch (request.getMethod()) {
            case "add_user":
                UserProfile user = addUser((UserProfile) request.getArgs().getSerializable("user"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                addResponse(request, new Response(bundle));
                return null;
            case "get_user":
                UserProfile userProfile = getUserById(request.getArgs().getString("id"));
                bundle = new Bundle();
                bundle.putSerializable("user", userProfile);
                return new Response(bundle);
            case "remove_user":
                removeUser(request.getArgs().getString("id"));
                return null;
            case "clear":
                clear();
                return null;
        }

        return null;
    }
}
