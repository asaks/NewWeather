package com.asaks.newweather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс настроек приложения
 */
@Entity(tableName = "settings")
public class ApplicationSettings implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private long idd;
    // город
    @ColumnInfo(name = "city")
    private String city;
    // позиция выбранной единицы измерения температуры
    @ColumnInfo(name = "unit_temp")
    private int unitTempPos;
    // позиция выбранной единицы измерения давления
    @ColumnInfo(name = "unit_press")
    private int unitPressPos;
    // широта города в градусах
    @ColumnInfo(name = "lat")
    private double lat;
    // долгота города в градусах
    @ColumnInfo(name = "lon")
    private double lon;

    public ApplicationSettings()
    {
        this.city = "";
        this.unitTempPos = GlobalMethodsAndConstants.TEMP_KELVIN;
        this.unitPressPos = GlobalMethodsAndConstants.PRESS_MM_HG;
        this.lat = 0;
        this.lon = 0;
    }

    protected ApplicationSettings(Parcel in) {
        city = in.readString();
        unitTempPos = in.readInt();
        unitPressPos = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
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
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }

    public long getIdd()
    {
        return idd;
    }

    public void setIdd(long idd)
    {
        this.idd = idd;
    }

    public int getUnitTempPos()
    {
        return unitTempPos;
    }

    public void setUnitTempPos(int unitTempPos)
    {
        this.unitTempPos = unitTempPos;
    }

    public int getUnitPressPos()
    {
        return unitPressPos;
    }

    public void setUnitPressPos(int unitPressPos)
    {
        this.unitPressPos = unitPressPos;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }
}
