package org.pieszku.messaging.api.packet;


public interface MessengerPacketResponse<T> {


    void done(T packetResponse);
    void error(String errorMessage);

}
