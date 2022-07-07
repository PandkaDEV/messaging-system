package org.pieszku.messaging.nats;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.injector.MessengerPacketHandlerDependencyInjector;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;

import java.util.Optional;

public class NatsMessengerDependencyInjector extends MessengerPacketHandlerDependencyInjector<NatsMessengerConnection> {


    public NatsMessengerDependencyInjector(String handlersPackageName){
        super(handlersPackageName);
    }

    @Override
    public void initialize(NatsMessengerConnection messengerConnection) {
        messengerConnection.getConnection().createDispatcher().subscribe("messenger_response", message -> {
            MessengerPacket packet = NatsMessengerPacketSerializer.deserialize(message.getData());

            if(packet instanceof MessengerPacketRequest){
                MessengerPacketRequest packetRequest = (MessengerPacketRequest) packet;

                Optional<MessengerPacketResponse> callbackPacketOptional = NatsMessenger.getInstance().getCallbackCache().findRequestCallbackPacket(packetRequest.getCallbackId());
                if (!callbackPacketOptional.isPresent()) return;

                callbackPacketOptional.ifPresent(callbackPacket -> {

                    if (!packetRequest.isResponse()) {
                        callbackPacket.error(packetRequest.getErrorMessage());
                        return;
                    }
                    callbackPacket.done(packetRequest);
                    NatsMessenger.getInstance().getCallbackCache().applyCallback(packetRequest.getCallbackId());
                });
            }
        });
        this.getReflections().getSubTypesOf(MessengerPacketRequestHandler.class).forEach(messengerHandlerInstance -> {
            try {
                NatsMessengerCallbackHandler requestHandler = new NatsMessengerCallbackHandler(messengerHandlerInstance.newInstance());
                messengerConnection.getConnection().createDispatcher().subscribe(requestHandler.getHandlerInfo().listenChannelName(), requestHandler);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
