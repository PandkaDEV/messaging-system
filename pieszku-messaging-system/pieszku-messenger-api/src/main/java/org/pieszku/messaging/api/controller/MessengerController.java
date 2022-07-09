package org.pieszku.messaging.api.controller;

import org.nustaq.serialization.FSTConfiguration;
import org.pieszku.messaging.api.Messenger;
import org.pieszku.messaging.api.callback.CallbackCache;
import org.pieszku.messaging.api.injector.MessengerPacketInjector;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;

public abstract class MessengerController<T extends Messenger> extends MessengerPacketInjector<T> {

    private final CallbackCache callbackCache = new CallbackCache();

    public MessengerController(MessengerPacketInjectorType packetInjectorType) {
        super(packetInjectorType);
    }

    public CallbackCache getCallbackCache() {
        return callbackCache;
    }

}
