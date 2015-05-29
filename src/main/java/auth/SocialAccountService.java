package auth;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import model.UserProfile;

/**
 * nickolay, 21.03.15.
 */
public abstract class SocialAccountService implements Abonent, Runnable {
    private final Address address = new Address();
    protected final MessageSystem messageSystem;

    public SocialAccountService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
    }

    public abstract UserProfile addUser(UserProfile userProfile);

    public abstract UserProfile getUserById(String id);

    public abstract void removeUser(String id);

    public abstract long getUserCount();

    public abstract void clear();

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(MessageSystem.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
