package com.weatherapp.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements WeatherResponseListner {
//    TextView responseWeakData;
    RecyclerView recyclerView;
    RecyclerAdapterWeekWeatherList recyclerAdapterWeekWeatherList;
    List<WeatherForecast> forecastList;



    private static final String TAG = "MainActivity";
    private static final String API_KEY = "";
    private static final String CITYFinal = "Udupi";
    String city = "Udupi";
    TextView temperatureText,coordText,tempDetText,cityText;
    EditText cityName;
    ImageButton searchCity;
    ImageView weather_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);




        temperatureText = findViewById(R.id.temperature);
        coordText = findViewById(R.id.coordText);
        tempDetText = findViewById(R.id.tempDetText);
        cityText = findViewById(R.id.cityText);
        cityName = findViewById(R.id.cityName);
        searchCity = findViewById(R.id.searchCity);
        weather_img = findViewById(R.id.weather_img);
//        dataWText = findViewById(R.id.dataW);
//        responseWeakData = findViewById(R.id.responseWeakData);


        //getting the cityname
        searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                city = cityName.getText().toString();
                city = city.trim();
                fetchWeather();
            }
        });
       fetchWeather();

    }

    //calling fetchWeather() method to get the city weather details
    public void fetchWeather() {
        WeatherFetch weatherFetch = new WeatherFetch(this,this,city);
        weatherFetch.execute();

        WeekWeatherFetch weekWeatherFetch = new WeekWeatherFetch(this,city);
        weekWeatherFetch.execute();
    }

    //setting values in the xml from the response recieved
    @Override
    public void onWeatherResponseReceived(String response) {
        // Handle the response here
        System.out.println(response);
//        dataWText.setText(response);

        try
        {
            JSONObject jsonRes = new JSONObject(response);
            DecimalFormat df = new DecimalFormat("#.##");

            //coord
            JSONObject coord = jsonRes.getJSONObject("coord");
            double longitude = coord.getDouble("lon");
            double latitude = coord.getDouble("lat");
            String coordStr = "Logitude : "+df.format(longitude)+ "\nLatitude : "+df.format(latitude);

            JSONArray weatherArray = jsonRes.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            String sky = weather.getString("description");

            //main
            JSONObject mainData = jsonRes.getJSONObject("main");
            double temperature = mainData.getDouble("temp") - 273.15;
            double feelsLike = mainData.getDouble("feels_like") - 273.15;
            double minTemp = mainData.getDouble("temp_min") - 273.15;
            double maxTemp = mainData.getDouble("temp_max") - 273.15;
            int humidity = mainData.getInt("humidity");
            String tempDetails = "Feels    : "+df.format(feelsLike)+"°C"+"\nMin Temp : "+df.format(minTemp)+"°C"+"\nMax Temp : "+df.format(maxTemp)+"°C"+"\nHumidity : "+humidity;

            //sys
            JSONObject locData = jsonRes.getJSONObject("sys");
            String country = locData.getString("country");


            city = jsonRes.getString("name");



            coordText.setText(coordStr);
            tempDetText.setText(tempDetails);
            cityText.setText(city+", "+country);



            LocalTime currentTime = LocalTime.now();
            LocalTime startTime =  LocalTime.of(6,0);
            LocalTime endTime = LocalTime.of(19,0);
            if(sky.equals("clear sky"))
            {
                if((currentTime.isAfter(startTime) && currentTime.isBefore(endTime)))
                {
                    weather_img.setImageResource(R.drawable.sun);

                }
                else
                {
                    weather_img.setImageResource(R.drawable.moon);
                }
            }
            else if(sky.equals("overcast clouds"))
            {
                weather_img.setImageResource(R.drawable.cloud);
            }
            else if(sky.equals("Rain"))
            {
                weather_img.setImageResource(R.drawable.rain);
            }
            else
            {
                weather_img.setImageResource(R.drawable.sun);
            }
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(1000); // 1 second
            weather_img.startAnimation(fadeIn);


        }
        catch(Exception e)
        {
            System.out.println("Exception "+e);
        }


    }

    @Override
    public void onWeekWeatherResponseReceived(String response) {
        recyclerView = findViewById(R.id.recyclerview);
        forecastList = new ArrayList<>();
        recyclerAdapterWeekWeatherList = new RecyclerAdapterWeekWeatherList(forecastList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapterWeekWeatherList);

        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println(response);
//        responseWeakData.setText(response);

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray listArray = jsonObject.getJSONArray("list");
            for(int i=0;i<listArray.length();i++)
            {
                JSONObject listObject = listArray.getJSONObject(i);

                JSONObject mainData = listObject.getJSONObject("main");
                double tempData = mainData.getDouble("temp") - 273.0;

                String date = listObject.getString("dt_txt");

                WeatherForecast weatherForecast = new WeatherForecast(date,df.format(tempData)+" °C");
                forecastList.add(weatherForecast);
            }
            recyclerAdapterWeekWeatherList.notifyDataSetChanged();



        }
        catch(Exception e)
        {
            System.out.println("error :->"+e.toString());
        }



    }


}



