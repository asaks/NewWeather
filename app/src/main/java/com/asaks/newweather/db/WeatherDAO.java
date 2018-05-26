package com.asaks.newweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;

import java.util.List;

@Dao
public interface WeatherDAO
{
    /**
     * Запрос информации о текущей погоде по всем городам
     * @return список объектов с информацией о погоде в городах
     */
    @Query("select * from data_weather")
    List<WeatherDay> getCurrentWeather();

    /**
     * Вставка новых данных в БД
     * @param weatherDay - объект с информацией
     */
    @Insert
    void insert( WeatherDay weatherDay );

    /**
     * Обновление данных в БД
     * @param weatherDay - объект с информацией, которую нужно обновить
     */
    @Update
    void update( WeatherDay weatherDay );

    /**
     * Удаление записи из БД
     * @param weatherDay - объект с информацией, которую нужно удалить
     */
    @Delete
    void delete( WeatherDay weatherDay );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveWeatherDay( WeatherDay day );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveCoords(WeatherDay.Coords coords);

    @Query("select * from coords where id = :id")
    WeatherDay.Coords getCoords(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveWind( WeatherDay.Wind wind );

    @Query("select * from wind where id = :id")
    WeatherDay.Wind getWind(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveTemp(WeatherDay.DayTemperature temp);

    @Query("select * from temperature where id = :id")
    WeatherDay.DayTemperature getTemp(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveWeatherDesc(List<WeatherDay.WeatherDesc> lst);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveSys(WeatherDay.Sys sys);

    @Query("select * from sys where id = :id")
    WeatherDay.Sys getSys(long id);

    @Query("select * from data_weather where current_or_forecast = :currentOrForecast")
    List<WeatherDay> getCurrentWeather(int currentOrForecast);

    @Query("select * from weather where id_weather_day = :idd")
    List<WeatherDay.WeatherDesc> getWeatherDesc(long idd);

    @Query("delete from data_weather where current_or_forecast = :curr_or_forecast")
    void deleteCurrentWeather(int curr_or_forecast);

    @Query("select * from data_weather where current_or_forecast = :curr_or_forecast")
    List<WeatherDay> getForecast(int curr_or_forecast);
}
