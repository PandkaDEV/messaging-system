package org.pieszku.messaging.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;
import org.pieszku.messaging.api.connection.MessengerConnection;
import org.pieszku.messaging.api.handler.type.MessengerPacketHandlerInfo;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;

import java.util.Optional;

public class RedisMessengerCallbackHandler implements RedisPubSubListener<String, MessengerPacket> {


    private final MessengerPacketRequestHandler instanceRequestClass;
    private final RedisMessengerConnection messengerConnection = RedisMessenger.getInstance().getMessengerConnection();

    public MessengerPacketHandlerInfo getHandlerInfo() {
        MessengerPacketHandlerInfo messengerPacketHandlerInfo = null;
        try {
            messengerPacketHandlerInfo = this.instanceRequestClass.getClass().getMethod("handle", MessengerPacketRequest.class, long.class, MessengerConnection.class).getAnnotation(MessengerPacketHandlerInfo.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return messengerPacketHandlerInfo;
    }

    public RedisMessengerCallbackHandler(MessengerPacketRequestHandler instanceRequestClass) {
        this.instanceRequestClass = instanceRequestClass;
    }

    @Override
    public void message(String channel, MessengerPacket messagePacket) {
        if (!channel.equalsIgnoreCase(this.getHandlerInfo().listenChannelName())) return;
        if (messagePacket instanceof MessengerPacketRequest) {
            MessengerPacketRequest packetRequest = (MessengerPacketRequest) messagePacket;
            this.instanceRequestClass.handle(packetRequest, packetRequest.getCallbackId(), this.messengerConnection);
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
}
