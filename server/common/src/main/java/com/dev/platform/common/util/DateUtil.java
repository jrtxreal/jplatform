package com.dev.platform.common.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
    public static final String DATE_FMT = "yyyy-MM-dd";
    public static final String DATETIME_FMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转日期类型
     * @param spl spl∈T, T = fmt
     * @param fmt 格式化字符串模板
     * @return
     */
    public static Date parse(String spl,String fmt) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        return simpleDateFormat.parse(spl);
    }

    /**
     *  日期格式化为字符串
     * @param d 要格式化的日期
     * @param fmt 格式化模板字符串
     * @return
     * @throws ParseException
     */
    public static String format(Date d,String fmt){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        return simpleDateFormat.format(d);
    }
    /**
     *splA∈T, splB∈T, T = yyyy-MM-dd'T'HH:mm:ssZ
     * @param splA 时区A样例
     * @param splB 时区B样例
     * @return splA和splB的时区差
     * @throws Exception
     */
    public static int getZoneGap(String splA, String splB) throws Exception {
        int zoneNumberA = getZoneNumber(splA);
        int zoneNumberB = getZoneNumber(splB);
        return (zoneNumberA - zoneNumberB);
    }

    /**
     * 依照本地时区进行格式化
     * @param date 要进行格式化的日期
     * @return 格式化后的字符串
     */
    public static String zoneFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @param date 本地时间
     * @param zoneGap 与目标时区相差的小时数
     * @return
     */
    public static Date zoneShift(Date date,int zoneGap) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.HOUR_OF_DAY,zoneGap);
      return c.getTime();
    }

    // 忽略时区 spl∈T, T = yyyy-MM-dd'T'HH:mm:ssZ
    public static Date ignoreZone(String spl) throws ParseException {
        Pattern pattern = Pattern.compile("(.*)T(.*)[\\+|\\-]");
        Matcher matcher = pattern.matcher(spl);
        matcher.find();
        return parse(matcher.group(1) +" "+ matcher.group(2),DATETIME_FMT);
    }

    // 获取时区号 spl∈T, T = yyyy-MM-dd'T'HH:mm:ssZ
    public static int getZoneNumber(String spl) throws Exception{
        Pattern pattern = Pattern.compile("([\\+|\\-]\\d+)00$");
        Matcher matcher = pattern.matcher(spl);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }
    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }
//public static void main(String[] args) throws Exception{
//    System.out.println(ignoreZone("2021-03-21T01:18:43-0200"));
//}
}
