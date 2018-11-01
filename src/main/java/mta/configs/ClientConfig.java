package mta.configs;

import mta.configs.common.PackingDataRequest;
import mta.contracts.REQUEST_TYPE;
import mta.server.models.Login;
import mta.server.models.Student;
import mta.server.services.deencrypto.EncryptService;
import mta.server.services.deencrypto.PairKeyGenerate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Map;

/**
 * Chuc nang cua client
 * sua, lay thong tin cua client
 */
// TODO: 28/10/2018 XU ly packing goi tin tu admin client - tiep theo sau AdminConfig

/**
 * Config for packing request to receive information from mta.server
 */
public class ClientConfig {
    // pack send
    // use static keep state util client logout
    private PackingDataRequest<Student> studentPackingDataRequest = new PackingDataRequest<>();
    private EncryptService encryptService = new EncryptService();
    private static PublicKey publicKeyServer;

    public ClientConfig() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        studentPackingDataRequest.header.put("Role", "client");
    }
    // expect config only one
    public void login(Login login) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        String identity = login.getUsername() + "|" + login.getPassword();
        byte[] authorizedBytes = encryptService.encryptMessage(publicKeyServer, identity.getBytes());
        String authorized = new String(authorizedBytes);
        this.studentPackingDataRequest.header.put("Authorized", authorized);
    }

    /**
     * When logout delete all of thing save in mta.server or client
     */
    public void logout() {
        studentPackingDataRequest.header.clear();
    }

    /**
     *
     * @param requestType default is Get
     * @param path is method want to execute
     * @param params
     */
    public void sendRequest(REQUEST_TYPE requestType, String path, Object... params) throws IOException {
        // TODO: 29/10/2018 send request handle code
        // pass message through socket client
        // step 0: pass request to trade public key - public key (mta.server-client) to encrypt information in secure
        publicKeyServer = sendPublicKey(studentPackingDataRequest.header);
        // step 0.5: waiting response with public key from mta.server

        // step 1: pass request type and username - password have been encrypt - to receive the way to access function

//        encryptService.encryptMessage()
        // step 1.5: get back descriptor of services which mta.server provide

        // step 2: waiting mta.server handle request and return value of full qualify of method to client execute

        // step 3: set method to request and call method from client

        // step 4: get result return from mta.server
    }

    /**
     * client-admin send public key and expect receive public key from mta.server to encrypt
     * @param header
     * @return
     * @throws IOException
     */
    public PublicKey sendPublicKey(Map<String, Object> header) throws IOException {
        byte[] publicKey = getCertified(PairKeyGenerate.PUBLIC_KEY);
        header.put("Public-Key", new String(publicKey));
        // call socket to send this
        // TODO: 29/10/2018 socket handle code
        return null;
    }

    private byte[] getCertified(PublicKey publicKey) throws IOException {
        // write object to byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writeObject = new ObjectOutputStream(out);
        writeObject.writeObject(publicKey);

        return out.toByteArray();
    }

    // receive
}
