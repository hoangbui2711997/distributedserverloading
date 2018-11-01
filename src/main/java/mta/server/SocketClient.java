package mta.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mta.ServerPublicInfo;
import mta.configs.common.PackingDataRequest;
import mta.configs.common.XmlAnnotationIntrospectorEx;
import mta.contracts.KeyValidate;
import mta.contracts.REQUEST_TYPE;
import mta.server.models.Info;
import mta.server.models.Student;
import mta.server.services.deencrypto.EncryptService;
import mta.server.services.deencrypto.PairKeyGenerate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static mta.server.services.times.TimeConvert.convertTimeZone;

public class SocketClient {

    private final SocketFactory socketFactory;
    private final String clientName;
    // port 9999 for client
//    public static final int PORT= 9999;
    // queue 20 connect
//    public static final int BACK_LOG= 20;
    private final Socket socketClient;
    private XmlMapper xmlMapper;
    private PublicKey publicKey;
    /**
     * Saving info of server
     */
    private final HashMap<String, KeyValidate> cache;

    public SocketClient(String hostName, int port) throws IOException, ClassNotFoundException {
        this.cache = getExistServer();
        System.out.println(cache.size());
        this.clientName = UUID.randomUUID().toString();
        this.socketFactory = SocketFactory.getDefault();
        this.socketClient = this.socketFactory.createSocket(hostName, port);

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
    }

    public void start() {
        Thread thread = new Thread(() -> {
            try (
                    OutputStream out = this.socketClient.getOutputStream();
                    InputStream in = this.socketClient.getInputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                    ObjectInputStream objectInputStream = new ObjectInputStream(in)
            ) {
                if (this.socketClient.isConnected()) {
                    // helloServer
                    helloServer(objectOutputStream, objectInputStream);

                    // start
//                    packingData(REQUEST_TYPE.START, null);
//
//                    String response = objectInputStream.readLine();
//                    if(response.equals("starting")) {
//                        Scanner sc = new Scanner(System.in);
//                        String s = "";
//                        while(!s.equals("stop")) {
//
//
////                            s = sc.nextLine();
//                        }
//                    }

                    Student student =
                            new Student(UUID.randomUUID(), "studnet 1", "hiha", 10);
                    String s = packingData(REQUEST_TYPE.SHUTDOWN, student);

                    // encrypt data
                    byte[] message = new EncryptService().encryptMessage(this.publicKey, s.getBytes());
//                    System.out.println("message: " + new String(message) + "\tsize: " + message.length);
                    // send data
                    out.write(message, 0, message.length);
                    out.flush();

//                    new Scanner(System.in).nextLine();
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } finally {

            }
        });
        thread.start();
    }

    private <T> String packingData(REQUEST_TYPE requestType, T dataPack) throws JsonProcessingException {
        PackingDataRequest<T> objectPackingDataRequest = new PackingDataRequest<>();
        objectPackingDataRequest.header.put("Client-name", this.clientName);
        objectPackingDataRequest.setMethod(requestType.toString());
        if (dataPack == null) {

        } else {
            if (dataPack instanceof Student) {

            } else if (dataPack instanceof Info) {

            } else {

            }
            objectPackingDataRequest.setBodyData(dataPack);
        }

        objectPackingDataRequest.send();

        // wrap in xml
        return xmlMapper.writeValueAsString(objectPackingDataRequest);
    }

    /**
     * Gui toi server xem server co quen ko
     *
     * @param objectOutputStream
     * @param objectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void helloServer(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        System.out.println("client send name");
        objectOutputStream.writeUTF(this.clientName);
        objectOutputStream.flush();

        System.out.println("client receive name mta.server");
        String nameSV = objectInputStream.readUTF();


        // by default client should sent public key
        System.out.println("client send public key!");
        objectOutputStream.writeObject(PairKeyGenerate.PUBLIC_KEY);
        objectOutputStream.flush();

        // check name mta.server exist in file
        checkCached(objectOutputStream, objectInputStream, nameSV);
    }

    /**
     * Kiem tra xem server co trong cache ko
     *
     * @param objectOutputStream
     * @param objectInputStream
     * @param nameSV
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void checkCached(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String nameSV) throws IOException, ClassNotFoundException {
        if (checkExistNameServer(nameSV)) {
            objectOutputStream.writeInt(0);
            objectOutputStream.flush();
            this.publicKey = cache.get(nameSV).getPublicKey();
        } else {
            // dua ra chi thi trao doi key voi server
            objectOutputStream.writeInt(1);
            objectOutputStream.flush();

            // add info to server
            addInforServer(objectOutputStream, objectInputStream, nameSV);
        }
    }

    /**
     * add info of server to cache
     *
     * @param objectOutputStream
     * @param objectInputStream
     * @param nameSV
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addInforServer(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String nameSV) throws IOException, ClassNotFoundException {
        System.out.println("client receive public key!");
        this.publicKey = (PublicKey) objectInputStream.readObject();
        System.out.println(this.publicKey);

        System.out.println("client send request get info of mta.server");
        ServerPublicInfo serverInfo = (ServerPublicInfo) objectInputStream.readObject();

        // chuyen ve mui gio 0
        ZonedDateTime zoneZero = convertTimeZone(serverInfo.getDateResetKeyPair());
        ZonedDateTime dateExpired = zoneZero.plusDays(serverInfo.getValidateTime());

        // truyen thong tin can biet vao Keyvalidate object
        KeyValidate keyValidate = new KeyValidate(nameSV, this.publicKey, dateExpired);

        // luu tru thong tin
        this.cache.put(keyValidate.getNameServer(), keyValidate);

        saveInfoServer();
//        xmlMapper.writeValue(new File("mta.server-info.se"), this.cache);
    }

    private void saveInfoServer() throws IOException {
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream("mta.server-info.se"));
        objectOutputStream.writeObject(this.cache);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private boolean checkValidate(KeyValidate keyValidate) {
        return
                keyValidate
                        .getDateExpired()
                        .compareTo(ZonedDateTime.now()) > 1 ? true : false;
    }

    /**
     * Lay tat ca server dang ton tai trong file
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private HashMap getExistServer() throws IOException, ClassNotFoundException {
        String fileName = "mta.server-info.se";
        try {
            if (Files.exists(Paths.get(fileName))) {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
                HashMap<String, KeyValidate> stringKeyValidateHashMap = (HashMap<String, KeyValidate>) objectInputStream.readObject();
                List<String> invalidServer = stringKeyValidateHashMap.values()
                        .stream()
                        .filter(e -> checkValidate(e))
                        .map(e -> e.getNameServer())
                        .collect(Collectors.toList());

                // delete server invalid
                invalidServer.forEach(stringKeyValidateHashMap::remove);
                return stringKeyValidateHashMap;
            } else {
                new File(fileName).createNewFile();
                return new HashMap();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new HashMap();
        }
    }

    /**
     * Kiem tra xem server da ton tai chua
     *
     * @param nameSV
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private boolean checkExistNameServer(String nameSV) throws IOException, ClassNotFoundException {
        KeyValidate keyValidate = cache.get(nameSV);
        if (keyValidate == null) {
            return false;
        }
        return keyValidate.getNameServer().equals(nameSV);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Class.forName("java.security.PublicKey");

//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
//        PackingDataRequest packingDataRequest = new PackingDataRequest();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        xmlMapper.writeValue(new File("test1.xml"), packingDataRequest);
        SocketClient socketClient = new SocketClient("localhost", 9999);
        socketClient.start();
//        test2();
    }

    public static void test2() throws IOException, ClassNotFoundException {
        for (int i = 0; i < 100; i++) {
            SocketClient socketClient = new SocketClient("localhost", 9999);
            socketClient.start();
        }
    }

    private static void test1() throws IOException, ClassNotFoundException {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            stringStringHashMap.put("" + i, "" + i);
        }

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("asasd"));
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("asasd"));
        objectOutputStream.writeObject(stringStringHashMap);
        objectOutputStream.flush();
        ;
        objectOutputStream.close();

        HashMap<String, String> stringStringHashMap1 = (HashMap<String, String>) objectInputStream.readObject();
        for (int i = 0; i < 10; i++) {
            System.out.println(stringStringHashMap1.get("" + i));
        }
        objectInputStream.close();
    }
}

//class AX {
//    public static void mta.main(String[] args) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
//        Socket socket = new Socket(InetAddress.getByName("localhost"), 9999);
//
//        if (!socket.isConnected()) {
//            socket.connect(socket.getLocalSocketAddress());
//        }
//        AdminConfig adminConfig = new AdminConfig();
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospectorEx());
//        String s = xmlMapper.writeValueAsString(adminConfig.getStudentPackingDataRequest().send());
//
//        OutputStream outputStream = socket.getOutputStream();
//
//        byte[] bytes = s.getBytes();
//        outputStream.write(bytes, 0, bytes.length);
//        outputStream.flush();
//        new Scanner(System.in).nextLine();
//        socket.close();
//    }
//    private static DateTimeFormatter dateFormat =
//            DateTimeFormatter.ofPattern("dd-MM-yyyy:hh-mm-ss.SSS Z");
//    @Test
//    public void test() throws ParseException {
////        Date date = new Date();
////        System.out.println(date.getTimezoneOffset() / 60);
////        LocalDate now = LocalDate.now();
////        System.out.println(now.toString());
////        LocalDateTime.now();
//
////        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy:hh-mm-ss.SSSS Z");
////        String format1 = ZonedDateTime.now().format(dateTimeFormatter);
////        TemporalAccessor parse = dateTimeFormatter.parse(format1);
////        // parse Date
////        LocalDate date = LocalDate.parse(format1, dateTimeFormatter);
////        // Parse Time
////        CharSequence text;
////        LocalTime time = LocalTime.parse(format1, dateTimeFormatter);
////
////        System.out.println(date);
////        System.out.println(time);
//
//        LocalDateTime dateTime = LocalDateTime.now();
//        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
//        String s = zonedDateTime.toString();
//        ZonedDateTime parse = ZonedDateTime.parse(s);
//        System.out.println(parse);
////        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
////        System.out.println(zonedDateTime);
//    }
//}
