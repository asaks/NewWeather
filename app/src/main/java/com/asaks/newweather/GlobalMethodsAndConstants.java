package com.asaks.newweather;

/**
 * Константы и общеиспользуемые функции
 */

public class GlobalMethodsAndConstants
{
    // названия вкладок
    public static final String TITLE_FIRST_TAB = "Погода сейчас";
    public static final String TITLE_SECOND_TAB = "Прогноз погоды";

    // номера вкладок
    public static final int PAGE_CURRENT_WEATHER = 0; // вкладка текущей погоды
    public static final int PAGE_FORECAST_WEATHER = 1; // вкладка прогноза погоды

    // тэги
    public static final String TAG_SETTINGS = "settings";
    public static final String TAG_TEMP_UNITS = "temp_units";
    public static final String TAG_PRESS_UNITS = "press_units";
    public static final String TAG_ABOUT_APP = "about_app";
    public static final String TAG_WEATHER = "weather";
    public static final String TAG_FORECAST = "forecast";
    public static final String TAG_CITY = "city";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LON = "lon";

    // смещение по оси Y для всплывающей подсказки
    public static final int Y_OFFSET_TOAST = 100;

    // код запроса настроек приложения
    public static final int REQUEST_CODE_SETTINGS = 111;

    // константы типа географических координат
    public static final int COORD_LAT = 0; // широта
    public static final int COORD_LON = 1; // долгота

    // температура
    public static final int TEMP_CELSIUS = 0; // градусы Цельсия
    public static final int TEMP_FARENHEIT = 1; // градусы Фаренгейта
    public static final int TEMP_KELVIN = 2; // Кельвины

    // давление
    public static final int PRESS_HPA = 0; // гПа
    public static final int PRESS_MM_HG = 1; // мм рт.ст.

    // actions
    public static final String INTENT_NEW_WEATHER = "update_current_weather";
    public static final String INTENT_NEW_SETTINGS = "new_settings";
    public static final String INTENT_NEW_FORECAST = "update_weather_forecast";

    //
    public static final int IDD_CURRENT_WEATHER = 1;
    public static final int IDD_WEATHER_FORECAST = 2;

    /**
     * Функция конвертации температуры из Кельвинов в градусы Цельсия
     * @param dTempKelvin - температура в кельвинах
     * @return температура в градусах Цельсия
     */
    public static long toCelsius( double dTempKelvin )
    {
        double dCelsiusTemp = dTempKelvin - 273.15;

        return Math.round( dCelsiusTemp );
    }

    /**
     * Функция конвертации температуры из Кельвинов в градусы Фаренгейта
     * @param dTempKelvin - температура в кельвинах
     * @return температура в градусах Фаренгейта
     */
    public static long toFarenheit( double dTempKelvin )
    {
        double dTempF = 1.8 * ( dTempKelvin - 273.15 ) + 32;

        return Math.round( dTempF );
    }

    /**
     * Функция конвертации гектопаскалей в мм рт. ст.
     * @param dhPa - давление в гектопаскалях
     * @return давление в мм рт. ст.
     */
    public static double toMmHg( double dhPa )
    {
        return dhPa * 0.750064;
    }
}
