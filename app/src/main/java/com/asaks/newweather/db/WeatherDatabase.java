package com.asaks.newweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.asaks.newweather.ApplicationSettings;
import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;

@Database(entities = {WeatherDay.class, WeatherDay.DayTemperature.class, WeatherDay.Coords.class,
        WeatherDay.Wind.class, WeatherDay.Sys.class, WeatherDay.WeatherDesc.class/*, WeatherForecast.class*/,
        ApplicationSettings.class}, version = 2)
public abstract class WeatherDatabase extends RoomDatabase
{
    public abstract WeatherDAO weatherDayDao();
    public abstract SettingsDAO settingsDao();
}
