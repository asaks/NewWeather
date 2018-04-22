package com.asaks.newweather.api;

import com.asaks.newweather.weather.WeatherDay;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

//! Класс "общения" с OpenWeatherMap
public class WeatherAPI
{
    private static Retrofit retrofit = null;

    //! Интерфейс запроса погоды
    public interface InterfaceAPI
    {
        //! Запрос текущей погоды по координатам
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        //! Запрос текущей погоды по id города OpenWeatherMap
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("id") long id,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        //! Запрос текущей погоды по названию города
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("q") String city,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

    }

    public static Retrofit getClient()
    {
        if ( null == retrofit )
            retrofit = new Retrofit.Builder().baseUrl(ConstantsAPI.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
