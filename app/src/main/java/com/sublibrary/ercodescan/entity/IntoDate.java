package com.sublibrary.ercodescan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/12/24.
 */

public class IntoDate implements Serializable {

    private String date;

    private String day;

    private String hours;

    private String minutes;

    private String month;

    private String seconds;

    private String time;

    private String timezoneOffset;

    private String year;



    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setDay(String day){
        this.day = day;
    }
    public String getDay(){
        return this.day;
    }
    public void setHours(String hours){
        this.hours = hours;
    }
    public String getHours(){
        return this.hours;
    }
    public void setMinutes(String minutes){
        this.minutes = minutes;
    }
    public String getMinutes(){
        return this.minutes;
    }
    public void setMonth(String month){
        this.month = month;
    }
    public String getMonth(){
        return this.month;
    }
    public void setSeconds(String seconds){
        this.seconds = seconds;
    }
    public String getSeconds(){
        return this.seconds;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
    public void setTimezoneOffset(String timezoneOffset){
        this.timezoneOffset = timezoneOffset;
    }
    public String getTimezoneOffset(){
        return this.timezoneOffset;
    }
    public void setYear(String year){
        this.year = year;
    }
    public String getYear(){
        return this.year;
    }
    }
