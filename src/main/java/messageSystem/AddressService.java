package messageSystem;


import auth.SocialAccountService;
import frontend.FrontendService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public final class AddressService {

    private List<Address> accountServiceList = new ArrayList<>();
    private List<Address> frontendServiceList = new ArrayList<>();

    private AtomicInteger accountServiceCounter = new AtomicInteger();
    private AtomicInteger frontendServiceCounter = new AtomicInteger();

    public void registerFrontendService(FrontendService frontendService) {
        frontendServiceList.add(frontendService.getAddress());
    }

    public void registerAccountService(SocialAccountService accountService) {
        accountServiceList.add(accountService.getAddress());
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement();
        if (index >= accountServiceList.size()) {
            index = 0;
        }
        return accountServiceList.get(index);
    }

    public synchronized Address getFrontendServiceAddress() {
        int index = frontendServiceCounter.getAndIncrement();
        if (index >= frontendServiceList.size()) {
            index = 0;
        }
        return frontendServiceList.get(index);
    }
}
