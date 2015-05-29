package auth.messages;

import auth.SocialAccountService;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import model.UserProfile;

/**
 * nickolay, 29.05.15.
 */
public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof SocialAccountService) {
            exec((SocialAccountService) abonent);
        }
    }

    protected abstract void exec(SocialAccountService service);
}
