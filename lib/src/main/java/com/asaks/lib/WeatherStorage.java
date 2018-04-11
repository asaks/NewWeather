package com.asaks.lib;
/**
 * Created by asaks on 11.04.18.
 */

/**
 * Класс, содержащий информацию о городе и погоде в нем
 */

public class WeatherStorage
{
    private InfoCity infoCity;
    private WeatherData weatherData;

    public WeatherStorage()
    {
    }

    public WeatherStorage(InfoCity infoCity, WeatherData weatherData)
    {
        this.infoCity = infoCity;
        this.weatherData = weatherData;
    }

    //! Возвращает информацию о городе
    public InfoCity getInfoCity()
    {
        return infoCity;
    }

    //! Записывает информацию о городе
    public void setInfoCity(InfoCity infoCity)
    {
        this.infoCity = infoCity;
    }

    //! Возвращает информацию о погодных условиях
    public WeatherData getWeatherData()
    {
        return weatherData;
    }

    //! Записывает информацию о погодных условиях
    public void setWeatherData(WeatherData weatherData)
    {
        this.weatherData = weatherData;
    }

    //! Возвращает ID города OpenWeatherMap
    public int getIDcity()
    {
        return  infoCity.getIDcity();
    }

    //! Возвращает название города
    public String getCityName()
    {
        return infoCity.getCityName();
    }

    //! Возвращает широту
    public double getLatitude()
    {
        return infoCity.getLatitude();
    }

    //! Возвращает долготу
    public double getLongitude()
    {
        return infoCity.getLongitude();
    }

    //! Возвращает текущую температуру в кельвинах
    public double getCurrentTemp()
    {
        return weatherData.getCurrentTemp();
    }

    //! Возвращает давление
    public double getPressure()
    {
        return weatherData.getPressure();
    }

    //! Возвращает влажность в процентах
    public double getHumidity()
    {
        return weatherData.getHumidity();
    }

    //! Возвращает минимальную температуру в этот день в кельвинах
    public double getTempMin()
    {
        return weatherData.getTempMin();
    }

    //! Возвращает максимальную температуру в этот день в кельвинах
    public double getTempMax()
    {
        return weatherData.getTempMax();
    }

    //! Возвращает
    public double getSeaLevel()
    {
        return weatherData.getSeaLevel();
    }

    //! Возвращает
    public double getGroundLevel()
    {
        return weatherData.getGroundLevel();
    }

    //! Возвращает дату и время в виде количества секунд
    public int getDateTime()
    {
        return weatherData.getDateTime();
    }

    //! Возвращает скорость ветра в м/с
    public double getWindSpeed()
    {
        return weatherData.getWindSpeed();
    }

    //! Возвращает направление ветра в градусах
    public double getWindDeg()
    {
        return weatherData.getWindDeg();
    }

    //! Возвращает объем дождя за посление 3 часа
    public double getRain()
    {
        return weatherData.getRain();
    }

    //! Возвращает объем снега за посление 3 часа
    public double getSnow()
    {
        return weatherData.getSnow();
    }

    //! Возвращает процент облачности
    public double getClouds()
    {
        return weatherData.getClouds();
    }

    //! Возвращает описание погодных условий
    public String getWeatherDesc()
    {
        return weatherData.getWeatherDesc();
    }

    //! Возвращает описание состояния неба
    public String getSkyDesc()
    {
        return weatherData.getSkyDesc();
    }
}
