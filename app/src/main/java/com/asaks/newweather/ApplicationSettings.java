package com.asaks.newweather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс настроек приложения
 */

public class ApplicationSettings implements Parcelable
{
    // город
    private String city;
    // позиция выбранной единицы измерения температуры
    private int unitTempPos;
    // позиция выбранной единицы измерения давления
    private int unitPressPos;
    //

    public ApplicationSettings()
    {
        this.city = "";
        this.unitTempPos = GlobalMethodsAndConstants.TEMP_KELVIN;
        this.unitPressPos = GlobalMethodsAndConstants.PRESS_MM_HG;
    }

    protected ApplicationSettings(Parcel in) {
        city = in.readString();
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
        parcel.writeString(city);
        parcel.writeInt(unitTempPos);
        parcel.writeInt(unitPressPos);
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
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
