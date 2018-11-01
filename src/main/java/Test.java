import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mta.configs.common.PackingDataRequest;
import mta.configs.common.XmlAnnotationIntrospectorEx;
import mta.server.models.Student;
import mta.server.services.deencrypto.DecryptService;
import mta.server.services.deencrypto.EncryptService;
import mta.server.services.deencrypto.PairKeyGenerate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

public class Test {


    public static void main(String[] args) throws IOException, NoSuchProviderException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        XmlMapper xmlMapper;
        xmlMapper = new XmlMapper();
        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
        PackingDataRequest<Student> objectPackingDataRequest = new PackingDataRequest<>();
        Student student = new Student(UUID.randomUUID(), "studnet 1", "hiha", 10);
        objectPackingDataRequest.setBodyData(student);
        objectPackingDataRequest.send();
        byte[] s = xmlMapper.writeValueAsBytes(objectPackingDataRequest);

        EncryptService encryptService = new EncryptService();
        DecryptService decryptService = new DecryptService();
        byte[] bytes = encryptService.encryptMessage(PairKeyGenerate.getPublicKey(), s);
        byte[] decrypt = decryptService.decrypt(bytes);
        PackingDataRequest packingDataRequest = xmlMapper.readValue(decrypt, PackingDataRequest.class);
        System.out.println(packingDataRequest);
    }
}
