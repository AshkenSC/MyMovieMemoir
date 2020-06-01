package com.example.assignment3;

import android.widget.DatePicker;

public class DateFormat {
    // convert date in DatePicker to
    public static String setDoBStr(DatePicker DoB) {
        String year = String.valueOf(DoB.getYear());
        String month = String.valueOf(DoB.getMonth() + 1);
        String day = String.valueOf(DoB.getDayOfMonth());

        if(Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if(Integer.parseInt(day) < 10){
            day  = "0" + day ;
        }

        final String DoBStr = year + "-" + month + "-" + day;
        return DoBStr;
    }

    // add tail "T00:00:00+08:00" at the end of date string to fit the format of database
    public static String dateStrAddTail(String dateStr) {
        return dateStr + "T00:00:00+08:00";
    }
}
