package org.pieszku.messaging.nats;

import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.controller.MessengerController;
import org.pieszku.messaging.api.entity.User;
import org.pieszku.messaging.api.injector.type.MessengerPacketInjectorType;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;
import org.pieszku.messaging.api.packet.testing.TestPacket;
import org.pieszku.messaging.api.packet.testing.TestPacketResponse;

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

        this.messengerConnection.sendRequestPacket("messenger_request", new TestPacket("test a message", new User()), new MessengerPacketResponse<TestPacketResponse>() {


                    @Override
                    public void done(TestPacketResponse packetResponse) {
                        System.out.println(packetResponse.getText());
                    }

                    @Override
                    public void error(String errorMessage) {
                        System.out.println(errorMessage);
                    }
                });
    }

    public static void main(String[] args) {
        new NatsMessenger("0.0.0.0", 4222,"", "org.pieszku.messaging.nats.handler","messenger_response", "messenger_request");
    }

    public NatsMessengerConnection getMessengerConnection() {
        return this.messengerConnection;
    }
}
