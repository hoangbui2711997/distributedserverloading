package mta.server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Redirect request to mta.server.
 * Collect information of mta.server
 */
public class Master {
    // information of servers -- khoi tao khoang 3 mta.server de dong bo
    List<ServerInfo> serverInfor = new LinkedList<>();
    // open socket for client with port 9999 and backlog 20
    SocketClient socketClient = new SocketClient("localhost", 9999);
    // Mo them 1 port de giao tiep voi 3 thang mta.server service trao doi = ssl

    public Master() throws IOException, ClassNotFoundException {
//        serverInfor.add();
    }

    // format path: package.class$method_Type arg1,type arg2,type arg3,...
    // type must be full path
    public static void redirect(InputStream pack) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        neu muon dieu huong thi phai biet thong tin cua tat ca mta.server
//        String path = "";
//        String[] classMethod = path.split("\\$");
//        // get class
//        String strClazz = classMethod[0];
//        //
//        String[] methodArgs = classMethod[1].split("_");
//        // get method
//        String method = methodArgs[0];
//        // get args
//        String[] args = methodArgs[1].split(",");
//
//        List<Object> objects = new ArrayList<>();
//
//        Class clazz = Class.forName(Master.class.getName());
//        Object instance = clazz.newInstance();
//        Method display =
//                clazz.getMethod("display");
//        display.invoke(instance);
    }

    public void display() {
        System.out.println("display");
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        Master.redirect("", null);
    }
}
