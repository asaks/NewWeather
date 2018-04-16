package com.asaks.newweather.weather;

/**
 * Created by asaks on 13.04.18.
 */

public class City
{
    // id города в OpenWeatherMap
    private int iIDcity;
    // название города
    private  String sCityName;
    // страна
    private String sCountry;
    // широта
    private double dLatitude;
    // долгота
    private double dLongitude;

    public City(int iIDcity, String sCityName, String sCountry, double dLatitude, double dLongitude)
    {
        this.iIDcity = iIDcity;
        this.sCityName = sCityName;
        this.sCountry = sCountry;
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;
    }

    public City()
    {
        this(0,"","",0.0,0.0);
    }

    //! Возвращает ID города OpenWeatherMap
    public int getIDcity()
    {
        return iIDcity;
    }

    //! Возвращает страну
    public String getCountry()
    {
        return sCountry;
    }

    //! Возвращает широту в градусах
    public double getLatitude()
    {
        return dLatitude;
    }

    //! Возвращает долготу в градусах
    public double getLongitude()
    {
        return dLongitude;
    }

    //! Возвращает название города
    public String getCityName()
    {
        return sCityName;
    }

    //! Записывает ID города OpenWeatherMap
    public void setIDcity(int iIDcity)
    {
        this.iIDcity = iIDcity;
    }

    //! Записывает страну
    public void setCountry( String sCountry )
    {
        this.sCountry = sCountry;
    }

    //! Записывает широту в градусах
    public void setLatitude(double dLatitude)
    {
        this.dLatitude = dLatitude;
    }

    //! Записывает долготу в градусах
    public void setLongitude(double dLongitude)
    {
        this.dLongitude = dLongitude;
    }

    //! Записывает название города
    public void setCityName(String sCityName)
    {
        this.sCityName = sCityName;
    }
}
