package com.asaks.newweather.weather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcelable;
import android.os.Parcel;

import com.asaks.newweather.db.DbTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaks on 13.04.18.
 *
 * Класс, содержащий информацию о погоде на день
 */


@Entity(tableName = "current_weather")
public class WeatherDay implements Parcelable
{
    //! Класс, содержащий географические координаты города
    public static class Coords
    {
        // широта города
        @ColumnInfo(name = "lat")
        @SerializedName("lat")
        double lat;
        // долгота города
        @ColumnInfo(name = "lon")
        @SerializedName("lon")
        double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
    //! Класс, содержащий сведения о температуре, влажности, давлении
    public static class DayTemperature
    {
        // текущая температура
        @ColumnInfo(name = "temp")
        @SerializedName("temp")
        double temp;
        // температурный минимум
        @ColumnInfo(name = "temp_min")
        @SerializedName("temp_min")
        double temp_min;
        // температурный максимум
        @ColumnInfo(name = "temp_max")
        @SerializedName("temp_max")
        double temp_max;
        // давление
        @ColumnInfo(name = "pressure")
        @SerializedName("pressure")
        double pressure;
        // влажность
        @ColumnInfo(name = "humidity")
        @SerializedName("humidity")
        double humidity;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }
    }

    //! Класс, содержащий описание погодных условий
    @Entity
    public class WeatherDesc
    {
        // id погодных условий в OpenWeatherMap
        @ColumnInfo(name = "weather_id")
        @SerializedName("id")
        int id;
        // описание группы погодных условий
        @ColumnInfo(name = "main")
        @SerializedName("main")
        String main;
        // описание погодных условий
        @ColumnInfo(name =" weather_desc")
        @SerializedName("description")
        String description;
        // иконка
        @ColumnInfo(name =" weather_icon")
        @SerializedName("icon")
        String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    //! Класс, содержащий сведения о ветре
    public static class Wind
    {
        // скорость ветра
        @ColumnInfo(name = "speed")
        @SerializedName("speed")
        double speed;
        // направление (метеорологическое) ветра, в градусах
        @ColumnInfo(name = "wind_deg")
        @SerializedName("deg")
        double deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }
    }

    //! Класс, содержащий информацию о стране и времени восхода и заката
    public static class Sys
    {
        // страна
        @ColumnInfo(name = "country")
        @SerializedName("country")
        private String country;
        // время рассвета
        @ColumnInfo(name = "sunrise")
        @SerializedName("sunrise")
        private long sunrise;
        // время заката
        @ColumnInfo(name = "sunset")
        @SerializedName("sunset")
        private long sunset;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }

    // название города
    @ColumnInfo(name = "city_name")
    @SerializedName("name")
    private String cityName;

    // id города OpenWeatherMap
    @PrimaryKey
    @SerializedName("id")
    private long cityID;

    @Embedded
    @SerializedName("coord")
    private Coords coords;

    // температура
    @Embedded
    @SerializedName("main")
    private DayTemperature temp;

    // описание погодных условий
    //TODO какую аннотацию вписать чтобы работать со списком
    @ColumnInfo(name = "weather_desc")
    @TypeConverters({DbTypeConverters.class})
    @SerializedName("weather")
    private List<WeatherDesc> weatherDesc;

    @SerializedName("wind")
    @Embedded
    private Wind wind;

    // дата и время
    @ColumnInfo(name = "datetime_update")
    @SerializedName("dt")
    private long dateTime;

    @Embedded
    @SerializedName("sys")
    private Sys sys;

    protected WeatherDay(Parcel in) {
        cityName = in.readString();
        cityID = in.readLong();
        dateTime = in.readLong();
        coords.lat = in.readDouble();
        coords.lon = in.readDouble();
        temp.temp = in.readDouble();
        temp.humidity = in.readDouble();
        temp.pressure = in.readDouble();
        weatherDesc.get(0).description = in.readString();
        weatherDesc.get(0).icon = in.readString();
        wind.deg = in.readDouble();
        wind.speed = in.readDouble();
        sys.country = in.readString();
        sys.sunrise = in.readLong();
        sys.sunset = in.readLong();
    }

    public static final Creator<WeatherDay> CREATOR = new Creator<WeatherDay>() {
        @Override
        public WeatherDay createFromParcel(Parcel in) {
            return new WeatherDay(in);
        }

        @Override
        public WeatherDay[] newArray(int size) {
            return new WeatherDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeLong(cityID);
        parcel.writeLong(dateTime);
        parcel.writeDouble(coords.lat);
        parcel.writeDouble(coords.lon);
        parcel.writeDouble(temp.temp);
        parcel.writeDouble(temp.humidity);
        parcel.writeDouble(temp.pressure);

        if ( !weatherDesc.isEmpty() )
        {
            parcel.writeString(weatherDesc.get(0).description);
            parcel.writeString(weatherDesc.get(0).icon);
        }

        parcel.writeDouble(wind.deg);
        parcel.writeDouble(wind.speed);
        parcel.writeString(sys.country);
        parcel.writeLong(sys.sunrise);
        parcel.writeLong(sys.sunset);
    }


    public WeatherDay( Coords coords, DayTemperature temp, List<WeatherDesc> desc, Wind wind, Sys sys )
    {
        this.coords = coords;
        this.temp = temp;
        this.weatherDesc = desc;
        this.wind = wind;
        this.sys = sys;
    }

    public WeatherDay()
    {
        temp = new DayTemperature();
        weatherDesc = new ArrayList<>();
        coords = new Coords();
        wind = new Wind();
        sys = new Sys();
    }

    ////////////////////////////геттеры////////////////////////////

    //! Возвращает URL иконки погоды
    public String getWeatherIconUrl()
    {
        return "http://openweathermap.org/img/w/" + weatherDesc.get(0).icon + ".png";
    }

    //! Возвращает URL иконки флага страны
    public String getCountryFlagUrl()
    {
        return "http://openweathermap.org/images/flags/" + sys.country.toLowerCase() + ".png";
    }

    public int getWeatherID()
    {
        return weatherDesc.get(0).id;
    }

    public String getWeatherMain()
    {
        return weatherDesc.get(0).main;
    }

    public List<WeatherDesc> getWeatherDesc()
    {
        return weatherDesc;
    }

    //! Возвращает название города
    public String getCityName()
    {
        return cityName;
    }

    //! Возвращает название страны
    public String getCountry()
    {
        return sys.country.toLowerCase();
    }

    //! Возвращает широту
    public double getLatitude()
    {
        return coords.lat;
    }

    //! Возвращает долготу
    public double getLongitude()
    {
        return coords.lon;
    }

    //! Возвращает текущую температуру в Кельвинах
    public double getCurrentTemp()
    {
        return temp.temp;
    }

    //! Возвращает описание погодных условий
    public String getWeatherDescription()
    {
        return weatherDesc.get(0).description;
    }

    //! Возвращает давление
    public double getPressure()
    {
        return temp.pressure;
    }

    //! Возвращает влажность в процентах
    public double getHumidity()
    {
        return temp.humidity;
    }

    //! Возвращает дату и время обновления в UNIX timestamp
    public long getDateTime()
    {
        return dateTime;
    }

    //! Возвращает скорость ветра в м/с
    public double getWindSpeed()
    {
        return wind.speed;
    }

    //! Возвращает направление ветра в градусах (метеорологическое направление)
    public double getWindDeg()
    {
        return wind.deg;
    }

    //! Возвращает время рассвета UNIX timestamp
    public long getTimeSunrise()
    {
        return sys.sunrise;
    }

    //! Возвращает время заката в UNIX timestamp
    public long getTimeSunset()
    {
        return sys.sunset;
    }

    //! Возвращает id города OpenWeatherMap
    public long getCityID()
    {
        return cityID;
    }

    public String getIcon()
    {
        return weatherDesc.get(0).icon;
    }

    public Sys getSys()
    {
        return sys;
    }

    public Wind getWind()
    {
        return wind;
    }

    public DayTemperature getTemp()
    {
        return temp;
    }

    public Coords getCoords()
    {
        return coords;
    }

    ////////////////////////////сеттеры////////////////////////////

    //! Записывает id города OpenWeatherMap
    public void setCityID( long iCityID )
    {
        this.cityID = iCityID;
    }

    public void setWeatherID( int id )
    {
        weatherDesc.get(0).id = id;
    }

    public void setWeatherMain( String main )
    {
        weatherDesc.get(0).main = main;
    }

    //! Записывает название города
    public void setCityName( String sCity )
    {
        this.cityName = sCity;
    }

    //! Записывает широту города
    public void setCityLatitude( double dLat )
    {
        coords.lat = dLat;
    }

    //! Записывает долготу города
    public void setCityLongitude( double dLon )
    {
        coords.lon = dLon;
    }

    //! Записывает название страны
    public void setCountry( String sCountry )
    {
        sys.country = sCountry;
    }

    //! Записывает текущую температуру
    public void setCurrentTemp( double dCurrentTemp )
    {
        temp.temp = dCurrentTemp;
    }

    //! Записывает минимальную температуру на данный момент
    public void setTempMin(double dTempMin) {
        temp.temp_min = dTempMin;
    }

    //! Записывает максимальную температуру на данный момент
    public void setTempMax(double dTempMax) {
        temp.temp_max = dTempMax;
    }

    //! Записывает давление
    public void setPressure(double dPressure) {
        temp.pressure = dPressure;
    }

    //! Записывает влажность в процентах
    public void setHumidity(double dHumidity) {
        temp.humidity = dHumidity;
    }

    //! Записывает текущую дату и время в UNIX timestamp
    public void setTimeUpdate(long iDateTime) {
        this.dateTime = iDateTime;
    }

    //! Записывает скорость ветра в м/с
    public void setWindSpeed(double dWindSpeed) {
        wind.speed = dWindSpeed;
    }

    //! Записывает направление ветра в градусах (метеорологическое направление)
    public void setWindDeg(double dWindDeg) {
        wind.deg = dWindDeg;
    }

    //! Записывает время рассвета в UNIX timestamp
    public  void setTimeSunrise(long iTimeSunrise )
    {
        sys.sunrise = iTimeSunrise;
    }

    //! Записывает время заката в UNIX timestamp
    public void setTimeSunset( long iTimeSunset )
    {
        sys.sunset = iTimeSunset;
    }

    public void setIcon( String icon )
    {
        weatherDesc.get(0).icon = icon;
    }

    public void setDateTime( long dateTime )
    {
        this.dateTime = dateTime;
    }

    public void setCoords(Coords coords)
    {
        this.coords = coords;
    }

    public void setTemp(DayTemperature temp)
    {
        this.temp = temp;
    }

    public void setWeatherDesc(List<WeatherDesc> weatherDesc)
    {
        this.weatherDesc = weatherDesc;
    }

    public void setWind(Wind wind)
    {
        this.wind = wind;
    }

    public void setSys(Sys sys)
    {
        this.sys = sys;
    }
}
