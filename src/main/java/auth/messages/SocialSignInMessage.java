package auth.messages;

import auth.SocialAccountService;
import frontend.message.MessageIsAuthenticated;
import messageSystem.Address;
import messageSystem.Message;
import model.UserProfile;

public final class SocialSignInMessage extends MessageToAccountService {
    private UserProfile user;

    public SocialSignInMessage(Address from, Address to, UserProfile user) {
        super(from, to);
        this.user = user;
    }

    @Override
    public void exec(SocialAccountService service) {
        UserProfile signUser = service.addUser(user);
        final Message back = new MessageIsAuthenticated(getTo(), getFrom(), this, signUser);
        service.getMessageSystem().sendMessage(back);
        System.out.println("Sending auth message back " + back.toString());
    }
}
