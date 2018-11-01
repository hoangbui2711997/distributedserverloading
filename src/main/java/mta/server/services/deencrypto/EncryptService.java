package mta.server.services.deencrypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Arrays;

// TODO: 28/10/2018  dich vu ma hoa - su dung cho nhanh thang nao can thiet
public class EncryptService {
    //  RSA ma hoa duoc 512 byte - 11 byte padding
//    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    final Cipher cipher = Cipher.getInstance("RSA");
    private final static int PADDING = 11;

    public EncryptService() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
    }

    public byte[] encryptMessage(PublicKey publicKey, byte[] arrayBytesPlain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        // init cipher with encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int length = arrayBytesPlain.length;

        // padding = 11 * blocks cipher
//        int newLength = length + (int) Math.ceil((double) length / 501) * PADDING;
        int newLength = (int) Math.ceil((double) length / 501) * 512;
        System.out.println(Math.ceil((double) length / 501));

        System.out.println(length);
        System.out.println(newLength);
        byte[] arrayBytesCipher = new byte[newLength];

        int times = 0;
        int offset;
        int offset_;
        System.out.println("cipher.getAlgorithm(): " + cipher.getAlgorithm());

        int lengthEncrypt = 501;

        while (length > 0) {
            // TODO: 01/11/2018 dag toi doan copy byte de ma hoa vi day la ma hoa khoi
            offset = (501 * times);
            offset_ = (512 * times);
            byte[] array501Plain = null;

            if(length < lengthEncrypt) {
                array501Plain = Arrays.copyOfRange(arrayBytesPlain, 0 + offset, arrayBytesPlain.length);
            } else {
                array501Plain = Arrays.copyOfRange(arrayBytesPlain, 0 + offset, lengthEncrypt + offset);
            }

            byte[] array501Cipher = cipher.doFinal(array501Plain);
            System.out.println(offset_ + array501Cipher.length);

            for (int i = 0; i < array501Cipher.length; i++) {
                arrayBytesCipher[i + offset_] = array501Cipher[i];
            }

            length -= lengthEncrypt;
            times++;
        }

        // encrypt message
        return arrayBytesCipher;
    }
}
