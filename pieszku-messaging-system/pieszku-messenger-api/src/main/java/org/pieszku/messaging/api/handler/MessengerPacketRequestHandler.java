package org.pieszku.messaging.api.handler;

import org.pieszku.messaging.api.connection.MessengerConnection;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;

import java.io.Serializable;

public interface MessengerPacketRequestHandler<T extends MessengerPacketRequest> extends Serializable {

    void handle(T packet, long callbackId, MessengerConnection messengerConnection);
}
