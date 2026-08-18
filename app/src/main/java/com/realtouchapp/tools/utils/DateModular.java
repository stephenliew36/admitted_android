package com.realtouchapp.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>日期模組: </p>
 *
 * @author Robert Chou didi31139@gmail.com
 * @version 1.0
 * @date 2016年5月20日 下午1:28:26
 */
public class DateModular {
    /**
     * 取得格式為目前當天 "5/20(五)"
     */
    public static String[] getDays(int daysize) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String[] days = new String[daysize];
        days[0] = getMMDDW(calendar);
        for (int i = 1; i < daysize; i++) {
            calendar.add(Calendar.DATE, 1);
            days[i] = getMMDDW(calendar);
        }

        return days;
    }

    public static String[] getYYYYMMDD(int size) {
        String[] days = new String[size];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        days[0] = simpleDateFormat.format(calendar.getTime());
        for (int i = 1; i < size; i++) {
            calendar.add(Calendar.DATE, 1);
            days[i] = simpleDateFormat.format(calendar.getTime());
        }
        return days;
    }

    public static String getCurrentDayYYYYMMDD(int date, String spliteSymbol) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + spliteSymbol + "MM" + spliteSymbol + "dd");
        calendar.add(Calendar.DATE, date);
        String result = simpleDateFormat.format(calendar.getTime());
        return result;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy");
        String result = simpleDateFormat.format(calendar.getTime());
        return Integer.valueOf(result);
    }
    public static String getCurrenYYYYMMDDHHMMSS() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = simpleDateFormat.format(calendar.getTime());
        return result;
    }

    public static long getLong(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            
            e.printStackTrace();
        }
        date2.getTime();

        return date2.getTime();
    }

    /**
     * 回傳 "月/日(週)"
     */
    public static String getMMDDW(Calendar calendar) {

        return getMonThDay(calendar, "/") + getWeek(calendar, "(", ")");
    }


    /**
     * <p>回傳"start週end": </p>
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年5月20日 下午2:33:44
     * @version
     */
    public static String getWeek(Calendar calendar, String start, String end) {
        String week = "";
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //判斷取得的數值等於星期幾
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                week = "日";
                break;
            case Calendar.MONDAY:
                week = "一";
                break;
            case Calendar.TUESDAY:
                week = "二";
                break;
            case Calendar.WEDNESDAY:
                week = "三";
                break;
            case Calendar.THURSDAY:
                week = "四";
                break;
            case Calendar.FRIDAY:
                week = "五";
                break;
            case Calendar.SATURDAY:
                week = "六";
        }
        return start + week + end;
    }

    /**
     * <p>回傳"start週end": </p>
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年5月20日 下午2:33:44
     * @version
     */
    public static String getWeek(String start, String end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return getWeek(calendar, start, end);
    }

    /**
     * <p>回傳"月divider日": </p>
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年5月20日 下午2:33:44
     * @version
     */
    public static String getMonThDay(Calendar calendar, String divider) {
        String mmdd = "";
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mmdd = "" + month + divider + day;
        return mmdd;
    }

    /**
     * <p>回傳"月divider日": </p>
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年5月20日 下午2:33:44
     * @version
     */
    public static String getMonThDay(String divider) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return getMonThDay(calendar, divider);
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年7月7日 下午3:49:51
     * @version
     */
    public static Boolean isNowBiggerThanDate(String date, String dateformat) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        //將字串轉成Date型
        try {
            dt = sdf.parse(date);

        } catch (ParseException e) {
            
            e.printStackTrace();
        }
        return System.currentTimeMillis() > dt.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年7月7日 下午3:49:51
     * @version
     */
    public static Boolean isNowBiggerThanDate(String date, String dateformat, int days) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        //將字串轉成Date型
        try {
            dt = sdf.parse(date);

        } catch (ParseException e) {
            
            e.printStackTrace();
        }
        return System.currentTimeMillis() + days * 86400000 > dt.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @author Robert Chou didi31139@gmail.com
     * @date 2016年7月7日 下午3:49:45
     * @version
     */
    public static Boolean isSrcBiggerThanDst(String src, String dst, String dateFormat) {
        Date srcDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //將字串轉成Date型
        try {
            srcDate = sdf.parse(src);
        } catch (ParseException e) {
            
            e.printStackTrace();
        }

        Date dstDate = new Date();
        //將字串轉成Date型
        try {
            dstDate = sdf.parse(src);
        } catch (ParseException e) {
            
            e.printStackTrace();
        }
        return dstDate.getTime() > srcDate.getTime();
    }
}
