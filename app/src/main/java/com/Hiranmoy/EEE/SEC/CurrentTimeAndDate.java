package com.Hiranmoy.EEE.SEC;

import java.util.Calendar;
import java.util.Date;

public class CurrentTimeAndDate {
    public static String get(){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR)+10000;
        int month = calendar.get(Calendar.MONTH) + 1+100; // Month is zero-based, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH)+100;
        int hour = calendar.get(Calendar.HOUR_OF_DAY)+100;
        int minute = calendar.get(Calendar.MINUTE)+100;
        int second = calendar.get(Calendar.SECOND)+100;

// Combine the date and time values into a single string
        return Integer.toString(year)+ month + day + hour + minute+ second;
    }
}
