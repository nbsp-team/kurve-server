package frontend.message;

import auth.SocialAccountService;
import frontend.FrontendService;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import model.UserProfile;

public final class MessageIsAuthenticated extends MessageToFrontend {
    private Message request;
    private UserProfile user;

    public MessageIsAuthenticated(Address from, Address to, Message request, UserProfile user) {
        super(from, to);
        this.request = request;
        this.user = user;
    }

    @Override
    protected void exec(FrontendService frontendService) {
        frontendService.onAuth(user, request);
    }

}
