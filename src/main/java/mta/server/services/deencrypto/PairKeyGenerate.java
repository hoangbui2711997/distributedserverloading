package mta.server.services.deencrypto;

import java.security.*;

/**
 * Each client have only one private-public key
 *
 */
public class PairKeyGenerate {
    public static PrivateKey PRIVATE_KEY;
    public static PublicKey PUBLIC_KEY;
    private static KeyPairGenerator keyPairGenerator;

    static {
        try {
            resetKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void resetKey() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048 * 2);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PRIVATE_KEY = keyPair.getPrivate();
        PUBLIC_KEY = keyPair.getPublic();
    }

    public static void setPublicKey(PublicKey publicKey) {
        PUBLIC_KEY = publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static void setPrivateKey(PrivateKey privateKey) {
        PRIVATE_KEY = privateKey;
    }

    public static PublicKey getPublicKey() {
        return PUBLIC_KEY;
    }
}
