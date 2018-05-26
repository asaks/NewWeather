package com.asaks.newweather.db;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.asaks.newweather.GlobalMethodsAndConstants;
import com.asaks.newweather.weather.WeatherDay;

import java.util.List;

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
        //TODO пока выполняется деструктивная миграция
        weatherDatabase = Room.databaseBuilder( this, WeatherDatabase.class, "weather" )
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public static AppDatabase getInstance()
    {
        return instance;
    }

    public WeatherDatabase getWeatherDatabase()
    {
        return weatherDatabase;
    }

    public WeatherDay getCurrentWeather()
    {
        List<WeatherDay> days = weatherDatabase.weatherDayDao().getCurrentWeather(GlobalMethodsAndConstants.IDD_CURRENT_WEATHER);
        WeatherDay weatherDay = null;

        if ( !days.isEmpty() )
        {
            weatherDay = days.get(0);

            weatherDay.setCoords( weatherDatabase.weatherDayDao().getCoords( weatherDay.getIdCoords() ) );
            weatherDay.setTemp( weatherDatabase.weatherDayDao().getTemp( weatherDay.getIdTemp() ) );
            weatherDay.setWind( weatherDatabase.weatherDayDao().getWind( weatherDay.getIdWind() ) );
            weatherDay.setSys( weatherDatabase.weatherDayDao().getSys( weatherDay.getIdSys() ) );

            weatherDay.setWeatherDesc( weatherDatabase.weatherDayDao().getWeatherDesc( weatherDay.getIdd() ) );

            List<WeatherDay.WeatherDesc> lstWeatherDesc =
                    weatherDatabase.weatherDayDao().getWeatherDesc( weatherDay.getIdd() );

            if ( lstWeatherDesc != null )
                weatherDay.setWeatherDesc( lstWeatherDesc );
        }

        return weatherDay;
    }
}
