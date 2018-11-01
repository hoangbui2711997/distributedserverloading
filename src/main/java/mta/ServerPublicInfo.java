package mta;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class ServerPublicInfo implements Serializable {
    private final String serverName;
    private final ZonedDateTime dateStartServer;
    private final ZonedDateTime dateResetKeyPair;
    private final int validateTime;

    public ServerPublicInfo() {
        serverName = UUID.randomUUID().toString();
        dateStartServer = ZonedDateTime.now();
        dateResetKeyPair = ZonedDateTime.now();
        validateTime = 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerPublicInfo)) return false;
        ServerPublicInfo that = (ServerPublicInfo) o;
        return validateTime == that.validateTime &&
                Objects.equals(serverName, that.serverName) &&
                Objects.equals(dateStartServer, that.dateStartServer) &&
                Objects.equals(dateResetKeyPair, that.dateResetKeyPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, dateStartServer, dateResetKeyPair, validateTime);
    }

    @Override
    public String toString() {
        return "ServerPublicInfo{" +
                "serverName='" + serverName + '\'' +
                ", dateStartServer=" + dateStartServer +
                ", dateResetKeyPair=" + dateResetKeyPair +
                ", validateTime=" + validateTime +
                '}';
    }

    public String getServerName() {
        return serverName;
    }

    public ZonedDateTime getDateStartServer() {
        return dateStartServer;
    }

    public ZonedDateTime getDateResetKeyPair() {
        return dateResetKeyPair;
    }

    public int getValidateTime() {
        return validateTime;
    }
}
