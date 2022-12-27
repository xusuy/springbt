package com.xusy.springbt.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author xusy
 * @date 2022/7/13 20:38
 * @description 时间测试
 */
public class DateTest {

    @Test
    public void test1() throws ParseException {
        Date nowDate = new Date();
        System.out.println(DateUtil.offsetDay(nowDate, 1));
        System.out.println("=========================");
        System.out.println(DateUtil.offsetDay(nowDate, 0));
        System.out.println("=========================");
        Date beginDate = dateSetTime(DateUtil.year(nowDate),
                DateUtil.month(nowDate), DateUtil.dayOfMonth(nowDate), 8, 30);
        Date endDate = DateUtils.setHours(beginDate, 17);
        endDate = DateUtils.setMinutes(endDate, 30);
        DateTime dateTime = DateUtil.offsetDay(nowDate, 7);
        //hutool只能间隔整数小时
        List<DateTime> dateTimeList = DateUtil.rangeToList(beginDate, endDate, DateField.HOUR_OF_DAY, 1);
//        dateTimeList.forEach(x -> System.out.println(DateUtil.format(x, "yyyy-MM-dd HH:mm")));

        //获取固定间隔时刻集合
        List<LocalTime> intervalTimeList = getIntervalTimeList("08:30", "17:30", 90);
        intervalTimeList.forEach(x -> System.out.println(x.format(DateTimeFormatter.ofPattern("HH:mm"))));
    }

    /**
     * 给日期设置指定时间、分钟
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @return
     */
    public static Date dateSetTime(Integer year, Integer month, Integer day, Integer hour,
                                   Integer minute) {
        Calendar cal = Calendar.getInstance();
        //Calendar.HOUR(小时)
        //Calendar.DATE(天)
        cal.set(year, month, day, hour, minute);
        return cal.getTime();
    }


    /**
     * 获取固定间隔时刻集合
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔(单位：分钟)
     */
    public static List<LocalTime> getIntervalTimeList(String start, String end, int interval) {
        List<LocalTime> result = new ArrayList<>();
        //格式化成日期类型
        LocalTime startDate = LocalTime.parse(start);
        LocalTime endDate = LocalTime.parse(end);
        //循环按固定间隔，存入集合中
        while (!startDate.isAfter(endDate)) {
            result.add(startDate);
            startDate = startDate.plusMinutes(interval);
            if (startDate.equals(LocalTime.MIN)) {
                result.add(endDate);
                break;
            }
        }
        return result;
    }

}
