package io.github.zy945.util.dateUtil;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 伍六七
 * @date 2023/3/1 18:16
 */
public class DateUtil {
    //HH是24小时,hh是12小时
    public static final DateTimeFormatter DateTimeForMatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //一天的毫秒数
    private static final long nd = 1000 * 60 * 60 * 24;
    //一小时的毫秒数
    private static final long nh = 1000 * 60 * 60;
    //一分钟的毫秒数
    private static final long nm = 1000 * 60;
    //一秒钟的毫秒数
    private static final long ns = 1000;


    public enum Time {
        DAY, HOUR, MINUTE, SECOND;
    }


    /**
     * 正则匹配，解析
     *
     * @param content 2023年2月21日 下午07时14分06秒
     * @return date Tue Feb 21 19:14:06 CST 2023
     * @throws ParseException
     */
    public static Date getDateByMailContent(String content) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Matcher dateMatcher1 = Pattern.compile("(.*?) ").matcher(content);
        Matcher dateMatcher2 = Pattern.compile("午(.*?)秒").matcher(content);
        Matcher dateMatcher3 = Pattern.compile("日(.*?)午").matcher(content);

        String date1 = null;
        String date2 = null;
        if (dateMatcher1.find() && dateMatcher2.find()) {
            date1 = dateMatcher1.group();
            date2 = dateMatcher2.group().substring(1);
        }
        Date finalTime = ft.parse(date1 + date2);
        Calendar calendar = Calendar.getInstance();
        if (dateMatcher3.find() && dateMatcher3.group().contains("下午")) {
            calendar.setTime(finalTime);
//            第一个参数如果是1则代表的是对年份操作，2是对月份操作，3是对星期操作，5是对日期操作，11是对小时操作，12是对分钟操作，13是对秒操作，14是对毫秒操作。
//            第二个参数则是加或者减指定的 年/月/周/日/时/分/秒/毫秒
            calendar.add(Calendar.HOUR_OF_DAY, +12);
            finalTime = calendar.getTime();
        }
        return finalTime;
    }

    /**
     * jdk 1.8<br/>
     * 获取当前时间<br/>
     * 格式: HH:mm:dd<br/>
     *
     * @return String
     */
    public static String getNowTimeStr() {
        return LocalTime.now().format(TimeFormatter);
    }

    /**
     * jdk 1.8<br/>
     * 获取当前时间<br/>
     * 格式: yyyy-MM-dd<br/>
     *
     * @return String
     */
    public static String getNowDateStr() {
        return LocalDate.now().format(DateFormatter);
    }

    /**
     * jdk 1.8 <br/>
     * 获取当前时间<br/>
     * 格式: yyyy-MM-dd HH:mm:dd<br/>
     *
     * @return String
     */
    public static String getNowDateTimeStr() {
        return LocalDateTime.now().format(DateTimeForMatter);
    }

    public static Instant getNowInstant() {
        return Instant.now().plus(Duration.ofHours(8));
    }

    public static Instant getBeforeDayInstant(Instant now, Integer day) {
        if (day == null || day < 0) {
            throw new RuntimeException("day不能为null或负数");
        }
        return now.minus(Period.ofDays(day));
    }

    public static Instant getBeforeMinuteInstant(Instant now, Integer minute) {
        if (minute == null || minute < 0) {
            throw new RuntimeException("minute不能为null或负数");
        }
        return now.minus(Duration.ofMinutes(minute));
    }

    public static Instant getBeforeSecondInstant(Instant now, Long second) {
        if (second == null || second < 0) {
            throw new RuntimeException("second不能为null或负数");
        }
        return now.minus(Duration.ofSeconds(second));
    }

    public static Date getNowDate() {
        String dateStr = LocalDateTime.now().format(DateTimeForMatter);
        Date parse = null;
        try {
            parse = format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

    /**
     * 只能解决当天的24
     *
     * @param endTime
     * @param startTime
     * @param type
     * @return
     */
    public static long getTimeDelta(String startTime, String endTime, Time type) {
        //时间格式，自己可以随便定义
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            //获取两个时间的毫秒时间差
            diff = format.parse(endTime).getTime() - format.parse(startTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算相差的天数
        long time = -1L;
        if (type.equals(Time.DAY)) {
            time = diff / nd;
        }
        //计算相差的小时
        if (type.equals(Time.HOUR)) {
//            time = diff % nd / nh;
            time = diff / nh;
        }
        //计算相差的分钟
        if (type.equals(Time.MINUTE)) {
            time = diff % nd % nh / nm;
        }
        //计算相差的秒
        if (type.equals(Time.SECOND)) {
            time = diff % nd % nh % nm / ns;
        }
        return time;
    }

    public static String getStr(Long timeMs) {
        long days = timeMs / (24 * 3600);
        long hours = (timeMs % (24 * 3600)) / 3600;
        long minutes = (timeMs % 3600) / 60;
        long seconds = timeMs % 60;
        return "系统已运行：" + (int) days + " 天 " + (int) hours + " 小时 " + (int) minutes + " 分钟 " + (int) seconds + " 秒";
    }

}
