package com.asaks.newweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asaks.newweather.weather.WeatherForecast;

@Dao
public interface ForecastDAO
{
    @Query("select * from weather_forecast")
    WeatherForecast getForecast();

    @Update
    void update( WeatherForecast forecast );

    @Delete
    void delete( WeatherForecast forecast );
}
