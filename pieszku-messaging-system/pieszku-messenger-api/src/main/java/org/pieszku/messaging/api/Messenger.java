package org.pieszku.messaging.api;

import org.pieszku.messaging.api.connection.exception.MessengerConnectionException;
import org.pieszku.messaging.api.connection.exception.MessengerSendPacketException;
import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.packet.MessengerPacket;
import org.pieszku.messaging.api.packet.MessengerPacketRequest;
import org.pieszku.messaging.api.packet.MessengerPacketResponse;

public interface Messenger {


     void setConnectionState(MessengerConnectionState connectionState);

    void connect(String host, String password, int port) throws MessengerConnectionException;

    void disconnect();

    <T extends MessengerPacket> void sendPacket(String channelName, T packet) throws MessengerSendPacketException;

    <T extends MessengerPacketRequest> void sendRequestPacket(String channelName, MessengerPacketRequest packet, MessengerPacketResponse<T> packetResponse) throws MessengerSendPacketException;

    void reply(MessengerPacket replyPacket);
}
