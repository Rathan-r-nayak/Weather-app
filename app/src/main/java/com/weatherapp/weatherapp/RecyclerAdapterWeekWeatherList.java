package com.weatherapp.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterWeekWeatherList extends RecyclerView.Adapter<RecyclerAdapterWeekWeatherList.ForecastViewHolder> {
    List<WeatherForecast> forecastList;

    public RecyclerAdapterWeekWeatherList(List<WeatherForecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public RecyclerAdapterWeekWeatherList.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_week_weather, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWeekWeatherList.ForecastViewHolder holder, int position) {
        WeatherForecast forecast = forecastList.get(position);
        holder.week_temperature.setText(String.valueOf(forecast.getTemperature()));
        holder.week_day.setText(forecast.getDay());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }


    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView week_day,week_temperature;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            week_day = itemView.findViewById(R.id.week_day);
            week_temperature = itemView.findViewById(R.id.week_temperature);
        }
    }

}
