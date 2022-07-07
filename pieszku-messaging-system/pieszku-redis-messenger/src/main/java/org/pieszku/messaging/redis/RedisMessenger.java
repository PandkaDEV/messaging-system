package org.pieszku.messaging.redis;

import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.controller.MessengerController;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;

public class RedisMessenger extends MessengerController {


    private static RedisMessenger instance;
    private final RedisMessengerConnection messengerConnection;

    public static RedisMessenger getInstance() {
        return instance;
    }
    public RedisMessenger(String hostName, int port, String password,String handlersPackageName, String... handlerChannels) {
        super(MessengerPacketInjectorType.REDIS);
        instance = this;

        this.messengerConnection = new RedisMessengerConnection(hostName, port, password, handlerChannels, handlersPackageName);
        this.messengerConnection.setConnectionState(MessengerConnectionState.TRYING);

        RedisMessengerDependencyInjector redisMessengerDependencyInjector = new RedisMessengerDependencyInjector(handlersPackageName);
        redisMessengerDependencyInjector.initialize(this.messengerConnection);

    }

    public RedisMessengerConnection getMessengerConnection() {
        return messengerConnection;
    }
}
