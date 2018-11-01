package mta.main;

import org.apache.commons.lang3.SerializationUtils;
import mta.server.services.deencrypto.PairKeyGenerate;

import java.io.*;
import java.security.PublicKey;

/**
 * Our Custom Class Loader to load the classes. Any class in the com.journaldev
 * package will be loaded using this ClassLoader. For other classes, it will
 * delegate the request to its Parent ClassLoader.
 */
public class CCLoader extends ClassLoader {

    /**
     * This constructor is used to set the parent ClassLoader
     */
    public CCLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * Loads the class from the file system. The class file should be located in
     * the file system. The name should be relative to get the file location
     *
     * @param name Fully Classified name of class, for example com.journaldev.Foo
     */
    private Class getClass(String name) throws ClassNotFoundException {
        String file = name.replace('.', File.separatorChar) + ".class";
        byte[] b = null;
        try {
            // This loads the byte code data from the file
            b = loadClassFileData(file);
            // defineClass is inherited from the ClassLoader class
            // that converts byte array into a Class. defineClass is Final
            // so we cannot override it
            Class c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Every request for a class passes through this method. If the class is in
     * com.journaldev package, we will use this classloader or else delegate the
     * request to parent classloader.
     *
     * @param name Full class name
     */
    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading Class '" + name + "'");
        if (name.startsWith("mta.server.models")) {
        System.out.println("Loading Class using mta.main.CCLoader");
        return getClass(name);
        }
        return super.loadClass(name);
    }

    /**
     * Reads the file (.class) into a byte array. The file should be
     * accessible as a resource and make sure that its not in Classpath to avoid
     * any confusion.
     *
     * @param name File name
     * @return Byte array read from the file
     * @throws IOException if any exception comes in reading the file
     */
    private byte[] loadClassFileData(String name) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                name);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;
    }
}

class Test {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
//        case 1 cham hon case 2
        case1();
        case2();
    }

    private static void case2() throws IOException, ClassNotFoundException {
        long l1 = System.currentTimeMillis();
        PublicKey publicKey = PairKeyGenerate.PUBLIC_KEY;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(publicKey);
        long l2 = System.currentTimeMillis();
        long l3 = System.currentTimeMillis();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Object object = objectInputStream.readObject();
        System.out.println(object);
        long l4 = System.currentTimeMillis();
        System.out.println(l2 - l1);
        System.out.println(l4 - l3);
    }

    private static void case1() {
        PublicKey publicKey = PairKeyGenerate.PUBLIC_KEY;
        String s = publicKey.toString();
        long l1 = System.currentTimeMillis();
        byte[] serialize = SerializationUtils.serialize(publicKey);
        long l2 = System.currentTimeMillis();
        long l3 = System.currentTimeMillis();
        Object objKey = SerializationUtils.deserialize(serialize);
        long l4 = System.currentTimeMillis();
        PublicKey publicKey1 = (PublicKey) objKey;
        String s1 = publicKey1.toString();
        System.out.println(l2 - l1);
        System.out.println(l4 - l3);
//        assert s1.equals(s);
    }
}
