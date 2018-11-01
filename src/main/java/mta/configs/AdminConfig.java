package mta.configs;

import mta.configs.common.PackingDataRequest;
import mta.server.models.Student;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Chuc nang cua admin
 * them, sua, xoa, tim kiem, xem tat ca
 */
// TODO: 28/10/2018 XU ly packing goi tin tu admin client - lam tiep cai nay
/**
 * Config for packing request to receive information from mta.server
 */
public class AdminConfig extends ClientConfig{
    private static PackingDataRequest studentPackingDataRequest = new PackingDataRequest();
    public AdminConfig() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
//        super();
        studentPackingDataRequest.header.put("Role", "admin");
    }

    public PackingDataRequest<Student> getStudentPackingDataRequest() {
        return studentPackingDataRequest;
    }
}
