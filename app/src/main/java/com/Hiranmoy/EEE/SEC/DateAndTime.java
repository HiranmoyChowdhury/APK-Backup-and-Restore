package com.Hiranmoy.EEE.SEC;


import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DateAndTime {
    public static String get(){
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format the date and time as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateString = dateFormat.format(currentDate);
        return currentDateString;
    }

// Output the date and time

}
