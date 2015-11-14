package com.anthony.myweather;

/**
 * Created by anthonyo on 22/5/2015.
 */
public class Weather {
    private String icon="";
    private String date="";
    private String day="";
    private String maxMin_Temp="";
    private double day_Temp=0.0;
    private double night_Temp=0.0;
    private int humidity=0;
    private double windSpeed=0.0;
    private String windDirection="";
    private String desc="";
    private String locationName="";

        public Weather(){

        }

        public Weather(String icon, String date, String day, String maxMin_Temp,double day_Temp, double night_Temp, int humidity, double windSpeed, String windDirection, String desc, String locationName){
            super();
            this.icon = icon;
            this.date = date;
            this.day=day;
            //this.temperature=temperature;
            this.maxMin_Temp = maxMin_Temp;
            this.day_Temp=day_Temp;
            this.night_Temp=night_Temp;
            this.humidity=humidity;
            this.windSpeed=windSpeed;
            this.windDirection = windDirection;
            this.desc=desc;
            this.locationName=locationName;
        }

    public void setIcon(String icon){
        this.icon=icon;
    }

    public  void setDate(String date){
        this.date=date;
    }

    public  void setDay(String day){
        this.day=day;
    }

    public  void setMaxMin_Temp(String  maxMin_Temp){
        this.maxMin_Temp=maxMin_Temp;
    }

    public  void setDay_Temp(double day_Temp){
        this.day_Temp=day_Temp;
    }

    public  void setHumidity(int humidity){
        this.humidity=humidity;
    }

    public  void setWindSpeed(double windSpeed){
        this.windSpeed=windSpeed;
    }

    public void setDesc(String desc){
        this.desc=desc;
    }

    public void setLocationName(String locationName){
        this.locationName=locationName;
    }

    public String getIcon(){
        return this.icon;
    }

    public  String getDate(){
        return this.date;
    }

    public  String getDay(){
        return this.day;
    }

    public  String getMaxMin_Temp(){
        return this.maxMin_Temp;
    }

    public  double getDay_Temp(){
        return this.day_Temp;
    }

    public  int getHumidity(){
        return this.humidity;
    }

    public  double getWindSpeed(){
        return this.windSpeed;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getLocationName(){
        return this.locationName;
    }
}
