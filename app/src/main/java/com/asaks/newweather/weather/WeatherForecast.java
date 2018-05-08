package com.asaks.newweather.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий прогноз погоды
 */
public class WeatherForecast
{
    @SerializedName("list")
    private List<WeatherDay> weatherItems;

    public WeatherForecast()
    {
        weatherItems = new ArrayList<>();
    }

    public WeatherForecast( List<WeatherDay> items )
    {
        this.weatherItems = items;
    }

    public List<WeatherDay> getWeatherItems()
    {
        return weatherItems;
    }
}
