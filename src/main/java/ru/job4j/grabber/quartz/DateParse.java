package ru.job4j.grabber.quartz;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DateParse {
    String toDayDate = null;
    String toDayTime = null;
    String yesterdayTime = null;
    String yesterdayDate = null;
    String normalDate = null;
    String normalTime = null;
    Boolean toDay = false;
    Boolean yesterday = false;
    private static HashMap<String, Integer> map = new HashMap<>() {
        {
            put("янв", 1);
            put("фев", 2);
            put("мар", 3);
            put("апр", 4);
            put("май", 5);
            put("июн", 6);
            put("июл", 7);
            put("авг", 8);
            put("сен", 9);
            put("окт", 10);
            put("ноя", 11);
            put("дек", 12);
        }

    };
    /*
    this method determines the date type
     */
    public void parseString(String dateTime) throws IOException {
        String[] strings = dateTime.toLowerCase().split(",");
           if (strings.length > 1) {
               if (strings[0].equals("сегодня")) {
                   toDay = true;
                   yesterday = false;
                   toDayDate = strings[0];
                   toDayTime = strings[1];
                   parseToDay();
               } else if (strings[0].equals("вчера")) {
                   yesterdayDate = strings[0];
                   yesterdayTime = strings[1];
                   toDay = false;
                   yesterday = true;
                   parseYesterday();
               } else {
                   normalDate = strings[0];
                   normalTime = strings[1];
                   toDay = false;
                   yesterday = false;
                   parseNormalDay(normalDate, normalTime);
               }
           } else {
               throw new IOException("the date is incorrect");
           }

       }

       /*
        this method return Date now
        */

    private Timestamp parseToDay() {
        System.out.println(Timestamp.valueOf(parseDay(toDayTime, toDay)));
        return Timestamp.valueOf(parseDay(toDayTime, toDay));
    }
    /*
        this method return Date minus 1 Day
    */
    private Timestamp parseYesterday() {
        System.out.println(Timestamp.valueOf(parseDay(yesterdayTime, yesterday).minusDays(1)));
       return Timestamp.valueOf(parseDay(yesterdayTime, yesterday).minusDays(1));
    }

    private int compareMonth(String month) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(month)) {
                return entry.getValue();
            }
        } return 0;
    }
    /*
        this common method return Date now
    */
    private LocalDateTime parseDay(String time, boolean rsl) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int year, month, dayOfMonth;
        String[] parseHourMinute = time.replaceAll(" ", "").split(":");
        if (rsl) {
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            return LocalDateTime.of(year, month, dayOfMonth, Integer.parseInt(parseHourMinute[0]), Integer.parseInt(parseHourMinute[1]));
        }
        return null;
    }
    /*
        this method returns a Date other than today and yesterday
    */
    private Timestamp parseNormalDay(String date, String time) {
        int year, month, dayOfMonth;
        String[] parseHourMinute = time.replaceAll(" ", "").split(":");
        String[] parseYearMonthDay = date.replaceAll("\\p{Punct}", "").toLowerCase().split(" ");
        year = 2000 + Integer.parseInt(parseYearMonthDay[2]);
        String monthParse = parseYearMonthDay[1];
        month = compareMonth(monthParse);
        dayOfMonth = Integer.parseInt(parseYearMonthDay[0]);
        System.out.println(Timestamp.valueOf(LocalDateTime.of(year, month, dayOfMonth, Integer.parseInt(parseHourMinute[0]), Integer.parseInt(parseHourMinute[1]))));
        return Timestamp.valueOf(LocalDateTime.of(year, month, dayOfMonth, Integer.parseInt(parseHourMinute[0]), Integer.parseInt(parseHourMinute[1])));

    }

    public static void main(String[] args) throws IOException {
        //String date = "2 дек 21, 22:29";
       // String date = ", 22:29";
        String date = "2 янв 19, 12:29";
       //String date = "Сегодня, 12:30";
       //String date = "вчера, 12:59";
        DateParse dateParse = new DateParse();
        dateParse.parseString(date);



    }
}
