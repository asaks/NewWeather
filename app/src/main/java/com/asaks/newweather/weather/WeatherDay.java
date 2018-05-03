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
    //! Класс, содержащий географические координаты города
    public class Coords
    {
        // широта города
        @SerializedName("lat")
        double dCityLat;
        // долгота города
        @SerializedName("lon")
        double dCityLon;
    }
    //! Класс, содержащий сведения о температуре, влажности, давлении
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
        @SerializedName("id")
        int id;
        // описание группы погодных условий
        @SerializedName("main")
        String main;
        // описание погодных условий
        @SerializedName("description")
        String description;
        // иконка
        @SerializedName("icon")
        String icon;
    }

    //! Класс, содержащий сведения о ветре
    public class Wind
    {
        // скорость ветра
        @SerializedName("speed")
        double dWindSpeed;
        // направление (метеорологическое) ветра, в градусах
        @SerializedName("deg")
        double dWindDeg;
    }

    //! Класс, содержащий информацию о стране и времени восхода и заката
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

    // id города OpenWeatherMap
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


    public WeatherDay( Coords coords, DayTemperature temp, List<WeatherDesc> desc, Wind wind, Sys sys )
    {
        this.coords = coords;
        this.temp = temp;
        this.weatherDesc = desc;
        this.wind = wind;
        this.sys = sys;
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
                                    double dHumidity, double dWindSpeed, double dWindDeg, String sWeatherDesc,
                                    long iSunrise, long iSunset )
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
        sys.iTimeSunrise = iSunrise;
        sys.iTimeSunset = iSunset;
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

    public int getWeatherID()
    {
        return weatherDesc.get(0).id;
    }

    public String getWeatherMain()
    {
        return weatherDesc.get(0).main;
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

    //! Возвращает направление ветра в градусах (метеорологическое направление)
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

    //! Возвращает id города OpenWeatherMap
    public long getCityID()
    {
        return iCityID;
    }

    ////////////////////////////сеттеры////////////////////////////

    //! Записывает id города OpenWeatherMap
    public void setCityID( long iCityID )
    {
        this.iCityID = iCityID;
    }

    public void setWeatherID( int id )
    {
        weatherDesc.get(0).id = id;
    }

    public void setWeatherMain( String main )
    {
        weatherDesc.get(0).main = main;
    }

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

    //! Записывает направление ветра в градусах (метеорологическое направление)
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
