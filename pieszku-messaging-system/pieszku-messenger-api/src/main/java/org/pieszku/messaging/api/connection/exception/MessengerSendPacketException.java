package org.pieszku.messaging.api.connection.exception;

public class MessengerSendPacketException extends Exception{

    private final String message;

    public MessengerSendPacketException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
