package com.asaks.newweather.api;

import retrofit2.Retrofit;

public class WeatherAPI
{
    private static Retrofit retrofit = null;

    public interface InterfaceAPI
    {

    }

    public static Retrofit getClient()
    {
        if ( null == retrofit )
            retrofit = new Retrofit.Builder().baseUrl(ConstantsAPI.BASE_API_URL).build();

        return retrofit;
    }
}
