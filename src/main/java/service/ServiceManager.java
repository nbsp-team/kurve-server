package service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * nickolay, 28.05.15.
 */
public class ServiceManager {
    public static ServiceManager instance = new ServiceManager();

    public static final int SERVICE_SLEEP_TIME = 100;
    private static final Random random = new Random();

    private final Map<Address, Service> services = new ConcurrentHashMap<>();
    private final Map<ServiceType, List<Address>> addresses = new ConcurrentHashMap<>();

    private ServiceManager() {
        for(ServiceType type : ServiceType.values()) {
            addresses.put(type, new CopyOnWriteArrayList<>());
        }
    }

    public static ServiceManager getInstance() {
        return instance;
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

    public void addTask(Task task) {
        services.get(task.getTo()).addTask(task);
    }
}
