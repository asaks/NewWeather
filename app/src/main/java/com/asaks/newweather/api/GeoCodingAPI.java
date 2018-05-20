package com.asaks.newweather.api;

import com.asaks.newweather.geo.GeoCodingAddress;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Класс, реализующий взаимодействие с GeoCoding API
 */
public class GeoCodingAPI
{
    private static Retrofit retrofit = null;

    public interface InterfaceGeoAPI
    {
        /**
         * Запрос координат места
         * @param address - адрес места
         * @param language - язык ответа
         * @param key - ключ GeoCoding API
         * @return
         */
        @GET("json")
        Call<GeoCodingAddress> getGeoAddress(
                @Query("address") String address,
                @Query("language") String language,
                @Query("key") String key
        );
    }

    /**
     * Создание клиента GeoCoding API
     * @return ссылка на клиент
     */
    public static Retrofit getClient()
    {
        if ( null == retrofit )
            retrofit = new Retrofit.Builder().baseUrl(ConstantsGeoCodingAPI.BASE_API_URL)
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();

        return retrofit;
    }
}
