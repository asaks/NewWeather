package com.asaks.newweather.weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaks on 13.04.18.
 *
 * Класс, содержащий информацию о погоде на день
 */

public class WeatherDay
{
    //! Класс, содержащий сведения о температуре
    public class DayTemperature
    {
        // текущая температура
        double dCurrentTemp;
        // температурный минимум
        double dTempMin;
        // температурный максимум
        double dTempMax;
    }

    //! Класс, содержащий описание погодных условий
    public class WeatherDesc
    {
        // id погодных условий в OpenWeatherMap
        int id;
        // описание погодных условий
        String sDesc;
        // описание состояния неба
        String skyDesc;
        // иконка
        String sIcon;
    }

    // название города
    private String sCityName;
    // страна
    private String sCountry;
    // широта города
    private double dCityLat;
    // долгота города
    private double dCityLon;

    // температура
    private DayTemperature temp;

    // описание погодных условий
    private List<WeatherDesc> weatherDesc;

    // давление
    private double dPressure;
    // влажность
    private double dHumidity;
    // дата и время
    private long iDateTime;
    // скорость ветра
    private double dWindSpeed;
    // направление ветра, в градусах
    private double dWindDeg;

    // время рассвета
    private long iTimeDawn;
    // время заката
    private long iTimeSunset;

    public WeatherDay( DayTemperature temp, List<WeatherDesc> desc )
    {
        this.temp = temp;
        this.weatherDesc = desc;
    }

    public WeatherDay()
    {
        temp = new DayTemperature();
        weatherDesc = new ArrayList<WeatherDesc>();
    }


    // Функция заполнения тестовыми данными
    public void setTestData( String sCity, double dLat, double dLon, long iTimeUpd, double dCurrentTemp, double dPressure,
                                    double dHumidity, double dWindSpeed, double dWindDeg, String sWeatherDesc )
    {
        weatherDesc.add( new WeatherDesc() );

        this.sCityName = sCity;
        this.dCityLat = dLat;
        this.dCityLon = dLon;
        this.iDateTime = iTimeUpd;
        temp.dCurrentTemp = dCurrentTemp;
        this.dPressure = dPressure;
        this.dHumidity = dHumidity;
        this.dWindSpeed = dWindSpeed;
        this.dWindDeg = dWindDeg;
        weatherDesc.get(0).sDesc = sWeatherDesc;
    }

    ////////////////////////////геттеры////////////////////////////

    //! Возвращает URL иконки погоды
    public String getIconUrl()
    {
        return "http://openweathermap.org/img/w/" + weatherDesc.get(0).sIcon + ".png";
    }

    //! Возвращает название города
    public String getCityName()
    {
        return sCityName;
    }

    //! Возвращает название страны
    public String getCountry()
    {
        return sCountry;
    }

    //! Возвращает широту
    public double getLatitude()
    {
        return dCityLat;
    }

    //! Возвращает долготу
    public double getLongitude()
    {
        return dCityLon;
    }

    //! Возвращает текущую температуру в Кельвинах
    public double getCurrentTemp()
    {
        return temp.dCurrentTemp;
    }

    //! Возвращает описание погодных условий
    public String getWeatherDesc()
    {
        return weatherDesc.get(0).sDesc;
    }

    //! Возвращает давление
    public double getPressure()
    {
        return dPressure;
    }

    //! Возвращает влажность в процентах
    public double getHumidity()
    {
        return dHumidity;
    }

    //! Возвращает дату и время в виде количества секунд
    public long getDateTime()
    {
        return iDateTime;
    }

    //! Возвращает скорость ветра в м/с
    public double getWindSpeed()
    {
        return dWindSpeed;
    }

    //! Возвращает направление ветра в градусах
    public double getWindDeg()
    {
        return dWindDeg;
    }

    //! Возвращает время рассвета
    public long getTimeDawn()
    {
        return iTimeDawn;
    }

    //! Возвращает время заката
    public long getTimeSunset()
    {
        return iTimeSunset;
    }

    ////////////////////////////сеттеры////////////////////////////

    //! Записывает название города
    public void setCityName( String sCity )
    {
        this.sCityName = sCity;
    }

    //! Записывает широту города
    public void setCityLatitude( double dLat )
    {
        this.dCityLat = dLat;
    }

    //! Записывает долготу города
    public void setCityLongitude( double dLon )
    {
        this.dCityLon = dLon;
    }

    //! Записывает название страны
    public void setCountry( String sCountry )
    {
        this.sCountry = sCountry;
    }

    //! Записывает текущую температуру
    public void setCurrentTemp( double dCurrentTemp )
    {
        temp.dCurrentTemp = dCurrentTemp;
    }

    //! Записывает минимальную температуру на данный момент
    public void setTempMin(double dTempMin) {
        temp.dTempMin = dTempMin;
    }

    //! Записывает максимальную температуру на данный момент
    public void setTempMax(double dTempMax) {
        temp.dTempMax = dTempMax;
    }

    //! Записывает давление
    public void setPressure(double dPressure) {
        this.dPressure = dPressure;
    }

    //! Записывает влажность в процентах
    public void setHumidity(double dHumidity) {
        this.dHumidity = dHumidity;
    }

    //! Записывает текущую дату и время в секундах
    public void setDateTime(long iDateTime) {
        this.iDateTime = iDateTime;
    }

    //! Записывает скорость ветра в м/с
    public void setWindSpeed(double dWindSpeed) {
        this.dWindSpeed = dWindSpeed;
    }

    //! Записывает направление ветра в градусах
    public void setWindDeg(double dWindDeg) {
        this.dWindDeg = dWindDeg;
    }

    //! Записывает время рассвета
    public  void setTimeDawn( long iTimeDawn )
    {
        this.iTimeDawn = iTimeDawn;
    }

    //! Записывает время заката
    public void setTimeSunset( long iTimeSunset )
    {
        this.iTimeSunset = iTimeSunset;
    }
}
