package mta.main;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import mta.server.services.deencrypto.PairKeyGenerate;

import java.io.*;
import java.security.PublicKey;

public class MyClassLoader extends ClassLoader {

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public <T>Class getClass(String name, Object object) {
        byte[] bytesObject = SerializationUtils.serialize((Serializable) object);
        String file = name.replace('.', File.separatorChar) + ".class";
        Class<?> aClass = defineClass(name, bytesObject, 0, bytesObject.length);
        resolveClass(aClass);
        return aClass;
    }
}

class Tess {
    @Test
    public void test() {
        MyClassLoader myClassLoader = new MyClassLoader(Tess.class.getClassLoader());
        System.out.println("askljdhasdwa");
        assert PairKeyGenerate.PUBLIC_KEY != null : "ashalksjdhalkjwhaw";

        Class aClass = myClassLoader.getClass("java.security.PublicKey", PairKeyGenerate.PUBLIC_KEY);
        if (aClass != null) {
            System.out.println("Oke");
        }
    }

    @Test
    public void test1() throws IOException, ClassNotFoundException {
        PublicKey publicKey = PairKeyGenerate.PUBLIC_KEY;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(publicKey);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream input = new ObjectInputStream(in);
        Object object = input.readObject();
        if (object != null) {
            System.out.println("success");
        }
    }
}

