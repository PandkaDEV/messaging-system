package org.pieszku.messaging.nats;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.pieszku.messaging.api.connection.MessengerConnection;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.handler.type.MessengerPacketHandlerInfo;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;

import java.util.Optional;

public class NatsMessengerCallbackHandler implements MessageHandler {

    private final MessengerPacketRequestHandler instanceRequestClass;
    private final NatsMessengerConnection messengerConnection = NatsMessenger.getInstance().getMessengerConnection();

    public NatsMessengerCallbackHandler(MessengerPacketRequestHandler instanceRequestClass) {
        this.instanceRequestClass = instanceRequestClass;
    }

    public MessengerPacketHandlerInfo getHandlerInfo() {
        MessengerPacketHandlerInfo messengerPacketHandlerInfo = null;
        try {
            messengerPacketHandlerInfo = this.instanceRequestClass.getClass().getMethod("handle", MessengerPacketRequest.class, long.class, MessengerConnection.class).getAnnotation(MessengerPacketHandlerInfo.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return messengerPacketHandlerInfo;
    }

    @Override
    public void onMessage(Message message) {
        MessengerPacket packet = NatsMessengerPacketSerializer.deserialize(message.getData());

        if (packet instanceof MessengerPacketRequest) {
            MessengerPacketRequest packetRequest = (MessengerPacketRequest) packet;
            this.instanceRequestClass.handle(packetRequest, packetRequest.getCallbackId(), this.messengerConnection);
        }
    }
}
