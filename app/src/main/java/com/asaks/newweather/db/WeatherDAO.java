package com.asaks.newweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asaks.newweather.weather.WeatherDay;

import java.util.List;

@Dao
public interface WeatherDAO
{
    /**
     * Запрос информации о текущей погоде по всем городам
     * @return список объектов с информацией о погоде в городах
     */
    @Query("select * from current_weather")
    List<WeatherDay> getCurrentWeather();

    /**
     * Запрос текущей погоды по идентификатору города
     * @param id - идентификатор города
     * @return объект с информацией о текущей погоде
     */
    @Query("select * from current_weather where cityID = :id")
    WeatherDay getCurrentWeatherByCityId( long id );

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

    /**
     * Проверка существования записи в БД
     * @param id - идентификатор города
     * @return true - если существует, false - нет
     */
    @Query("select exists (select cityID from current_weather where cityID = :id)")
    boolean isExist( long id );
}
