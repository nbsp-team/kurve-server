package service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * nickolay, 28.05.15.
 */
public final class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final int id;

    public Address() {
        id = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public int hashCode() {
        return id;
    }
}