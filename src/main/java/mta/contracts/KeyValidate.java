package mta.contracts;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.Objects;

public class KeyValidate implements Serializable {

    public KeyValidate(String nameServer, PublicKey publicKey, ZonedDateTime dateExpired) {
        this.nameServer = nameServer;
        this.publicKey = publicKey;
        this.dateExpired = dateExpired;
    }

    private String nameServer;
    private PublicKey publicKey;
    private ZonedDateTime dateExpired;

    @Override
    public String toString() {
        return "KeyValidate{" +
                "nameServer='" + nameServer + '\'' +
                ", publicKey=" + publicKey +
                ", dateExpired=" + dateExpired +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyValidate)) return false;
        KeyValidate that = (KeyValidate) o;
        return Objects.equals(nameServer, that.nameServer) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(dateExpired, that.dateExpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameServer, publicKey, dateExpired);
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public ZonedDateTime getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(ZonedDateTime dateExpired) {
        this.dateExpired = dateExpired;
    }

    public KeyValidate() {
    }
}
