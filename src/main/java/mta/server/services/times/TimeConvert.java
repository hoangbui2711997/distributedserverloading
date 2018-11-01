package mta.server.services.times;

import java.time.ZonedDateTime;

public class TimeConvert {
    /**
     * Chuyen ve mui gio 0
     * @param dateResetKeyPair
     * @return
     */
    public static ZonedDateTime convertTimeZone(ZonedDateTime dateResetKeyPair) {
        int offsetHours = dateResetKeyPair.getOffset().getTotalSeconds() / 3600;
        ZonedDateTime zonedDateTime = dateResetKeyPair.minusHours(offsetHours);
        return zonedDateTime;
    }
}
