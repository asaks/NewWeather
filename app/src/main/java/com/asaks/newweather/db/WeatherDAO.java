package com.asaks.newweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asaks.newweather.weather.WeatherDay;

import java.util.List;

@Dao
public interface WeatherDAO
{
    @Query("select * from current_weather")
    List<WeatherDay> getCurrentWeather();

    @Query("select * from current_weather where cityID = :id")
    WeatherDay getCurrentWeatherByCityId( long id );

    @Insert
    void insert( WeatherDay weatherDay );

    @Update
    void update( WeatherDay weatherDay );

    @Delete
    void delete( WeatherDay weatherDay );

    @Query("select exists (select cityID from current_weather where cityID = :id)")
    boolean isExist( long id );
}
