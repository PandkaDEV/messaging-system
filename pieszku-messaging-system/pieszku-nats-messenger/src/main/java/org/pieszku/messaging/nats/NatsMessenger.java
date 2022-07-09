package org.pieszku.messaging.nats;

import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.controller.MessengerController;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;

public class NatsMessenger extends MessengerController<NatsMessengerConnection> {


    private static NatsMessenger instance;
    private final NatsMessengerConnection messengerConnection;

    public static NatsMessenger getInstance() {
        return instance;
    }

    public NatsMessenger(String hostName, int port, String password, String handlersPackageName, String... handlerChannels) {
        super(MessengerPacketInjectorType.NATS);
        instance = this;

        this.messengerConnection = new NatsMessengerConnection(hostName, port, password, handlerChannels, handlersPackageName);
        this.messengerConnection.setConnectionState(MessengerConnectionState.TRYING);

        NatsMessengerDependencyInjector natsMessengerDependencyInjector = new NatsMessengerDependencyInjector(handlersPackageName);
        natsMessengerDependencyInjector.initialize(this.messengerConnection);
    }
    public NatsMessengerConnection getMessengerConnection() {
        return this.messengerConnection;
    }
}
