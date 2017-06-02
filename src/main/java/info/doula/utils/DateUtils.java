package info.doula.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by Faruque on 2016-12-13.
 */
public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date formatDate(String sDate) throws Exception {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (sDate == null) {
                return null;
            } else {
                return formatter.parse(sDate);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDateToISO(String sDate) throws Exception {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            Date date = sd.parse(sDate);
            return DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getDayFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd");
        int day = Integer.parseInt(simpleDateFormat.format(date));
        return day;
    }

    public static Date getFirstDateOfMonth(Date date) throws Exception {
        try {
            Calendar calendar = Calendar.getInstance();   // this takes current date
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            date = calendar.getTime();
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getLastDateOfMonth(Date date) throws Exception {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            date = calendar.getTime();
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getMonthFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("MM");
        int month = Integer.parseInt(simpleDateFormat.format(date));
        return month;
    }

    public static Integer getLastDayFromCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        return lastDay;
    }

    public static Integer getFirstDayFromCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int lastDate = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        return lastDay;
    }

    public static Integer getYearFromCurrentDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy");
        int year = Integer.parseInt(simpleDateFormat.format(date));
        return year;
    }

    public static Date addOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    static public LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return ldt;
    }

    public static Integer getDaysBetweenToDates(Date from, Date to) {
        long diff = to.getTime() - from.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000)+1;
        int daysdiff = (int) diffDays;
        return daysdiff;
    }
}
