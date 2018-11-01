package mta.server.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketServices {
    /**
     * doc thong tin ma hoa tu client
     *
     * @param socketClient
     * @param in
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static byte[] readMessage(Socket socketClient, InputStream in) throws IOException, InterruptedException {
        int available = in.available();
        while (available == 0 && socketClient.isConnected()) {
            Thread.sleep(1000);
            available = in.available();
//            System.out.println("available(): " + available);
        }
        System.out.println("available(): " + available);
        byte[] message = new byte[available];
        int read = in.read(message);
        System.out.println("total bytes read: " + read);

//        System.out.println("message: " + new String(message));
        return message;
    }
}
