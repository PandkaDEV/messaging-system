package org.pieszku.messaging.api.handler.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessengerPacketHandlerInfo {


    String listenChannelName();
    Class<?> packetClass();
}
