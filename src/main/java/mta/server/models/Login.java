package mta.server.models;

import java.io.Serializable;
import java.util.Objects;

public class Login implements Serializable {
    public static final long serialVersionUID=1;

    protected String username;
    protected String password;
    protected String email;
    /**
     * certified is public key
     */
//    protected PublicKey certified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;
        Login login = (Login) o;
        return Objects.equals(username, login.username) &&
                Objects.equals(password, login.password) &&
                Objects.equals(email, login.email);
//                &&
//                Objects.equals(certified, login.certified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }

//    public PublicKey getCertified() {
//        return certified;
//    }
//
//    public void setCertified(PublicKey certified) {
//        this.certified = certified;
//    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
//                ", certified=" + certified +
                '}';
    }

    public Login(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
//        this.certified = certified;
    }

    public Login() {
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
