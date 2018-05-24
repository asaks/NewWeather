package com.asaks.newweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.asaks.newweather.ApplicationSettings;
import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;

@Database(entities = {WeatherDay.class/*, WeatherForecast.class*/, ApplicationSettings.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase
{
    public abstract WeatherDAO weatherDayDao();
    //public abstract ForecastDAO forecastDao();
    public abstract SettingsDAO settingsDao();
}
