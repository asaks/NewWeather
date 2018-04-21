package com.asaks.newweather.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaks on 13.04.18.
 *
 * Класс, содержащий информацию о погоде на день
 */

public class WeatherDay
{
    public class Coords
    {
        // широта города
        @SerializedName("lat")
        double dCityLat;
        // долгота города
        @SerializedName("lon")
        double dCityLon;
    }
    //! Класс, содержащий сведения о температуре
    public class DayTemperature
    {
        // текущая температура
        @SerializedName("temp")
        double temp;
        // температурный минимум
        @SerializedName("temp_min")
        double temp_min;
        // температурный максимум
        @SerializedName("temp_max")
        double temp_max;
        // давление
        @SerializedName("pressure")
        double dPressure;
        // влажность
        @SerializedName("humidity")
        double dHumidity;
    }

    //! Класс, содержащий описание погодных условий
    public class WeatherDesc
    {
        // id погодных условий в OpenWeatherMap
        int id;
        // описание группы погодных условий
        String main;
        // описание погодных условий
        String description;
        // иконка
        String icon;
    }

    public class Wind
    {
        // скорость ветра
        @SerializedName("speed")
        double dWindSpeed;
        // направление ветра, в градусах
        @SerializedName("deg")
        double dWindDeg;
    }

    public class Sys
    {
        // страна
        @SerializedName("country")
        private String sCountry;
        // время рассвета
        @SerializedName("sunrise")
        private long iTimeSunrise;
        // время заката
        @SerializedName("sunset")
        private long iTimeSunset;
    }

    // название города
    @SerializedName("name")
    private String sCityName;

    //
    @SerializedName("id")
    private long iCityID;

    @SerializedName("coord")
    private Coords coords;

    // температура
    @SerializedName("main")
    private DayTemperature temp;

    // описание погодных условий
    @SerializedName("weather")
    private List<WeatherDesc> weatherDesc;

    @SerializedName("wind")
    private Wind wind;

    // дата и время
    @SerializedName("dt")
    private long iDateTime;

    @SerializedName("sys")
    private Sys sys;


    public WeatherDay( DayTemperature temp, List<WeatherDesc> desc )
    {
        this.temp = temp;
        this.weatherDesc = desc;
    }

    public WeatherDay()
    {
        temp = new DayTemperature();
        weatherDesc = new ArrayList<WeatherDesc>();
        coords = new Coords();
        wind = new Wind();
        sys = new Sys();
    }


    // Функция заполнения тестовыми данными
    public void setTestData( String sCity, double dLat, double dLon, long iTimeUpd, double dCurrentTemp, double dPressure,
                                    double dHumidity, double dWindSpeed, double dWindDeg, String sWeatherDesc )
    {
        weatherDesc.add( new WeatherDesc() );

        this.sCityName = sCity;
        coords.dCityLat = dLat;
        coords.dCityLon = dLon;
        this.iDateTime = iTimeUpd;
        temp.temp = dCurrentTemp;
        temp.dPressure = dPressure;
        temp.dHumidity = dHumidity;
        wind.dWindSpeed = dWindSpeed;
        wind.dWindDeg = dWindDeg;
        weatherDesc.get(0).description = sWeatherDesc;
    }

    ////////////////////////////геттеры////////////////////////////

    //! Возвращает URL иконки погоды
    public String getWeatherIconUrl()
    {
        return "http://openweathermap.org/img/w/" + weatherDesc.get(0).icon + ".png";
    }

    //! Возвращает URL иконки флага страны
    public String getCountryFlagUrl()
    {
        return "http://openweathermap.org/images/flags/" + sys.sCountry.toLowerCase() + ".png";
    }

    //! Возвращает название города
    public String getCityName()
    {
        return sCityName;
    }

    //! Возвращает название страны
    public String getCountry()
    {
        return sys.sCountry.toLowerCase();
    }

    //! Возвращает широту
    public double getLatitude()
    {
        return coords.dCityLat;
    }

    //! Возвращает долготу
    public double getLongitude()
    {
        return coords.dCityLon;
    }

    //! Возвращает текущую температуру в Кельвинах
    public double getCurrentTemp()
    {
        return temp.temp;
    }

    //! Возвращает описание погодных условий
    public String getWeatherDesc()
    {
        return weatherDesc.get(0).description;
    }

    //! Возвращает давление
    public double getPressure()
    {
        return temp.dPressure;
    }

    //! Возвращает влажность в процентах
    public double getHumidity()
    {
        return temp.dHumidity;
    }

    //! Возвращает дату и время обновления в UNIX timestamp
    public long getTimeUpdate()
    {
        return iDateTime;
    }

    //! Возвращает скорость ветра в м/с
    public double getWindSpeed()
    {
        return wind.dWindSpeed;
    }

    //! Возвращает направление ветра в градусах
    public double getWindDeg()
    {
        return wind.dWindDeg;
    }

    //! Возвращает время рассвета UNIX timestamp
    public long getTimeSunrise()
    {
        return sys.iTimeSunrise;
    }

    //! Возвращает время заката в UNIX timestamp
    public long getTimeSunset()
    {
        return sys.iTimeSunset;
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
        coords.dCityLat = dLat;
    }

    //! Записывает долготу города
    public void setCityLongitude( double dLon )
    {
        coords.dCityLon = dLon;
    }

    //! Записывает название страны
    public void setCountry( String sCountry )
    {
        sys.sCountry = sCountry;
    }

    //! Записывает текущую температуру
    public void setCurrentTemp( double dCurrentTemp )
    {
        temp.temp = dCurrentTemp;
    }

    //! Записывает минимальную температуру на данный момент
    public void setTempMin(double dTempMin) {
        temp.temp_min = dTempMin;
    }

    //! Записывает максимальную температуру на данный момент
    public void setTempMax(double dTempMax) {
        temp.temp_max = dTempMax;
    }

    //! Записывает давление
    public void setPressure(double dPressure) {
        temp.dPressure = dPressure;
    }

    //! Записывает влажность в процентах
    public void setHumidity(double dHumidity) {
        temp.dHumidity = dHumidity;
    }

    //! Записывает текущую дату и время в UNIX timestamp
    public void setTimeUpdate(long iDateTime) {
        this.iDateTime = iDateTime;
    }

    //! Записывает скорость ветра в м/с
    public void setWindSpeed(double dWindSpeed) {
        wind.dWindSpeed = dWindSpeed;
    }

    //! Записывает направление ветра в градусах
    public void setWindDeg(double dWindDeg) {
        wind.dWindDeg = dWindDeg;
    }

    //! Записывает время рассвета в UNIX timestamp
    public  void setTimeSunrise(long iTimeSunrise )
    {
        sys.iTimeSunrise = iTimeSunrise;
    }

    //! Записывает время заката в UNIX timestamp
    public void setTimeSunset( long iTimeSunset )
    {
        sys.iTimeSunset = iTimeSunset;
    }
}
