package com.anthony.myweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anthonyo on 22/5/2015.
 */
public class Utilities {
    private final String DEGREE="\u00B0";

    public String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedLow + DEGREE+" | " + roundedHigh+DEGREE;
        return highLowStr;
    }

    public String getTime(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM hh:mm");
        return format.format(date).toString();
    }

    public String getDay(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        //Date date = new Date(time * 1000);
        Calendar c1=Calendar.getInstance();
        c1.setTime(new Date(time*1000));
        Calendar today= Calendar.getInstance();
        today.setTime(new Date(System.currentTimeMillis()));
        if(c1.get(Calendar.YEAR)==today.get(Calendar.YEAR)) {

            if (c1.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                return "Today";
            } else if (c1.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) == 1) {
                return "Tomorrow";
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("E");

        return dateFormat.format(c1.getTime()).toString();
    }

    public String getDate(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
        return format.format(date).toString();
    }


    public int getIconImgSrc(String icon){
        switch (icon){
            case "01d":
                return  R.mipmap.clearsky_d;
                //break;
            case "01n":
                return  R.mipmap.clearsky_n;
                //break;
            case "02d":
                return  R.mipmap.fewclouds_d;
                //break;
            case "02n":
                return  R.mipmap.fewclouds_n;
               // break;
            case "03n":case"03d":
                return  R.mipmap.scatteredclouds;
                //break;
            case"04d":
                return  R.mipmap.fewclouds_d;
                //break;
            case"04n":
                return  R.mipmap.fewclouds_n;
               // break;
            case"09d":case "09n":
                return  R.mipmap.showerrains;
               // break;
            case"10d":
                return  R.mipmap.rain_d;
               // break;
            case"10n":
                return  R.mipmap.rain_n;
                //break;
            case"11d":
                return  R.mipmap.thunderstorm_d;
               //break;
            case"11n":
                return  R.mipmap.thunderstorm_n;
               // break;
            case"13d":
                return  R.mipmap.snow;
               // break;
            case"13n":
                return  R.mipmap.snow;
               // break;
            default:
                return  R.mipmap.na;
               // break;
        }
    }
}
