package org.pieszku.messaging.nats;

import io.nats.client.Connection;
import io.nats.client.Options;
import org.pieszku.messaging.api.connection.MessengerConnection;
import org.pieszku.messaging.api.connection.exception.MessengerConnectionException;
import org.pieszku.messaging.api.connection.exception.MessengerSendPacketException;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;
import org.pieszku.messaging.api.packet.MessengerPacketSerializer;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class NatsMessengerConnection extends MessengerConnection {

    private Connection connection;


    public NatsMessengerConnection(String host, int port, String password, String[] channelListenName, String packageName) {
        super(host, port, password, packageName);
        try {
            this.initialize(host, password, port, channelListenName);
        } catch (MessengerConnectionException e) {
            e.printStackTrace();
        }
    }
    public void initialize(String host, String password, int port, String[] channelListenName)throws MessengerConnectionException{
        this.connect(host, password, port);

        for (String channelName : channelListenName) {
            this.connection.subscribe(channelName);
        }
    }

    @Override
    public void connect(String host, String password, int port) throws MessengerConnectionException {
        try {
            Options.Builder optionsBuilder = new Options.Builder().server("nats://" + host + ":" + port)
                    .reconnectWait(Duration.ofSeconds(10L)).maxReconnects(1000)
                    .connectionTimeout(Duration.ofSeconds(2L));
            this.connection = io.nats.client.Nats.connect(optionsBuilder.build());

        } catch (IOException | InterruptedException e) {
            throw new MessengerConnectionException("NatsClient host not response.");
        }
    }

    @Override
    public void disconnect() {
        try {
            this.connection.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends MessengerPacket> void sendPacket(String channelName, T packet)  {
        try {
            this.connection.publish(channelName, MessengerPacketSerializer.serialize(packet));
        } catch (Exception e) {
            try {
                throw new MessengerSendPacketException("NatsClient problem sent packet: " + e.getMessage());
            } catch (MessengerSendPacketException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public <T extends MessengerPacketRequest> CompletableFuture<T> sendRequestPacket(String channelName, MessengerPacketRequest packet, MessengerPacketResponse<T> packetResponse) {
        NatsMessenger.getInstance().getCallbackCache().add(packet.getCallbackId(), packetResponse);
        return this.connection.requestWithTimeout(channelName, MessengerPacketSerializer.serialize(packet),
                        Duration.ofMinutes(2L))
                .thenApply(message -> (T) MessengerPacketSerializer.deserialize(message.getData()))
                .whenComplete((data, throwable) -> {
                    if (throwable == null && data != null && data.getErrorMessage() != null) {
                        try {
                            throw new MessengerSendPacketException("NatsClient problem sent packet request: " + data.getErrorMessage());
                        } catch (MessengerSendPacketException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void reply(MessengerPacket replyPacket) {
        this.sendPacket("messenger_response", replyPacket);
    }

    public Connection getConnection() {
        return connection;
    }

}
