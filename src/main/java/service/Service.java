package service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * nickolay, 28.05.15.
 */
public abstract class Service implements Runnable {
    public abstract Address getAddress();
    protected ServiceManager serviceManager;

    private ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();
    private Thread serviceThread;

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void addTask(Task task) {
        queue.add(task);
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
            execTasks();

            try {
                Thread.sleep(ServiceManager.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void execTasks() {
        while (!queue.isEmpty()) {
            Task task = queue.poll();
            try {
                task.exec(this);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (serviceThread != null) {
            serviceThread.start();
        } else {
            throw new RuntimeException("Service not registered in ServiceManager");
        }
    }
}
