package mta.server;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class ServerInfo {
    HashMap<String, Object> hashMap = new HashMap();
    private static final long CAPACITY= 10;
    private static final long TIME_ZONE= ZonedDateTime.now().getOffset().getTotalSeconds() / 3600;
    // cai nay de public vi Master class phai biet thang nay! de con dong bo
    public static final HashMap<String, File> cache = new HashMap<>();
    // khi dong bo xong -> xoa het file o cache di
    public ServerInfo() {
//        something like this!
//        cache.put("nameOffile", new File("nameOfFile"));
        this.hashMap.put("Time-create", ZonedDateTime.now());
        this.hashMap.put("Time-zone", TIME_ZONE);
        this.hashMap.put("Thread-working", 0);
        this.hashMap.put("Capacity", CAPACITY);
    }
}
