package com.example.assignment3;

import android.os.Build;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class DateTimeFormat {
    // convert date in DatePicker to
    public static String getDateStr(DatePicker DoB) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getTimeStr(TimePicker timePicker) {
        String hour = String.valueOf(timePicker.getHour());
        String minute = String.valueOf(timePicker.getMinute());
        String second = "00";

        String timeStr = "1970-01-01T" + hour + ":" + minute + ":" + second + "+08:00";
        return timeStr;
    }

    // convert from 01 Oct 2017
        public static String dateFormatConvert(String date) {
            Map<String, String> monthMap = new HashMap<String, String>(){{
               put("Jan", "01");
               put("Feb", "02");
               put("Mar", "03");
               put("Apr", "04");
               put("May", "05");
               put("Jun", "06");
               put("Jul", "07");
               put("Aug", "08");
               put("Sep", "09");
               put("Sept", "09");
               put("Oct", "10");
               put("Nov", "11");
               put("Dec", "12");
            }};
    
            String[] digits = date.split(" ");
            String year = digits[2];
            String month = monthMap.get(digits[1]);
            String day = digits[0];
    
            String output = year + "-" + month + "-" + day + "T00:00:00+08:00";
            return output;
        }
}
