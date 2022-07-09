package org.pieszku.messaging.nats;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.handler.type.MessengerPacketHandlerInfo;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketSerializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NatsMessengerCallbackHandler implements MessageHandler {

    private final MessengerPacketRequestHandler instanceRequestClass;
    private final NatsMessengerConnection messengerConnection = NatsMessenger.getInstance().getMessengerConnection();

    public NatsMessengerCallbackHandler(MessengerPacketRequestHandler instanceRequestClass) {
        this.instanceRequestClass = instanceRequestClass;
    }

    public List<MessengerPacketHandlerInfo> getHandlerInfoList() {
        List<MessengerPacketHandlerInfo> messengerPacketHandlerInfoList = new ArrayList<>();
        for (Method method : this.instanceRequestClass.getClass().getMethods()) {
            if(method.getAnnotation(MessengerPacketHandlerInfo.class) == null) continue;
            messengerPacketHandlerInfoList.add(method.getAnnotation(MessengerPacketHandlerInfo.class));
        }
        return messengerPacketHandlerInfoList;
    }

    @Override
    public void onMessage(Message message) {
        MessengerPacket packet = MessengerPacketSerializer.deserialize(message.getData());

        if (packet instanceof MessengerPacketRequest) {
            MessengerPacketRequest packetRequest = (MessengerPacketRequest) packet;

            this.findHandlerInfoByClass(packet).ifPresent(messengerPacketHandlerInfo -> {
                try {
                    Method method = null;
                    for (Method declaredMethod : this.instanceRequestClass.getClass().getDeclaredMethods()) {
                        if(declaredMethod.getAnnotation(MessengerPacketHandlerInfo.class) == null) continue;
                        if(!messengerPacketHandlerInfo.equals(declaredMethod.getAnnotation(MessengerPacketHandlerInfo.class))) continue;
                        if(!message.getSubject().equalsIgnoreCase(messengerPacketHandlerInfo.listenChannelName())) continue;
                        method = declaredMethod;
                    }
                    method.invoke(this.instanceRequestClass, packet, packetRequest.getCallbackId(),this.messengerConnection);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public Optional<MessengerPacketHandlerInfo> findHandlerInfoByClass(MessengerPacket packetRequest){
        return this.getHandlerInfoList()
                .stream()
                .filter(messengerPacketHandlerInfo -> packetRequest.getClass().isAssignableFrom(messengerPacketHandlerInfo.packetClass()))
                .findFirst();
    }
}
