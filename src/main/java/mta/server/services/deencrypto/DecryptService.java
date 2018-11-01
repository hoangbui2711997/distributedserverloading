package mta.server.services.deencrypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

// TODO: 28/10/2018 dich vu giai ma - su dung cho nhanh thang nao can thiet
public class DecryptService {
    //    private final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    private final Cipher cipher = Cipher.getInstance("RSA");
    private final static int PADDING = 11;

    public DecryptService() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        cipher.init(Cipher.DECRYPT_MODE, PairKeyGenerate.PRIVATE_KEY);
    }

    public byte[] decrypt(byte[] arrayBytesCipher) throws BadPaddingException, IllegalBlockSizeException {
        int length = arrayBytesCipher.length;
        int newLength = length - (int) Math.ceil((double) length / 512) * PADDING;
        byte[] arrayBytesPlain = new byte[newLength];
        int times = 0;
        int offset;
        int offset_;
        System.out.println("cipher.getAlgorithm(): " + cipher.getAlgorithm());

        int lengthDecrypt = 512;
        while (length > 0) {
            // TODO: 01/11/2018 dag toi doan copy byte de ma hoa vi day la ma hoa khoi
            offset = 512 * times;
            offset_ = 501 * times;

            byte[] arrays501Cipher;
            if(length < lengthDecrypt) {
                arrays501Cipher = Arrays.copyOfRange(arrayBytesCipher, 0 + offset, arrayBytesCipher.length);
            } else {
                arrays501Cipher = Arrays.copyOfRange(arrayBytesCipher, 0 + offset, lengthDecrypt + offset);
            }

            byte[] array501Cipher = cipher.doFinal(arrays501Cipher);

            for (int i = 0; i < array501Cipher.length; i++) {
                arrayBytesPlain[i + offset_] = array501Cipher[i];
            }

            length -= lengthDecrypt;
            times++;
        }

//      encrypt message
        return arrayBytesPlain;
    }
}
