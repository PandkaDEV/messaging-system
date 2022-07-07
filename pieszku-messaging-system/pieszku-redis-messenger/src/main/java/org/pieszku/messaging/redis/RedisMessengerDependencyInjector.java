package org.pieszku.messaging.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.injector.MessengerPacketHandlerDependencyInjector;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;

import java.util.Optional;

public class RedisMessengerDependencyInjector extends MessengerPacketHandlerDependencyInjector<RedisMessengerConnection> {

    public RedisMessengerDependencyInjector(String handlerPackageName) {
        super(handlerPackageName);
    }

    @Override
    public void initialize(RedisMessengerConnection messengerConnection) {

        this.getReflections().getSubTypesOf(MessengerPacketRequestHandler.class).forEach(messengerHandlerInstance -> {
            try {
                RedisMessengerCallbackHandler requestHandler = new RedisMessengerCallbackHandler(messengerHandlerInstance.newInstance());
                messengerConnection.getPubSubConnection().addListener(requestHandler);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });


        messengerConnection.getPubSubConnection().addListener(new RedisPubSubListener<String, MessengerPacket>() {
            @Override
            public void message(String channel, MessengerPacket messagePacket) {
                if (messagePacket instanceof MessengerPacketRequest) {
                    MessengerPacketRequest packetRequest = (MessengerPacketRequest) messagePacket;


                    Optional<MessengerPacketResponse> callbackPacketOptional = RedisMessenger.getInstance().getCallbackCache().findRequestCallbackPacket(packetRequest.getCallbackId());
                    if (!callbackPacketOptional.isPresent()) return;

                    callbackPacketOptional.ifPresent(callbackPacket -> {

                        if (!packetRequest.isResponse()) {
                            callbackPacket.error(packetRequest.getErrorMessage());
                            return;
                        }
                        if (!packetRequest.isDone()) return;

                        callbackPacket.done(packetRequest);
                        RedisMessenger.getInstance().getCallbackCache().applyCallback(packetRequest.getCallbackId());
                    });
                }
            }


            @Override
            public void message(String pattern, String channel, MessengerPacket message) {

            }

            @Override
            public void subscribed(String channel, long count) {

            }

            @Override
            public void psubscribed(String pattern, long count) {

            }

            @Override
            public void unsubscribed(String channel, long count) {

            }

            @Override
            public void punsubscribed(String pattern, long count) {

            }

        });
    }
}
