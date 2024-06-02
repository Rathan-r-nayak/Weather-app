package com.weatherapp.weatherapp;

public class WeatherForecast {
    private String day;
    private String temperature;

    public WeatherForecast(String day, String temperature) {
        this.day = day;
        this.temperature = temperature;
    }

    public String getDay() {
        return day;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
