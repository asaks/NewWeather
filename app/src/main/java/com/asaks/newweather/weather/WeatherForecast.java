package com.asaks.newweather.weather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.asaks.newweather.db.DbTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий прогноз погоды
 */
@Entity(tableName = "weather_forecast")
public class WeatherForecast implements Parcelable
{
    @ColumnInfo(name = "forecast")
    @TypeConverters({DbTypeConverters.class})
    @SerializedName("list")
    private List<WeatherDay> weatherItems;

    public WeatherForecast()
    {
        weatherItems = new ArrayList<>();
    }

    public WeatherForecast( List<WeatherDay> items )
    {
        this.weatherItems = items;
    }

    protected WeatherForecast(Parcel in) {
        weatherItems = in.createTypedArrayList(WeatherDay.CREATOR);
    }

    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(weatherItems);
    }

    public List<WeatherDay> getWeatherItems()
    {
        return weatherItems;
    }

    public void setWeatherItems( List<WeatherDay> items )
    {
        this.weatherItems.clear();
        this.weatherItems = items;
    }

}
