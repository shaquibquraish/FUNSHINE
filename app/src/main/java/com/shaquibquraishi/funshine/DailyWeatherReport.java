package com.shaquibquraishi.funshine;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyWeatherReport implements Serializable {
    private String cityName;
    private String country;
    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private String weather;
    private String formattedDate;

    public DailyWeatherReport(String cityName, String country, int currentTemp, int maxTemp, int minTemp, String weather, String rawDate) {
        this.cityName = cityName;
        this.country = country;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.formattedDate = rawDateToPretty(rawDate);
    }
    public String rawDateToPretty(String rawDate){
        //String[] str=rawDate.substring(0,10).split("-");
        String dayOfTheWeek="";
        String day="";
        String monthString="";
        String monthNumber="";
        String year="";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        try {
            Date date = format.parse(rawDate);
            dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            day          = (String) DateFormat.format("dd",   date); // 20
            monthString  = (String) DateFormat.format("MMM",  date); // Jun
            monthNumber  = (String) DateFormat.format("MM",   date); // 06
            year         = (String) DateFormat.format("yyyy", date); // 2013

        } catch (ParseException e) {
            e.printStackTrace();
        }


     return dayOfTheWeek+","+monthString+" "+day ;


    }
    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public String getWeather() {
        return weather;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
