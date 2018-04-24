package com.asaks.newweather.weather;

import com.asaks.newweather.Constants;

/**
 * Класс настроек приложения
 */

public class ApplicationSettings
{

    // позиция выбранной единицы измерения температуры
    private int unitTempPos;

    public ApplicationSettings()
    {
        this.unitTempPos = Constants.TEMP_KELVIN;
    }

    public int getUnitTempPos()
    {
        return unitTempPos;
    }

    public void setUnitTempPos( int unitTempPos )
    {
        this.unitTempPos = unitTempPos;
    }
}
