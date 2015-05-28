package service;

import utils.Bundle;

/**
 * nickolay, 28.05.15.
 */
public abstract class Request {
    private boolean response = false;
    private String method;
    private Bundle args;
    private long created;

    public Request(String method, Bundle args) {
        this.method = method;
        this.args = args;

        created = System.currentTimeMillis();
    }

    public Request(String method, Bundle args, boolean response) {
        this(method, args);
        this.response = response;
    }

    public String getMethod() {
        return method;
    }

    public Bundle getArgs() {
        return args;
    }

    public boolean withResponse() {
        return response;
    }

    public long getCreationTime() {
        return created;
    }
}