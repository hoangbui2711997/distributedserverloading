package mta.server.services.deencrypto;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Danh cho mta.server truyen tin, hoac client truyen tin can tinh toan ven
 */
public class HashService {
    private static final String SEPERATOR = "...|*&";

    public static <T>String encryptHash(T typeHash) throws InvocationTargetException, IllegalAccessException {
        Class<?> aClass = typeHash.getClass();
        List<Method> methods = new ArrayList<>();
        // get all value in this type deep = 1
        for (Method declaredMethod : aClass.getDeclaredMethods()) {
            if(declaredMethod.getName().startsWith("get")) {
                methods.add(declaredMethod);
            }
        }
        // get hasher to pass value to hash
        Hasher hasher = Hashing.md5().newHasher();
        for (int i = 0; i < methods.size(); i++) {
            String s = methods.get(i).invoke(typeHash).toString();
            hasher.putString(s, Charsets.UTF_8)
                    .putString(SEPERATOR, Charsets.UTF_8);

        }
//        System.out.println(hasher.toString());

        // hash
        HashCode hash = hasher.hash();
        return hash.toString();
    }
}
