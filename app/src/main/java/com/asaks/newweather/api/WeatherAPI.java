package com.asaks.newweather.api;

import com.asaks.newweather.weather.WeatherDay;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherAPI
{
    private static Retrofit retrofit = null;

    public interface InterfaceAPI
    {
        @GET("weather")
        Call<WeatherDay> getCurrentWeather(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

    }

    public static Retrofit getClient()
    {
        //TODO добавить конвертер
        if ( null == retrofit )
            retrofit = new Retrofit.Builder().baseUrl(ConstantsAPI.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
