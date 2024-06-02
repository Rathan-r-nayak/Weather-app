package com.weatherapp.weatherapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeekWeatherFetch extends AsyncTask<Void,Void,String> {
    private static final String API_KEY = "25253fe32a908424579e155ad66f1490";
    private static final String CITYFinal = "Udupi";
    String city;
    WeatherResponseListner listner;
    public WeekWeatherFetch(WeatherResponseListner listner,String city)
    {
        this.city = city;
        this.listner = listner;

    }

    @Override
    protected String doInBackground(Void... voids)
    {
        try
        {
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=" + API_KEY;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while((line = reader.readLine())!=null)
                {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }

        }
        catch (Exception e)
        {
            return e.toString();
        }
        return "";
    }



    @Override
    public void onPostExecute(String result)
    {
        if(listner != null)
        {
            listner.onWeekWeatherResponseReceived(result);
        }
    }
}
