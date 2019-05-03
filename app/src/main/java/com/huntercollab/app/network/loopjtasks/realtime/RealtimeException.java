package com.huntercollab.app.network.loopjtasks.realtime;

public class RealtimeException extends Exception {

    private final String message;

    public RealtimeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
