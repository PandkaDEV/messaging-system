
### Not a long... SourceCode
### NATS

Example connection:

```java
        this.messengerConnection = new NatsMessengerConnection("0.0.0.0", 4222, "",
                new String[]{"test_messenger_response", "test_messenger_request"},
                "org.pieszku.messaging.nats.handler");
        this.messengerConnection.setConnectionState(MessengerConnectionState.TRYING);
```
Enable handlers receving packets: 
```java
        NatsMessengerDependencyInjector natsMessengerDependencyInjector = new NatsMessengerDependencyInjector(this);
        natsMessengerDependencyInjector.initialize();
```
Example send callback packet: #try/catch is hardly required
```java
        try {
            this.messengerConnection.sendRequestPacket("test_messenger_request", new TestPacket("test a message", new User()),
                    new MessengerPacketResponse<TestPacketResponse>() {

                        @Override
                        public void done(TestPacketResponse packetResponse) {
                            System.out.println(packetResponse.getText());
                        }

                        @Override
                        public void error(String errorMessage) {
                            System.out.println(errorMessage);
                        }
                    });
        } catch (MessengerSendPacketException e) {
            e.printStackTrace();
        }
    }
```
Example handle received packet callback:
```java
public class TestPacketHandler implements MessengerCallbackPacketHandler<TestPacket> {


    @MessengerPacketHandlerInfo(listenChannelName = "test_messenger_request", packetClass = TestPacket.class)
    @Override
    public void handle(TestPacket packet, long callbackId, MessengerConnection messengerConnection) {
        System.out.println(packet.getMessage());

        TestPacketResponse testPacket = new TestPacketResponse();
        testPacket.setResponse(true);
        testPacket.setDone(true);
        testPacket.setCallbackId(callbackId);

        try {
            messengerConnection.reply(testPacket);
        } catch (MessengerSendPacketException e) {
            e.printStackTrace();
        }
    }
}
```
### REDIS
Almost how a Nats
Example connection redis
```java
        this.messengerConnection = new RedisMessengerConnection("127.0.0.1", 6379, "",
                new String[]{"test_messenger_response", "test_messenger_request"},
                "org.pieszku.messaging.redis.handler");
        this.messengerConnection.setConnectionState(MessengerConnectionState.TRYING);
```
Enable handlers receving packets: 
```java
        RedisMessengerDependencyInjector redisMessengerDependencyInjector = new RedisMessengerDependencyInjector(this);
        redisMessengerDependencyInjector.initialize();
```
Example send callback packet: #try/catch is hardly required
```java
        try {
            this.messengerConnection.sendRequestPacket("test_messenger_request", new TestPacket("test a message", new User()),
                    new MessengerPacketResponse<TestPacketResponse>() {

                        @Override
                        public void done(TestPacketResponse packetResponse) {
                            System.out.println(packetResponse.getText());
                        }

                        @Override
                        public void error(String errorMessage) {
                            System.out.println(errorMessage);
                        }
                    });
        } catch (MessengerSendPacketException e) {
            e.printStackTrace();
        }
    }
```
Example handle received packet callback:
```java
public class TestPacketHandler implements MessengerCallbackPacketHandler<TestPacket> {


    @MessengerPacketHandlerInfo(listenChannelName = "test_messenger_request", packetClass = TestPacket.class)
    @Override
    public void handle(TestPacket packet, long callbackId, MessengerConnection messengerConnection) {
        System.out.println(packet.getMessage());

        TestPacketResponse testPacket = new TestPacketResponse();
        testPacket.setResponse(true);
        testPacket.setDone(true);
        testPacket.setCallbackId(callbackId);

        try {
            messengerConnection.sendPacket("test_messenger_request", testPacket);
        } catch (MessengerSendPacketException e) {
            e.printStackTrace();
        }
    }
}
```


