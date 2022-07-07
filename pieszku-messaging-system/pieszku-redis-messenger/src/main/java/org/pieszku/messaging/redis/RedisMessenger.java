package org.pieszku.messaging.redis;

import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.controller.MessengerController;
import org.pieszku.messaging.api.entity.User;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;
import org.pieszku.messaging.api.packet.testing.TestPacket;
import org.pieszku.messaging.api.packet.testing.TestPacketResponse;

import java.util.concurrent.locks.LockSupport;

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

    public static void main(String[] args) {
        new RedisMessenger("127.0.0.1", 6379, "","org.pieszku.messaging.redis.handler","messenger_response", "messenger_request" );
    }

    public RedisMessengerConnection getMessengerConnection() {
        return messengerConnection;
    }
}
