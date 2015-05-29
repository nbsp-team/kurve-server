package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * nickolay, 28.05.15.
 */
public abstract class Service implements Runnable {
    public abstract Address getAddress();

    protected ServiceManager serviceManager;

    private ConcurrentLinkedQueue<Request> queue = new ConcurrentLinkedQueue<>();
    private Map<Request, ResponseListener> listeners = new ConcurrentHashMap<>();
    private Thread serviceThread;

    protected void addResponse(Request request, Response response) {
        serviceManager.addResponse(request, response);
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void addRequest(Request request) {
        queue.add(request);
    }

    public void setServiceThread(Thread serviceThread) {
        this.serviceThread = serviceThread;
    }

    @Override
    public void run() {
        if (serviceManager == null) {
            throw new RuntimeException("Service not registered in ServiceManager");
        }

        while (true){
            while (!queue.isEmpty()) {
                Request request = queue.poll();
                Response response = processRequest(request);

                if (response != null) {
                    serviceManager.addResponse(request, response);
                }
            }

            for(Request request : listeners.keySet()) {
                Response response = serviceManager.getResponse(request);
                if (response != null) {
                    listeners.get(request).onResponse(response);
                }
            }

            try {
                Thread.sleep(ServiceManager.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract Response processRequest(Request request);

    public void start() {
        if (serviceThread != null) {
            serviceThread.start();
        } else {
            throw new RuntimeException("Service not registered in ServiceManager");
        }
    }

    public void addListener(Request request, ResponseListener listener) {
        listeners.put(request, listener);
    }
}
