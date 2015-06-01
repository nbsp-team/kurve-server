package utils;

import java.util.Calendar;
import java.util.Date;

/**
 * nickolay, 01.06.15.
 */
public class DateUtils {
    public static Date offsetDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
