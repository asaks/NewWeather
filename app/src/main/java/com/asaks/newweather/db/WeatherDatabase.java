package com.asaks.newweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.asaks.newweather.weather.WeatherDay;

@Database(entities = {WeatherDay.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase
{
    public abstract WeatherDAO weatherDayDao();
}
