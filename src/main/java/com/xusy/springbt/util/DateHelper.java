package com.xusy.springbt.util;

import com.xusy.springbt.exception.ApplicationException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateHelper {
    // 全日期时间格式
    public static final String FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    // 日期格式
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_2 = "yyyyMMdd";
    public static final String DATE_PATTERN_3 = "yyMMddHHmmss";
    public static final String DATE_PATTERN_4 = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_5 = "yyyy年MM月dd日";
    public static final String YEAR_PATTERN = "yyyy";
    public static final String MONTH_PATTERN = "MM";
    public static final String DAY_PATTERN = "dd";
    // 年月格式
    public static final String YEAR_MONTH_PATTERN = "yyyyMM";
    public static final String YEAR_MONTH_PATTERN_2 = "yyyy-MM";
    // 月份格式
    public static final String MONTH_DAY_PATTERN = "MM-dd";
    // 时间格式
    public static final String TIME_PATTERN = "HH:mm:ss";

    public static final String LOCALE_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public static final SimpleDateFormat DAY_FORMAT_2 = new SimpleDateFormat(DATE_PATTERN_2);
    public static final SimpleDateFormat DAY_FORMAT_4 = new SimpleDateFormat(DATE_PATTERN_4);
    public static final SimpleDateFormat DAY_FORMAT_5 = new SimpleDateFormat(DATE_PATTERN_5);
    public static final SimpleDateFormat MONTH_DAY_FORMAT = new SimpleDateFormat(MONTH_DAY_PATTERN);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(FULL_PATTERN);
    public static final SimpleDateFormat ONLY_TIME_FORMAT = new SimpleDateFormat(TIME_PATTERN);
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat(YEAR_MONTH_PATTERN);
    public static final SimpleDateFormat YEAR_MONTH_FORMAT_2 = new SimpleDateFormat(YEAR_MONTH_PATTERN_2);
    public static final SimpleDateFormat ONLY_YEAR_FORMAT = new SimpleDateFormat(YEAR_PATTERN);
    public static final SimpleDateFormat ONLY_MONTH_FORMAT = new SimpleDateFormat(MONTH_PATTERN);
    public static final SimpleDateFormat ONLY_DAY_FORMAT = new SimpleDateFormat(DAY_PATTERN);

    /**
     * 标准日期格式转字符串
     *
     * @param date 标准日期
     */
    public static String conventStandardDateToStr(String date) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat(LOCALE_PATTERN, Locale.UK);
        Date temp = sdf1.parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(FULL_PATTERN);
        return formatter.format(temp);
    }

    /**
     * 日期格式转换
     * 将yyyyMMdd ——》yyyy-MM-dd
     */
    public static final String dateTransType(String time) throws ParseException {
        String msg = validate(time);
        if ("日期格式正确".equals(msg)) {
            Date date = DAY_FORMAT_2.parse(time);
            return DAY_FORMAT.format(date);
        } else {
            throw new ApplicationException("导入时间的" + msg);
        }
    }

    /**
     * 日期转字符串
     *
     * @param date    日期
     * @param pattern 转换后的日期格式
     */
    public static final String date2String(final Date date, final String pattern) {
        return getFormatter(pattern).format(date);
    }

    /**
     * 字符串转日期，此方法会根据 dateStr 的格式自行判断其 pattern，如果
     * 不需要这么做可以使用 ：
     * string2Date(final String dateStr, final String pattern)
     * 方法
     *
     * @param dateStr 日期串
     */
    public static Date string2Date(String dateStr) throws ParseException {
        Date date = null;
        if (dateStr != null && !"".equals(dateStr)) {
            final String pattern = getPatternByString(dateStr);
            date = string2Date(dateStr, pattern);
        }
        return date;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式器
     */
    public static Date string2Date(final String dateStr, final String pattern) throws ParseException {
        Date date = null;
        if (dateStr != null && !"".equals(dateStr)) {
            date = getFormatter(pattern).parse(dateStr);
        }
        return date;
    }

    //2019-01-01 转换成 2019年01月01日
    public static String dateFormatChange(String date) throws ParseException {
        String res = "";
        if (date != null && !"".equals(date)) {
            res = DAY_FORMAT_5.format(DAY_FORMAT.parse(date));
        }
        return res;
    }

    /**
     * 根据字符串返回对应的日期格式
     *
     * @param timeStr 日期字符串
     */
    public static final String getPatternByString(final String timeStr) {
        final String pattern;
        if (timeStr.matches("^(\\d{4})-(0?\\d{1}|1[0-2])-(0?\\d{1}|[12]\\d{1}|3[01])$")) {
            pattern = DateHelper.DATE_PATTERN;
        } else if (timeStr.matches("^(\\d{4})-(0?\\d{1}|1[0-2])-(0?\\d{1}|[12]\\d{1}|3[01])"
                + " (0?\\d{1}|1\\d{1}|2[0123]):(0?\\d{1}|[12345]\\d{1}):(0?\\d{1}|[12345]\\d{1})$")) {
            pattern = DateHelper.FULL_PATTERN;
        } else if (timeStr.matches("^(0?\\d{1}|1\\d{1}|2[0123]):(0?\\d{1}|[12345]\\d{1}):(0?\\d{1}|[12345]\\d{1})$")) {
            pattern = DateHelper.TIME_PATTERN;
        } else {
            pattern = DateHelper.DATE_PATTERN;
        }
        return pattern;
    }

    private static DateFormat getFormatter(final String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return ONLY_YEAR_FORMAT.format(new Date());
    }

    /**
     * 获取指定yyyy-MM-dd格式的YYYY格式
     *
     * @return
     */
    public static String getJustYear(String date) throws ParseException {
        Date temp = DAY_FORMAT.parse(date);
        return ONLY_YEAR_FORMAT.format(temp);
    }

    /**
     * 获取指定yyyy-MM-dd格式的MM格式
     *
     * @return
     */
    public static String getJustMonth(String date) throws ParseException {
        Date temp = DAY_FORMAT.parse(date);
        return ONLY_MONTH_FORMAT.format(temp);
    }

    /**
     * 获取指定yyyy-MM-dd格式的dd格式
     *
     * @return
     */
    public static String getJustDay(String date) throws ParseException {
        Date temp = DAY_FORMAT.parse(date);
        return ONLY_DAY_FORMAT.format(temp);
    }

    /**
     * 获取MM-DD格式
     *
     * @return
     */
    public static String getMonth(String date) throws ParseException {
        Date temp = DAY_FORMAT.parse(date);
        return MONTH_DAY_FORMAT.format(temp);
    }

    /**
     * 获取MM-DD格式
     *
     * @return
     */
    public static String getMonth() {
        return MONTH_DAY_FORMAT.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return DAY_FORMAT.format(new Date());
    }

    /**
     * 获取指定日期YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay(Date date) {
        return DAY_FORMAT.format(date);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return DAY_FORMAT_2.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return TIME_FORMAT.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * 获取HH:mm:ss格式
     */
    public static String getJustTime(Date date) {
        return ONLY_TIME_FORMAT.format(date);
    }

    /**
     * 获取yyyyMM格式
     */
    public static String getYearMonth(Date date) {
        return YEAR_MONTH_FORMAT.format(date);
    }

    /**
     * 获取yyyy-MM格式
     */
    public static String getYearMonth2(Date date) {
        return YEAR_MONTH_FORMAT_2.format(date);
    }

    /**
     * 获取yyyyMMddHHmmss格式
     */
    public static String getDate4(Date date) {
        return DAY_FORMAT_4.format(date);
    }

    /**
     * 获取HH:mm:ss时间的分钟差
     *
     * @param firstTime  第一个时间
     * @param secondTime 第二个时间
     * @return
     */
    public static int getDifferenceTimeMinute(String firstTime, String secondTime) {
        String[] firstStrArr = firstTime.split(":");
        // 精确到秒，还是因为有个09:00:01上班的标记为迟到。
        int firstSecond = Integer.parseInt(firstStrArr[0]) * 60 * 60
                + Integer.parseInt(firstStrArr[1]) * 60
                + Integer.parseInt(firstStrArr[2]);
        String[] secondStrArr = secondTime.split(":");
        int secondSecond = Integer.parseInt(secondStrArr[0]) * 60 * 60
                + Integer.parseInt(secondStrArr[1]) * 60
                + Integer.parseInt(secondStrArr[2]);
        double ceil = Math.ceil((secondSecond - firstSecond) / 60d);
        return new Double(ceil).intValue();
    }

    /**
     * 获取两个date的天数差
     *
     * @param firstDate  第一个时间
     * @param secondDate 第二个时间
     * @return
     */
    public static int calcDays(Date firstDate, Date secondDate) {
        return (int) (Math.abs(firstDate.getTime() - secondDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 校验日期是否合法
     *
     * @param: sourceDate
     * @Date in 11:32 on 2017/12/28.
     * @version V1.0
     **/
    public static boolean validateDate(String sourceDate) {
        boolean validate = false;
        if (!StringUtils.isEmpty(sourceDate)) {
            DAY_FORMAT.setLenient(false);
            try {
                DAY_FORMAT.parse(sourceDate);
            } catch (ParseException parseEx) {
                parseEx.printStackTrace();
            }
            validate = true;
        }
        return validate;
    }

    /**
     * 字符串日期比较大小
     *
     * @param: startDate 开始日期
     * @param: endDate   结束日期
     **/
    public static boolean compareDate(SimpleDateFormat format, String startDate, String endDate) {
        boolean result = false;
        try {
            Date sDate = format.parse(startDate);
            Date eDate = format.parse(endDate);
            if (!eDate.before(sDate)) {
                result = true;
            }
        } catch (ParseException parseEx) {
            parseEx.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串日期比较大小
     *
     * @param: startDate 开始日期
     * @param: endDate   结束日期
     **/
    public static int compareDate2(SimpleDateFormat format, String startDate, String endDate) {
        int result = 0;
        if (!"".equals(startDate)) {
            try {
                Date sDate = format.parse(startDate);
                Date eDate = format.parse(endDate);
                long sDateLong = sDate.getTime();
                long eDateLong = eDate.getTime();
                if (sDateLong < eDateLong) {
                    result = 1;
                } else if (sDateLong > eDateLong) {
                    result = -1;
                }
            } catch (ParseException parseEx) {
                parseEx.printStackTrace();
            }
        } else {
            result = 1;
        }
        return result;
    }

    /**
     * 获取指定日期是周几
     *
     * @param date  日期
     * @param isEng false表示英文星期，true表示中文星期
     */
    public static String getWeekOfDate(Date date, Boolean isEng) {
        String[] weekDays;
        if (isEng) {
            weekDays = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        } else {
            weekDays = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) {
            week = 0;
        }
        return weekDays[week];
    }

    /**
     * 获取具体指定的年月日
     *
     * @param type 年、月、日
     */
    public static String getCalendarValue(String type) {
        Calendar calendar = Calendar.getInstance();
        int result = "年".equals(type)
                ? calendar.get(Calendar.YEAR)
                : "月".equals(type)
                ? calendar.get(Calendar.MONTH) + 1 : "日".equals(type) ? calendar.get(Calendar.DAY_OF_MONTH) : -1;
        return String.valueOf(result > 9 ? result : "0".concat(String.valueOf(result)));
    }

    /**
     * 月初
     */
    public static String getFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * 指定月份最后一天
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * 月末
     */
    public static String getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        return DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * 获取指定月末
     */
    public static String getLastDayOfMonth(String date) throws ParseException {
        Date date1 = DAY_FORMAT.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        return DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * 获取指定月初
     */
    public static String getFirstDayOfMonth(String date) throws ParseException {
        Date date1 = DAY_FORMAT.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * 判断某一时间点是否在某一时间段内
     *
     * @param time         System.currentTimeMillis()
     * @param beginDateStr 开始时间 yyyy-MM-dd hh:mm:ss
     * @param endDateStr   结束时间 yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static boolean isInDate(String time, String beginDateStr, String endDateStr) throws ParseException {
        boolean result = false;
        // 需要比较的时间
        Date compareDate = DAY_FORMAT.parse(time);
        Date beginDate = DAY_FORMAT.parse(beginDateStr);
        Date endDate = DAY_FORMAT.parse(endDateStr);
        if (compareDate.getTime() >= beginDate.getTime()
                && compareDate.getTime() <= endDate.getTime()) {
            result = true;
        }
        return result;
    }

    /**
     * Date 转 Calendar
     *
     * @param date 时间
     * @return instance Calendar
     */
    public static Calendar data2Calendar(Date date) {
        Calendar instance = null;
        if (date != null) {
            instance = Calendar.getInstance();
            instance.setTime(date);
        }
        return instance;
    }

    /**
     * 时间 字符串 转 Calendar
     *
     * @param dateStr 字符串
     * @param pattern 时间格式
     * @return instance Calendar
     */
    public static Calendar string2Calendar(final String dateStr, final String pattern) throws ParseException {
        Date date = string2Date(dateStr, pattern);
        Calendar instance = null;
        if (date != null) {
            instance = Calendar.getInstance();
            instance.setTime(date);
        }
        return instance;
    }

    public static String dateFormatChange(final String dateStr, final String orginFormat, String targetFormat) throws ParseException {
        Date date = string2Date(dateStr, orginFormat);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetFormat);
        return simpleDateFormat.format(date);
    }

    /**
     * 比教两个时间相差年份
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return 年份精确到两位数
     */
    public static String yearCompare(Date fromDate, Date toDate) {
        BigDecimal years = BigDecimal.ZERO;
        if (fromDate != null && toDate != null) {
            Calendar timeFrom = Calendar.getInstance();
            timeFrom.setTime(fromDate);
            Calendar timeTo = Calendar.getInstance();
            timeTo.setTime(toDate);
            //只要年月
            int fromYear = timeFrom.get(Calendar.YEAR);
            int fromMonth = timeFrom.get(Calendar.MONTH);
            int toYear = timeTo.get(Calendar.YEAR);
            int toMonth = timeTo.get(Calendar.MONTH);
            int year = toYear - fromYear;
            int month = toMonth - fromMonth;
            BigDecimal bigYear = new BigDecimal(year);
            BigDecimal bigMonth = new BigDecimal(month);
            //返回2位小数，并且四舍五入
            years = bigYear.add(bigMonth.divide(new BigDecimal(12), 1, RoundingMode.HALF_UP));
        }
        DecimalFormat decimalFormat = new DecimalFormat("######0.0");
        return decimalFormat.format(years);
    }

    // 日期加几天、月、年
    public synchronized static String dateAddDate(String type, String Date, Integer addInteger) {
        Date sDate = null;
        try {
            sDate = DAY_FORMAT.parse(Date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sDate);
            if ("DAY".equals(type)) {
                cal.add(Calendar.DATE, addInteger);
            } else if ("MONTH".equals(type)) {
                cal.add(Calendar.MONTH, addInteger);
            } else if ("YEAR".equals(type)) {
                cal.add(Calendar.YEAR, addInteger);
            }
            return DAY_FORMAT.format(cal.getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //将传入的日期判断如果是双月则减一天，否则不处理。只取单月
    public static String checkDate(String date) throws ParseException {
        String mouth = getJustMonth(date);
        int mon = Integer.parseInt(mouth);
        String resDate = date;
        if (mon % 2 == 0) {
            resDate = dateAddDate("MONTH", date, -1);
        }
        return resDate;
    }

    //时间周期处理，用于合作商销售统计
    public static String cycleTime(int typeId, int id, String date) throws ParseException {
        String mouth = getJustMonth(date);
        int mon = Integer.parseInt(mouth);
        String cycleTime = "";
        //type == 1,计算结果月初，== 0 计算结果是月末
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(date));
        if (mon % 2 == 0 && typeId == 1) {
            int addInteger = id - 1;
            cycleTime = getFirstTime(date, addInteger);
        } else if (mon % 2 == 1 && typeId == 1) {
            cycleTime = getFirstTime(date, id);
        } else {
            if (mon % 2 == 1) {
                cycleTime = getLastTime(date, id);
            } else {
                int addInteger = id - 2;
                cycleTime = getLastTime(date, addInteger);
            }
        }
        return cycleTime;
    }

    //获取指定日期前n个月的月初
    public static String getFirstTime(String date, int num) throws ParseException {
        Calendar cale = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        cale = Calendar.getInstance();
        cale.setTime(format.parse(date));
        cale.add(Calendar.MONTH, num);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    //获取指定日期前n个月的月末
    public static String getLastTime(String date, int num) throws ParseException {
        Calendar cale = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        cale = Calendar.getInstance();
        cale.setTime(format.parse(date));
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, num + 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return format.format(cale.getTime());
    }

    //上个月 第一天
    public static String getLastMonth() {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-01");
        return timeFormatter.format(today);
    }

    // 上月最后一天
    public static String getLastMonthLastDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.add(Calendar.MONTH, -1);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 检查是否是闰年
     */
    public static boolean run(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static String validate(String dateStr) {
        String msg = "";
        boolean flag = true;
        // 若不符合规则将值改为false
        String year = "[0-9]{4}";
        // 年
        String month = "[0-9]||0[0-9]||1[12]";
        // 月
        String[] data = new String[3];
        String str = dateStr;
        // 输入的字符串,时间必须是8位数字的时间格式
        if (dateStr.matches("[0-9]+") && dateStr.length() == 8) {
            data[0] = dateStr.substring(0, 4);
            data[1] = dateStr.substring(4, 6);
            data[2] = dateStr.substring(6, 8);
        } else {
            throw new ApplicationException("请传入指定日期格式，如：20190302");
        }
        // 最基本的检查格式 begin
        if (!data[0].matches(year)) {
            msg += "年不对;";
            flag = false;
        }
        if (!data[1].matches(month)) {
            msg += "月不对;";
            flag = false;
        }
        // 天
        String day = "[0-9]||[0-2][0-9]||3[01]";
        if (!data[2].matches(day)) {
            msg += "日不对;";
            flag = false;
        }
        // end
        int _year = 0;
        _year = Integer.valueOf(data[0]);
        boolean run = run(_year);
        // run 为true是闰年否则是 非闰年
        if (run) {
            // 闰年
            if (data[1].matches("0[2]||2")) {
                // 这里是闰年的2月
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-9]")) {
                    flag = false;
                    msg += "2月份的天数不对;";
                }
            }
        } else {
            // 非闰年
            if (data[1].matches("0[2]||2")) {
                // 这里是平年的2月
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-8]")) {
                    flag = false;
                    msg += "2月份的天数不对;";
                }
            }
        }
        // 下面判断除了2月份的大小月天数
        if (data[1].matches("0[13578]||[13578]||1[02]")) {
            // 这里是大月
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||3[01]")) {
                flag = false;
                msg += data[2] + " 天数不对;";
            }
        } else if (data[1].matches("0[469]||[469]||11")) {
            // 这里是小月
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||30")) {
                flag = false;
                msg += data[2] + " 天数不对;";
            }
        }
        return flag ? "日期格式正确" : msg;
    }

    public static boolean isValidDate(String str, String formatStr) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
