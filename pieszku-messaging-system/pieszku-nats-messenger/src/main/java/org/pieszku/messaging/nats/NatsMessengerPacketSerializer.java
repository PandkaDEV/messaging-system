package org.pieszku.messaging.nats;

import org.nustaq.serialization.FSTConfiguration;
import org.pieszku.messaging.api.packet.MessengerPacket;

public class NatsMessengerPacketSerializer {

    private static final FSTConfiguration FST_CONFIGURATION = NatsMessenger.getInstance().getFstConfiguration();

    public static MessengerPacket deserialize(byte[] input) {
        return (MessengerPacket) FST_CONFIGURATION.asObject(input);
    }

    public static byte[] serialize(MessengerPacket packet) {
        return FST_CONFIGURATION.asByteArray(packet);
    }
}
