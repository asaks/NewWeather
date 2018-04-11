package com.asaks.lib;

/**
 * Created by asaks on 11.04.18.
 */

/**
 * Класс, содержащий информацию о погодных условиях
 */

public class WeatherData
{
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
    private int iDateTime;
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
    // описание неба
    private String sSkyDesc;

    public WeatherData(double dCurrentTemp, double dPressure, double dHumidity, double dTempMin, double dTempMax,
                       double dSeaLevel, double dGroundLevel, int iDateTime, double dWindSpeed, double dWindDeg,
                       double dClouds, double dRain, double dSnow, String sSky, String sSkyDesc)
    {
        this.dCurrentTemp = dCurrentTemp;
        this.dPressure = dPressure;
        this.dHumidity = dHumidity;
        this.dTempMin = dTempMin;
        this.dTempMax = dTempMax;
        this.dSeaLevel = dSeaLevel;
        this.dGroundLevel = dGroundLevel;
        this.iDateTime = iDateTime;
        this.dWindSpeed = dWindSpeed;
        this.dWindDeg = dWindDeg;
        this.dClouds = dClouds;
        this.dRain = dRain;
        this.dSnow = dSnow;
        this.sWeatherDesc = sSky;
        this.sSkyDesc = sSkyDesc;
    }

    public WeatherData()
    {
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
    public int getDateTime()
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

    //! Записывает текущую температуру
    public void setCurrentTemp(double dCurrentTemp) {
        this.dCurrentTemp = dCurrentTemp;
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
    public void setDateTime(int iDateTime) {
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

    //!
    public void setWeatherDesc(String sWeatherDesc) {
        this.sWeatherDesc = sWeatherDesc;
    }

    //! Записывает описание состояния неба
    public void setSkyDesc(String sSkyDesc) {
        this.sSkyDesc = sSkyDesc;
    }
}
