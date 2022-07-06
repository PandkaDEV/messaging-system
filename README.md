### NATS

Example connection:

```java
        this.messengerConnection = new NatsMessengerConnection("0.0.0.0", 4222, "",
                new String[]{"test_messenger_response", "test_messenger_request"},
                "org.pieszku.messaging.nats.handler");
        this.messengerConnection.setConnectionState(MessengerConnectionState.TRYING);

        NatsMessengerDependencyInjector natsMessengerDependencyInjector = new NatsMessengerDependencyInjector(this);
        natsMessengerDependencyInjector.initialize();
```
