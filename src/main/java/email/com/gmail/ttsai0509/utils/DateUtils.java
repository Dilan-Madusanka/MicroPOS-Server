package email.com.gmail.ttsai0509.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    public static Date startOf(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        return c.getTime();
    }

    public static Date endOf(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
