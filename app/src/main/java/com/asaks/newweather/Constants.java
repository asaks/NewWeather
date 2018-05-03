package com.asaks.newweather;

/**
 * Константы
 */

public class Constants
{
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
}
