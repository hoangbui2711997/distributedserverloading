package mta.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mta.ServerPublicInfo;
import mta.configs.common.PackingDataRequest;
import mta.configs.common.XmlAnnotationIntrospectorEx;
import mta.contracts.REQUEST_TYPE;
import mta.server.services.deencrypto.DecryptService;
import mta.server.services.deencrypto.PairKeyGenerate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static mta.server.services.SocketServices.readMessage;

public class SocketServer {

    //    private final SSLContext sslContext;
    private final ServerSocketFactory socketFactory;

    //region thong tin co ban cua mta.server
    ServerPublicInfo serverPublicInfo;
    //endregion

    private final ServerSocket socketServer;
    private final XmlMapper xmlMapper;
    private final HashMap<String, Object> cacheCertified;
    private final PackingDataRequest packingDataResponse;
    private final ConcurrentHashMap<String, Thread> threads;
    private AtomicInteger size = new AtomicInteger(0);

    public SocketServer(int port, int backlog) throws IOException, NoSuchAlgorithmException {
        this.cacheCertified = new HashMap<>();
        this.packingDataResponse = new PackingDataRequest();
        this.threads = new ConcurrentHashMap<>();
        // setup thong tin co ban cua mta.server
        this.serverPublicInfo = new ServerPublicInfo();
        this.packingDataResponse.header.put("Server-info", this.packingDataResponse);

        // setup port, so luong connections mta.server
        this.socketFactory = ServerSocketFactory.getDefault();
        this.socketServer = this.socketFactory.createServerSocket(port, backlog);

        // setup convert xml
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
    }

    public void start() {
        try {
            while (!socketServer.isClosed()) {
                final Socket socketClient = this.socketServer.accept();
                Thread thread = new Thread(() -> {
                    try
                            (
                                    InputStream in = socketClient.getInputStream();
                                    ObjectInputStream objectInputStream = new ObjectInputStream(in);
                                    OutputStream out = socketClient.getOutputStream();
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                            ) {
                        this.size.incrementAndGet();
                        Thread threadClient = Thread.currentThread();
                        String nameClient = helloClient(objectInputStream, objectOutputStream);

                        threadClient.setName(nameClient);
                        // co che nao do de sap xep thuc hien cac thread
//                        threadClient.setPriority();

                        // lay key ma hoa
//                        Object o = cacheCertified.get(nameClient);
                        String s = "";
                        do {
                            // doc thong tin ma hoa tu client
                            byte[] message = readMessage(socketClient, in);

                            // giai ma
                            byte[] decrypt = new DecryptService().decrypt(message);
                            String plainText = new String(decrypt);
                            System.out.println(plainText);
                            PackingDataRequest packingDataRequest = xmlMapper.readValue(plainText, PackingDataRequest.class);

                            handleRequest(packingDataRequest, in, out);
                        } while (!packingDataResponse.getMethod().equals(REQUEST_TYPE.SHUTDOWN.toString()));
                    } catch (JsonMappingException e1) {
                        e1.printStackTrace();
                    } catch (InvalidKeyException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchPaddingException e1) {
                        e1.printStackTrace();
                    } catch (IllegalBlockSizeException e1) {
                        e1.printStackTrace();
                    } catch (BadPaddingException e1) {
                        e1.printStackTrace();
                    } catch (JsonParseException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    }
//                        // -- xu ly close
//                        // cho client gui request ngat ket noi toi (trong do co ten cua client)
//                        cacheCertified.remove(packingDataRequest.header.get("Name-client"));
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                });
                while (size.get() >= 20) {
                    System.out.println("Server overload");
                    Thread.sleep(500);
                }
                thread.start();
                threads.put(thread.getName(), thread);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private REQUEST_TYPE handleRequest(PackingDataRequest packingDataRequest, InputStream in, OutputStream out) throws
            IOException, InterruptedException {
        if ("XML".equals(packingDataRequest.getContentType())) {
            String method = packingDataRequest.getMethod();
            System.out.println(method);
            System.out.println(packingDataRequest.getMethod());
            System.out.println(method.equals(REQUEST_TYPE.SHUTDOWN.toString()));
            if (method.equals(REQUEST_TYPE.SHUTDOWN.toString())) {
                Thread.sleep(1000);
                in.close();
                out.close();
                // clean cache
                Object nameClient = packingDataRequest.header.get("Client-name");
                this.cacheCertified.remove(nameClient);
                this.threads.remove(nameClient);
                int i = this.size.decrementAndGet();
                System.out.println(i);
                return REQUEST_TYPE.SHUTDOWN;
            } else if (method.equals(REQUEST_TYPE.READ.toString())) {
//                read

            } else if (method.equals(REQUEST_TYPE.CREATE.toString())) {
//                create

            } else if (method.equals(REQUEST_TYPE.DELETE.toString())) {
//                delete

            } else if (method.equals(REQUEST_TYPE.UPDATE.toString())) {
//                update

            }
        }
        return REQUEST_TYPE.SHUTDOWN;
    }


    private String helloClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws
            IOException, ClassNotFoundException {
        System.out.println("mta.server " + this.serverPublicInfo.getServerName() +
                " receive client name");
        String nameClient = objectInputStream.readUTF();

        System.out.println("mta.server " + this.serverPublicInfo.getServerName() +
                " send name");
        objectOutputStream.writeUTF(this.serverPublicInfo.getServerName());
        objectOutputStream.flush();

        // neu client ko co trong session
        if (cacheCertified.get(nameClient) == null) {
            System.out.println("mta.server " + this.serverPublicInfo.getServerName() +
                    " receiver public key!");
            Object object = objectInputStream.readObject();
            // save public key
            cacheCertified.put(nameClient, object);

            System.out.println(object);
        } else {
            System.out.println("Hiha");
        }

        // Yeu cau tiep theo cua client
        // - Neu la 0 thi khong lam gi
        // - Neu la 1 thi thuc hien trao doi key
        int directFromClient = objectInputStream.readInt();
        if (directFromClient == 1) {
            // trao doi key
            System.out.println("mta.server " + this.serverPublicInfo.getServerName() +
                    " sent public key!");
            objectOutputStream.writeObject(PairKeyGenerate.PUBLIC_KEY);
            objectOutputStream.flush();

            System.out.println("mta.server " + this.serverPublicInfo.getServerName() +
                    " send info");
            objectOutputStream.writeObject(serverPublicInfo);
            objectOutputStream.flush();
        }

        return nameClient;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        SocketServer socketServer = new SocketServer(9999, 20);
        socketServer.start();
    }
}