package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * nickolay, 28.05.15.
 */
public class ServiceManager {
    public static final Response MISS_RESPONSE = new Response(-1, null);
    public static final int SERVICE_SLEEP_TIME = 40;
    private static final Random random = new Random();

    private final Map<Address, Service> services = new HashMap<>();
    private final Map<ServiceType, List<Address>> addresses = new HashMap<>();
    private final Map<Request, Response> requests = new ConcurrentHashMap<>();

    public ServiceManager() {
        for(ServiceType type : ServiceType.values()) {
            addresses.put(type, new CopyOnWriteArrayList<>());
        }
    }

    public void registerService(Service service, ServiceType serviceType) {
        service.setServiceManager(this);

        services.put(service.getAddress(), service);
        addresses.get(serviceType).add(service.getAddress());

        final Thread serviceThread = new Thread(service);
        serviceThread.setDaemon(true);
        serviceThread.setName(String.format("%s %s", serviceType.name(), service.getAddress()));
        service.setServiceThread(serviceThread);
    }

    public void startAll() {
        for(Service service : services.values()) {
            service.start();
        }
    }

    public Address getServiceAddress(ServiceType serviceType, long session) {
        random.setSeed(session);
        List<Address> serviceAddresses = addresses.get(serviceType);
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }

    public void process(ServiceType type, long session, Request request) {
        if (request.withResponse()) {
            requests.put(request, MISS_RESPONSE);
        }

        Address address = getServiceAddress(type, session);
        services.get(address).addRequest(request);
    }

    public void addResponse(Request request, Response response) {
        requests.put(request, response);
    }

    public Response getResponse(Request request) {
        if (!requests.containsKey(request)) {
            return null;
        }

        Response response = requests.get(request);
        if (response.equals(MISS_RESPONSE)) {
            return null;
        }

        requests.remove(request);

        return response;
    }

    public Response waitResponse(Request request) {
        if (!requests.containsKey(request)) {
            return null;
        }

        while (true) {
            Response response = getResponse(request);
            if (response != null) {
                return response;
            }
        }
    }
}
