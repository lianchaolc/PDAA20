package com.sublibrary.ercodescan.entity;

/**
 * Created by Administrator on 2021/1/5.
 * "date":10,"day":6,"hours":0,"minutes":0,"month":9,"
 * seconds":0,"time":1602259200000,"timezoneOffset":-480,"year":120
 */
///  实体类代码  一个时间的对象 以前 是个String  现在修改  对象格式

public class OutData {
    public String getDate() {
        return date;
    }

    public OutData() {
    }

    public OutData(String date, String day, String hours, String minutes, String month, String seconds, String time, String timezoneOffset, String year) {
        this.date = date;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;

        this.month = month;
        this.seconds = seconds;
        this.time = time;
        this.timezoneOffset = timezoneOffset;
        this.year = year;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String date;

    private String day;

    private String hours;

    private String minutes;

    private String month;

    private String seconds;

    private String time;

    private String timezoneOffset;

    private String year;
}
