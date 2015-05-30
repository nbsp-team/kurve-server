package frontend;

import auth.messages.SocialSignInMessage;
import com.sun.org.apache.xpath.internal.SourceTree;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import model.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nickolay, 29.05.15.
 */
public class FrontendService implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private Map<Message, UserResponse> userResponses = new ConcurrentHashMap<>();

    public FrontendService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;

        messageSystem.addService(this);
        messageSystem.getAddressService().registerFrontendService(this);
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

    public void onAuth(UserProfile user, Message request) {
        System.out.println("OnAuth, request: " + request.toString());
        if (userResponses.containsKey(request)) {
            System.out.println("Setting user to request: " + request.toString());
            userResponses.get(request).setUser(user);
        }
    }

    public UserProfile addUser(UserProfile user) {
        Message request = new SocialSignInMessage(getAddress(), messageSystem.getAddressService().getAccountServiceAddress(), user);
        messageSystem.sendMessage(request);

        UserResponse response = new UserResponse();
        userResponses.put(request, response);

        System.out.println("Putted request to hashmap: " + request.toString());

        while(response.isEmpty()) {}

        UserProfile responseUser = response.getUser();
        userResponses.remove(request);
        return responseUser;
    }
}
