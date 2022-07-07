package org.pieszku.messaging.api.packet.testing;

import org.pieszku.messaging.api.entity.User;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;

public class TestPacket extends MessengerPacketRequest {

    private final String message;
    private final User user;


    public TestPacket(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "TestPacket{" +
                "message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
