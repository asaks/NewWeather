package com.asaks.newweather.weather;

import java.util.List;

/**
 * Класс, содержащий прогноз погоды
 */
public class WeatherForecast
{
    private List<WeatherDay> weatherItems;

    public WeatherForecast( List<WeatherDay> items )
    {
        this.weatherItems = items;
    }

    public List<WeatherDay> getWeatherItems()
    {
        return weatherItems;
    }
}
