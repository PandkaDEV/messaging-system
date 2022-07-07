package org.pieszku.messaging.api.packet.testing;

import org.pieszku.messaging.api.packet.MessengerPacketRequest;

public class TestPacketResponse extends MessengerPacketRequest {

    private final String text;

    public TestPacketResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
