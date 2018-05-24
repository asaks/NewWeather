package com.asaks.newweather.db;

import android.arch.persistence.room.TypeConverter;

import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DbTypeConverters
{
    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromListWeatherDesc( List<WeatherDay.WeatherDesc> lst )
    {
        return gson.toJson( lst );
    }

    @TypeConverter
    public static List<WeatherDay.WeatherDesc> toListWeather(String data )
    {
        if ( data == null )
            return Collections.emptyList();

        Type listType = new TypeToken<List<WeatherDay.WeatherDesc>>(){}.getType();

        return gson.fromJson( data, listType );
    }

    @TypeConverter
    public static String fromForecast(WeatherForecast forecast)
    {
        if ( forecast != null )
            return gson.toJson( forecast.getWeatherItems() );

        return "";
    }

    @TypeConverter
    public static WeatherForecast toForecast( String data )
    {
        WeatherForecast forecast = new WeatherForecast();

        if ( data == null )
            return forecast;

        Type listType = new TypeToken<List<WeatherDay>>(){}.getType();

        forecast.setWeatherItems((List<WeatherDay>) gson.fromJson( data, listType ));

        return forecast;
    }
}
