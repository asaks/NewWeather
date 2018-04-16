package com.asaks.newweather.weather;

/**
 * Created by asaks on 13.04.18.
 *
 * Класс, содержащий информацию о текущей погоде в указанном городе
 */

public class CurrentWeather
{
    // название города
    private String sCityName;
    // страна
    private String sCountry;
    // широта города
    private double dCityLat;
    // долгота города
    private double dCityLon;
    // текущая температура
    private double dCurrentTemp;
    // давление
    private double dPressure;
    // влажность
    private double dHumidity;
    // температурный минимум
    private double dTempMin;
    // температурный максимум
    private double dTempMax;
    // давление на уровне моря, гектопаскалей
    private double dSeaLevel;
    // давление на уровне земли, гектопаскалей
    private double dGroundLevel;
    // дата и время
    private long iDateTime;
    // скорость ветра
    private double dWindSpeed;
    // направление ветра, в градусах
    private double dWindDeg;
    // облачность, %
    private double dClouds;
    // объем дождя за последние 3 часа
    private double dRain;
    // объем снега за последние 3 часа
    private double dSnow;
    // описание погодных условий
    private String sWeatherDesc;
    // id иконки погодных условий OpenWeatherMap
    private int iIDweather;
    // описание неба
    private String sSkyDesc;
    // иконка погодных условий
    private String sWeatherIcon;
    // время рассвета
    private long iTimeDawn;
    // время заката
    private long iTimeSunset;


    // Функция заполнения тестовыми данными
    public void setTestData( String sCityName, double dLat, double dLon, long iTimeUpd, double dCurrentTemp, double dPressure,
                                    double dHumidity, double dWindSpeed, double dWindDeg, String sWeatherDesc, String sSkyDesc )
    {
        this.sCityName = sCityName;
        this.dCityLat = dLat;
        this.dCityLon = dLon;
        this.iDateTime = iTimeUpd;
        this.dCurrentTemp = dCurrentTemp;
        this.dPressure = dPressure;
        this.dHumidity = dHumidity;
        this.dWindSpeed = dWindSpeed;
        this.dWindDeg = dWindDeg;
        this.sWeatherDesc = sWeatherDesc;
        this.sSkyDesc = sSkyDesc;
    }

    ////////////////////////////геттеры////////////////////////////

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

    //! Возвращает id погодных условий OpenWeatherMap
    public int getIDweather()
    {
        return iIDweather;
    }

    //! Возвращает текущую температуру в кельвинах
    public double getCurrentTemp()
    {
        return dCurrentTemp;
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

    //! Возвращает минимальную температуру в этот день в кельвинах
    public double getTempMin()
    {
        return dTempMin;
    }

    //! Возвращает максимальную температуру в этот день в кельвинах
    public double getTempMax()
    {
        return dTempMax;
    }

    //! Возвращает давление на уровне моря, гектопаскалей
    public double getSeaLevel()
    {
        return dSeaLevel;
    }

    //! Возвращает давление на уровне земли, гектопаскалей
    public double getGroundLevel()
    {
        return dGroundLevel;
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

    //! Возвращает процент облачности
    public double getClouds()
    {
        return dClouds;
    }

    //! Возвращает объем дождя за посление 3 часа
    public double getRain()
    {
        return dRain;
    }

    //! Возвращает объем снега за последние 3 часа
    public double getSnow()
    {
        return dSnow;
    }

    //! Возвращает описание погодных условий
    public String getWeatherDesc()
    {
        return sWeatherDesc;
    }

    //! Возвращает описание состояния неба
    public String getSkyDesc()
    {
        return sSkyDesc;
    }

    //! Возвращает иконку погодных условий
    public String getWeatherIcon()
    {
        return sWeatherIcon;
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
        this.dCurrentTemp = dCurrentTemp;
    }

    //! Записывает id погодных условий OpenWeatherMap
    public void setIDweather( int iIDweather )
    {
        this.iIDweather = iIDweather;
    }

    //! Записывает давление
    public void setPressure(double dPressure) {
        this.dPressure = dPressure;
    }

    //! Записывает влажность в процентах
    public void setHumidity(double dHumidity) {
        this.dHumidity = dHumidity;
    }

    //! Записывает минимальную температуру на данный момент
    public void setTempMin(double dTempMin) {
        this.dTempMin = dTempMin;
    }

    //! Записывает максимальную температуру на данный момент
    public void setTempMax(double dTempMax) {
        this.dTempMax = dTempMax;
    }

    //! Записывает давление на уровне моря в гектопаскалях
    public void setSeaLevel(double dSeaLevel) {
        this.dSeaLevel = dSeaLevel;
    }

    //! Записывает давление на уровне земли в гектопаскалях
    public void setGroundLevel(double dGroundLevel) {
        this.dGroundLevel = dGroundLevel;
    }

    //! Записывает текущую дату и время в секундах
    public void setDateTime(long iDateTime) {
        this.iDateTime = iDateTime;
    }

    //! Записывает скорость ветра в м/с
    public void setWindSpeed(double dWindSpeed) {
        this.dWindSpeed = dWindSpeed;
    }

    //! Записывает процент облачности
    public void setClouds( double dClouds )
    {
        this.dClouds = dClouds;
    }

    //! Записывает направление ветра в градусах
    public void setWindDeg(double dWindDeg) {
        this.dWindDeg = dWindDeg;
    }

    //! Записывает объем дождя за посление 3 часа
    public void setRain(double dRain) {
        this.dRain = dRain;
    }

    //! Записывает объем снега за посление 3 часа
    public void setSnow(double dSnow) {
        this.dSnow = dSnow;
    }

    //! Записывает описание погодных условий
    public void setWeatherDesc(String sWeatherDesc) {
        this.sWeatherDesc = sWeatherDesc;
    }

    //! Записывает описание состояния неба
    public void setSkyDesc(String sSkyDesc) {
        this.sSkyDesc = sSkyDesc;
    }

    //! Записывает иконку погодных условий
    public void setWeatherIcon( String sWeatherIcon )
    {
        this.sWeatherIcon = sWeatherIcon;
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
