package mta.main;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import mta.configs.AdminConfig;
import mta.configs.common.PackingDataRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import mta.server.models.Login;
import mta.server.models.Student;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Note: putString("abc", Charsets.UTF_8).putString("def", Charsets.UTF_8) is
 * fully equivalent to putString("ab", Charsets.UTF_8).putString("cdef", Charsets.UTF_8),
 * because they produce the same byte sequence. This can cause unintended hash collisions.
 * Adding separators of some kind can help eliminate unintended hash collisions.
 */
public class Overview {

    public static final String SEPERATOR = "...|*&";

    public <T>void encryptHash(T typeHash) throws InvocationTargetException, IllegalAccessException {
        Class<?> aClass = typeHash.getClass();
        List<Method> methods = new ArrayList<>();
        for (Method declaredMethod : aClass.getDeclaredMethods()) {
            if(declaredMethod.getName().startsWith("get")) {
                methods.add(declaredMethod);
            }
        }
        Hasher hasher = Hashing.md5()
                .newHasher();
        for (int i = 0; i < methods.size(); i++) {
            System.out.println(methods.get(i));
            String s = methods.get(i).invoke(typeHash).toString();
            hasher
                    .putString(s, Charsets.UTF_8)
                    .putString(SEPERATOR, Charsets.UTF_8)
            ;

        }
//        System.out.println(hasher.toString());

        HashCode hash = hasher.hash();
        System.out.println(hash.toString());
    }

    /**
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     *
     * The RSA algorithm can only encrypt data that has a maximum byte length of the RSA key length in bits divided
     * with eight minus eleven padding bytes, i.e. number of maximum bytes = key length in bits / 8 - 11.
     *
     * So basicly you divide the key length with 8 -11(if you have padding). For example if you have
     * a 2048 bit key you can encrypt 2048/8 = 256 bytes (- 11 bytes if you have padding).
     * So, either use a larger key or you encrypt the data with a symmetric key,
     * and encrypt that key with rsa (which is the recommended approach).
     *
     * That will require you to:
     *
     * generate a symmetric key
     * Encrypt the data with the symmetric key
     * Encrypt the symmetric key with rsa
     * send the encrypted key and the data
     * Decrypt the encrypted symmetric key with rsa
     * decrypt the data with the symmetric key
     * done :)
     */
    @Test
    public void encryptMessage() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // key size of cipher
        keyPairGenerator.initialize(2048 * 2);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();

        Cipher cipher = Cipher.getInstance("RSA");
        // init cipher with encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, aPrivate);
        List<String> strings = Files.readAllLines(Paths.get("test.xml"));
        String join = Joiner.on("").join(strings);
//        byte[] signed = cipher.doFinal(join.getBytes());
        byte[] signed = cipher.doFinal(join.getBytes());
        System.out.println(new String(signed));

        Cipher cipher1 = cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, aPublic);
        byte[] verifier = cipher1.doFinal(signed);

        PackingDataRequest packingDataRequest = new XmlMapper().readValue(new String(verifier), PackingDataRequest.class);
        System.out.println(packingDataRequest);
        System.out.println(new String(verifier));
    }

//    @Test
//    public void classLoaderTest() throws IOException, ClassNotFoundException {
//        ClassLoader classLoader = new ClassLoader(Overview.class.getClassLoader()) {
//            @Override
//            public Class<?> loadClass(String name) throws ClassNotFoundException {
//                byte[] bytesClass;
//                String file = name.replace('.', File.separatorChar) + ".class";
//                try {
//                    bytesClass = getBytesClass();
//                    Class<?> aClass = defineClass(file, bytesClass, 0, bytesClass.length);
//                    resolveClass(aClass);
//                    return aClass;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return super.loadClass(name);
//            }
//
//            public byte[] getBytesClass() throws IOException {
//                FileInputStream fileInputStream = new FileInputStream("test1");
//                int size = fileInputStream.available();
//                byte[] data = new byte[size];
//                fileInputStream.read(data);
//                String s = new String(data);
//                System.out.println(s);
//                return data;
//            }
//        };
//    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IOException, IllegalBlockSizeException, NoSuchProviderException {
        Student student = new Student(UUID.fromString("a8fadf1b-ff2b-4132-8e61-d0859998fe39"), "Student name", "Student title", 20);
//        new mta.main.Overview().encryptHash(student);
        AdminConfig adminConfig = new AdminConfig();
        Login login = new Login("admin", "admin", "amdin@gmail.com");
        byte[] serialize = SerializationUtils.serialize(login);
        Login deserialize = SerializationUtils.deserialize(serialize);
        System.out.println(deserialize);
//        student.setLogin(login);
//        adminConfig.login(login);

        PackingDataRequest<Student> studentPackingDataRequest = adminConfig.getStudentPackingDataRequest();
        System.out.println(studentPackingDataRequest.toString());
    }

//    public static void mta.main(String[] args) throws IOException {
//        Student student = new Student(UUID.randomUUID(), "Student name", "Student title", 20);
//        long l = System.currentTimeMillis();
//        // Ton nhieu chi phi tao thang nay, roi vao khoang 0.5s
//
//
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
////        xmlMapper.writeValue(new File("test.xml"), student);
////        String s = xmlMapper.writeValueAsString(student);
////        Student student1 = xmlMapper.readValue(s, Student.class);
//        PackingDataRequest<Student> packingDataRequest = new PackingDataRequest();
//        packingDataRequest.setBodyData(student);
//        packingDataRequest.setMethod(mta.main.Overview.class.getClass().getName());
//
////        String s = xmlMapper.writeValueAsString(packingDataRequest.send());
////        System.out.println(s);
//        xmlMapper.writeValue(new File("test.xml"), packingDataRequest.send());
//
//        long l1 = System.currentTimeMillis();
//        System.out.println(l1 - l);
////        System.out.println(s);
////        System.out.println(student1);
//    }
}

class A {
    protected static int a = 10;

    public int getA() {
        return a;
    }
}

class B extends A{

}

class C {
    public static void main(String[] args) {
        A.a = 100;
        B b = new B();
        System.out.println(b.getA());
    }
}
