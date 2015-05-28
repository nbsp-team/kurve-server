package service;

import utils.Bundle;

/**
 * nickolay, 28.05.15.
 */
public class Response {
    public static final int SUCCESS = 0;

    private int error;
    private Bundle args;

    public Response(Bundle args) {
        this.args = args;
    }

    public Response(int error, Bundle args) {
        this.error = error;
        this.args = args;
    }

    public boolean isError() {
        return error != SUCCESS;
    }

    public Bundle getArgs() {
        return args;
    }
}