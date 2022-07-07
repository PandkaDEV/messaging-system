package org.pieszku.messaging.redis;

import com.sun.xml.internal.ws.api.message.Packet;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.pieszku.messaging.api.connection.MessengerConnection;
import org.pieszku.messaging.api.connection.exception.MessengerConnectionException;
import org.pieszku.messaging.api.handler.MessengerPacketRequestHandler;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;
import org.pieszku.messaging.api.packet.testing.TestPacket;
import org.reflections.Reflections;

public class RedisMessengerConnection extends MessengerConnection {

    private RedisClient redisClient;
    private String[] channelListenName;
    private final RedisPacketSerializerCodec packetSerializerCodec =  new RedisPacketSerializerCodec();
    private StatefulRedisPubSubConnection<String, MessengerPacket> pubSubConnection;
    private StatefulRedisConnection<String, MessengerPacket> connection;

    public RedisMessengerConnection(String host, int port, String password, String[] channelListenName, String packageName) {
        super(host, port, password, packageName);

        try {
            this.initialize(channelListenName, packageName);
        } catch (MessengerConnectionException e) {
            e.printStackTrace();
        }
    }

    public void initialize(String[] channelListenName, String packageName) throws MessengerConnectionException{
        try {
            this.connect(this.getHost(), this.getPassword(), this.getPort());
            this.connection = this.redisClient.connect(this.packetSerializerCodec);
            this.channelListenName = channelListenName;
            this.pubSubConnection = this.redisClient.connectPubSub(this.packetSerializerCodec);
            this.pubSubConnection.sync().subscribe(channelListenName);

        } catch (Exception e) {
            throw new MessengerConnectionException("RedisClient host not response.");
        }
    }
    @Override
    public void connect(String host, String password, int port) throws MessengerConnectionException {
        try {
           this.redisClient = RedisClient.create(RedisURI.builder()
                    .withHost(host)
                    .withPassword(password)
                    .withPort(port)
                    .build());
        } catch (Exception e) {
            throw new MessengerConnectionException("RedisClient host not response.");
        }
    }
    @Override
    public void disconnect() {
        this.redisClient.shutdown();
    }

    @Override
    public <T extends MessengerPacket> void sendPacket(String channelName, T packet) {
        this.getConnection().sync().publish(channelName, packet);
    }

    @Override
    public <T extends MessengerPacketRequest> void sendRequestPacket(String channelName, MessengerPacketRequest packet, MessengerPacketResponse<T> packetResponse) {
        packet.setResponse(true);
        RedisMessenger.getInstance().getCallbackCache().add(packet.getCallbackId(), packetResponse);
        this.getConnection().sync().publish(channelName, packet);
    }

    @Override
    public void reply(MessengerPacket replyPacket) {
        this.sendPacket("messenger_response", replyPacket);
    }

    public RedisPacketSerializerCodec getPacketSerializerCodec() {
        return packetSerializerCodec;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public StatefulRedisConnection<String, MessengerPacket> getConnection() {
        return connection;
    }

    public StatefulRedisPubSubConnection<String, MessengerPacket> getPubSubConnection() {
        return pubSubConnection;
    }

    public String[] getChannelListenName() {
        return channelListenName;
    }
}
