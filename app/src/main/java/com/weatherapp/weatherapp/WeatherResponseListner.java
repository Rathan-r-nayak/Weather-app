package com.weatherapp.weatherapp;

public interface WeatherResponseListner {
    void onWeatherResponseReceived(String response);
    void onWeekWeatherResponseReceived(String response);

}
