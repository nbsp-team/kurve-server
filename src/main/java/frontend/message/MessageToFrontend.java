package frontend.message;

import auth.SocialAccountService;
import frontend.FrontendService;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

/**
 * nickolay, 29.05.15.
 */
public abstract class MessageToFrontend extends Message {
    public MessageToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof FrontendService) {
            exec((FrontendService) abonent);
        }
    }

    protected abstract void exec(FrontendService service);
}
