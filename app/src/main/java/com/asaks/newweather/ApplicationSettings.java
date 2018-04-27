package com.asaks.newweather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс настроек приложения
 */

public class ApplicationSettings implements Parcelable
{

    // позиция выбранной единицы измерения температуры
    private int unitTempPos;
    // позиция выбранной единицы измерения давления
    private int unitPressPos;

    public ApplicationSettings()
    {
        this.unitTempPos = Constants.TEMP_KELVIN;
    }

    protected ApplicationSettings(Parcel in) {
        unitTempPos = in.readInt();
        unitPressPos = in.readInt();
    }

    public static final Creator<ApplicationSettings> CREATOR = new Creator<ApplicationSettings>() {
        @Override
        public ApplicationSettings createFromParcel(Parcel in) {
            return new ApplicationSettings(in);
        }

        @Override
        public ApplicationSettings[] newArray(int size) {
            return new ApplicationSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(unitTempPos);
        parcel.writeInt(unitPressPos);
    }

    public int getUnitTemp()
    {
        return unitTempPos;
    }

    public void setUnitTemp(int unitTempPos)
    {
        this.unitTempPos = unitTempPos;
    }

    public int getUnitPress()
    {
        return unitPressPos;
    }

    public void setUnitPress(int unitPressPos)
    {
        this.unitPressPos = unitPressPos;
    }
}
