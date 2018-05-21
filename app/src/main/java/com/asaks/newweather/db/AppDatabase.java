package com.asaks.newweather.db;

import android.app.Application;
import android.arch.persistence.room.Room;

public class AppDatabase extends Application
{
    private static AppDatabase instance;

    private WeatherDatabase weatherDatabase;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        //TODO пока разрешил выполнять в UI-потоке
        weatherDatabase = Room.databaseBuilder( this, WeatherDatabase.class, "weather" )
                .allowMainThreadQueries().build();
    }

    public static AppDatabase getInstance()
    {
        return instance;
    }

    public WeatherDatabase getWeatherDatabase()
    {
        return weatherDatabase;
    }
}
