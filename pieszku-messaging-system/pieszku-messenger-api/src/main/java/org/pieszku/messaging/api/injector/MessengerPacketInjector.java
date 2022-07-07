package org.pieszku.messaging.api.injector;

import org.pieszku.messaging.api.Messenger;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class MessengerPacketInjector<T extends Messenger> {


    private final MessengerPacketInjectorType packetInjectorType;

    public MessengerPacketInjector(MessengerPacketInjectorType packetInjectorType) {
        this.packetInjectorType = packetInjectorType;
    }
    public MessengerPacketInjectorType getPacketInjectorType() {
        return packetInjectorType;
    }

}
