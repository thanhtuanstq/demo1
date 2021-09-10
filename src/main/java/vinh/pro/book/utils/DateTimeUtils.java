package vinh.pro.book.utils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static String getISOLocalDateTime() {
        return new Timestamp(System.currentTimeMillis()).toLocalDateTime()
                                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
