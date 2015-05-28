package service;

/**
 * nickolay, 28.05.15.
 */
public abstract class Task {
    private final Address from;
    private final Address to;

    public Task(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(Service service);
}