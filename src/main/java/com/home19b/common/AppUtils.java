package com.home19b.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static final String DATE_ONLY_PATTERN = "dd/MM/yyyy";

    public static Date parseDate(String s, String pattern) throws ParseException {
        if (s == null || s.trim().length() == 0) {
            throw new ParseException("string for parse is null", 1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(s);
        return date;
    }

    public static String formatDate(Date d, String patern) throws NullPointerException, IllegalArgumentException {
        if (d == null) {
            return null;
        }
        return new SimpleDateFormat(patern).format(d);
    }

    public static int getSttInWeek(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        String d = simpleDateformat.format(date);
        if (d.equalsIgnoreCase("Monday")) {
            return 0;
        } else if (d.equalsIgnoreCase("Tuesday")) {
            return 1;
        } else if (d.equalsIgnoreCase("Thursday")) {
            return 3;
        } else if (d.equalsIgnoreCase("Wednesday")) {
            return 2;
        } else if (d.equalsIgnoreCase("Friday")) {
            return 4;
        } else if (d.equalsIgnoreCase("Saturday")) {
            return 5;
        } else if (d.equalsIgnoreCase("Sunday")) {
            return 6;
        }
        return 0;
    }

    public static Date clearTimeOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getFromStartWeek() {
        Date date = new Date();
        int sttInWeek = getSttInWeek(date);

        Date startWeek = AppUtils.clearTimeOfDate(appendDay(date, -sttInWeek));
        return startWeek;
    }

    public static Date getToEndWeek() {
        Date date = new Date();
        int sttInWeek = getSttInWeek(date);

        Date endWeek = AppUtils.clearTimeOfDate(appendDay(date, 6 - sttInWeek));
        return endWeek;
    }

    public static Date appendDay(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c.getTime();
    }
}
