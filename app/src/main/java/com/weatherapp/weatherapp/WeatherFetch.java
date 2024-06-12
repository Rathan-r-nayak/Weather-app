package com.weatherapp.weatherapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFetch extends AsyncTask<Void,Void,String>
{
    private static final String API_KEY = "";
    private static final String CITYFinal = "Udupi";
    private WeatherResponseListner listener;
    MainActivity mainActivity;
    String city;
    public WeatherFetch(WeatherResponseListner listener,MainActivity mainActivity,String city) {
        this.listener = listener;
        this.city = city;
        this.mainActivity = mainActivity;
    }
    @Override
    protected String doInBackground(Void... voids) {
        try
        {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
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
//                    System.out.println(response.toString());
                return response.toString();
            }
            else
            {
//                    System.out.println("error code :"+responseCode+" error");
                city = CITYFinal;
                mainActivity.fetchWeather();
            }
        }
        catch (Exception e)
        {
//                System.out.println(e);
            return e.toString();
        }
        return "";
    }

    @Override
    public void onPostExecute(String result)
    {
//            System.out.println("Weather API Response: " + result);
        if (listener != null) {
            listener.onWeatherResponseReceived(result);
        }
    }
}
