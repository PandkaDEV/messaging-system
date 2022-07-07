package org.pieszku.messaging.api.callback;

import org.pieszku.messaging.api.packet.MessengerPacketResponse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackCache {

    private final Map<Long, MessengerPacketResponse> callbackCache = new ConcurrentHashMap<>();

    public void add(long callbackId, MessengerPacketResponse requestPacket){
        this.callbackCache.put(callbackId, requestPacket);
    }
    public void applyCallback(long callbackId){
        this.callbackCache.remove(callbackId);
    }
    public Optional<MessengerPacketResponse> findRequestCallbackPacket(long callbackId){
        return Optional.ofNullable(this.callbackCache.get(callbackId));
    }

    public Map<Long, MessengerPacketResponse> getCallbackCache() {
        return callbackCache;
    }
}
