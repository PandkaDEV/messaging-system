package org.pieszku.messaging.redis;

import io.lettuce.core.codec.RedisCodec;
import org.pieszku.messaging.api.packet.MessengerPacket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RedisPacketSerializerCodec implements RedisCodec<String, MessengerPacket> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return this.charset.decode(bytes).toString();
    }

    @Override
    public MessengerPacket decodeValue(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return (MessengerPacket) RedisMessenger.getInstance().getFstConfiguration().asObject(bytes);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return this.charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(MessengerPacket value) {
        return ByteBuffer.wrap(RedisMessenger.getInstance().getFstConfiguration().asByteArray(value));
    }
}