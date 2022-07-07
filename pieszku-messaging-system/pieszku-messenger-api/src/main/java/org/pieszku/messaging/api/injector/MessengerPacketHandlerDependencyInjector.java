package org.pieszku.messaging.api.injector;

import org.pieszku.messaging.api.connection.MessengerConnection;
import org.reflections.Reflections;

public abstract class MessengerPacketHandlerDependencyInjector<T extends MessengerConnection> {

    private final Reflections reflections;

    public abstract void initialize(T messengerConnection);

    public MessengerPacketHandlerDependencyInjector(String handlersPackageName) {
        this.reflections = new Reflections(handlersPackageName);
    }

    public Reflections getReflections() {
        return reflections;
    }
    
}
