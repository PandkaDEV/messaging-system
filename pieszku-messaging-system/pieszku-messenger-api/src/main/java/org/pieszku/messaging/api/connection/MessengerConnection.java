package org.pieszku.messaging.api.connection;

import org.pieszku.messaging.api.Messenger;
import org.pieszku.messaging.api.connection.state.MessengerConnectionState;
import org.pieszku.messaging.api.connection.exception.MessengerConnectionException;

public abstract class MessengerConnection implements Messenger {


    private final String host;
    private final int port;
    private final String password;
    private final String handlersPackageName;
    private MessengerConnectionState connectionState;

    public MessengerConnection(String host, int port, String password, String handlersPackageName) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.handlersPackageName = handlersPackageName;
        this.connectionState = MessengerConnectionState.CLOSE;
    }

    public MessengerConnectionState getConnectionState() {
        return connectionState;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getHandlersPackageName() {
        return handlersPackageName;
    }

    @Override
    public void setConnectionState(MessengerConnectionState connectionState) {
        this.connectionState = connectionState;

        try {
        switch (connectionState){
            case TRYING: {
                this.connect(this.host, this.password, this.port);
                connectionState = MessengerConnectionState.CONNECTED;
                this.setConnectionState(connectionState);
                break;
            }
            case CONNECTED:{
                System.out.println("MessengerClient successfully connected to host: " + this.host);
                break;
            }
        }
        } catch (MessengerConnectionException messengerConnectionException) {
            messengerConnectionException.printStackTrace();;
            this.setConnectionState(MessengerConnectionState.TRYING);
        }
    }
}
