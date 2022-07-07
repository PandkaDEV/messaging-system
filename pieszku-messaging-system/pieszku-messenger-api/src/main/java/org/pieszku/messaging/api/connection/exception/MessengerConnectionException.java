package org.pieszku.messaging.api.connection.exception;

public class MessengerConnectionException extends Exception {

    private final String message;

    public MessengerConnectionException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
