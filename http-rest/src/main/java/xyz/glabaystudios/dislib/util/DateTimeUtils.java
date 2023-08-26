package xyz.glabaystudios.dislib.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DateTimeUtils {

    default String getCurrentDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
