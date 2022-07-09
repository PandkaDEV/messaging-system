package org.pieszku.messaging.api.packet;

import org.nustaq.serialization.FSTConfiguration;

public class MessengerPacketSerializer {

    private static final FSTConfiguration FST_CONFIGURATION = FSTConfiguration.createDefaultConfiguration();

    public static MessengerPacket deserialize(byte[] input) {
        return (MessengerPacket) FST_CONFIGURATION.asObject(input);
    }

    public static byte[] serialize(MessengerPacket packet) {
        return FST_CONFIGURATION.asByteArray(packet);
    }
}
