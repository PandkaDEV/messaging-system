package org.pieszku.messaging.api.packet;

import java.util.concurrent.ThreadLocalRandom;

public class MessengerPacketRequest implements MessengerPacket<MessengerPacketRequest>{

    private long callbackId = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    private boolean response, done;
    private String errorMessage;

    public long getCallbackId() {
        return callbackId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isResponse() {
        return response;
    }

    public void setCallbackId(long callbackId) {
        this.callbackId = callbackId;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "MessengerPacketRequest{" +
                "callbackId=" + callbackId +
                ", response=" + response +
                ", done=" + done +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
