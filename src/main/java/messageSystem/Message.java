package messageSystem;


public abstract class Message {
    private final Address to;
    private final Address from;

    public Message(Address from, Address to) {
        this.to = to;
        this.from = from;
    }

    public Address getTo() {
        return to;
    }

    public Address getFrom() {
        return from;
    }

    public abstract void exec(Abonent abonent);
}
