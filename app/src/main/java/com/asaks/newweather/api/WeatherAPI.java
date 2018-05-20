package com.asaks.newweather.api;

import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Класс, реализующий взаимодействие с API OpenWeatherMap
 */

public class WeatherAPI
{
    private static Retrofit retrofit = null;

    //! Интерфейс запроса погоды
    public interface InterfaceAPI
    {
        /**
         * Запрос текущей погоды по координатам города
         * @param lat - широта города в градусах
         * @param lon - долгота города в градусах
         * @param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о погоде
         */
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        /**
         * Запрос текущей погоды по id города OpenWeatherMap
         * @param id - id города
         * @param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о погоде
         */
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("id") long id,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        /**
         * Запрос текущей погоды по названию города
         * @param city - название города
         * @param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о погоде
         */
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("q") String city,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        /**
         * Запрос прогноза погоды по id города OpenWeatherMap
         * @param id - id Города
         * @param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о прогнозе погоды
         */
        @GET("forecast")
        Call<WeatherForecast> getWeatherForecast(
                @Query("id") long id,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        /**
         * Запрос прогноза погоды по названию города
         * @param city - название города
         * @param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о прогнозе погоды
         */
        @GET("forecast")
        Call<WeatherForecast> getWeatherForecast(
                @Query("q") String city,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        /**
         * Запрос прогноза погоды по координатам города
         * @param lat - широта города в градусах
         * @param lon - долгота города в градусах
         * @@param lang - язык ответа
         * @param appid - ключ OpenWeatherMap
         * @return объект с данными о прогнозе погоды
         */
        @GET("forecast")
        Call<WeatherForecast> getWeatherForecast(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("lang") String lang,
                @Query("appid") String appid
        );
    }

    public static Retrofit getClient()
    {
        if ( null == retrofit )
            retrofit = new Retrofit.Builder().baseUrl(ConstantsWeatherAPI.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
