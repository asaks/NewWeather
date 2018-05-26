package com.asaks.newweather.weather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;
import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaks on 13.04.18.
 *
 * Класс, содержащий информацию о погоде на день
 */


@Entity(tableName = "data_weather", foreignKeys = {@ForeignKey(entity = WeatherDay.Sys.class, parentColumns = "id", childColumns = "id_sys",
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),@ForeignKey(entity = WeatherDay.Wind.class, parentColumns = "id", childColumns = "id_wind",
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),@ForeignKey(entity = WeatherDay.DayTemperature.class, parentColumns = "id", childColumns = "id_temp",
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),@ForeignKey(entity = WeatherDay.Coords.class,parentColumns = "id", childColumns = "id_coords",
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class WeatherDay implements Parcelable
{
    //! Класс, содержащий географические координаты города
    @Entity(tableName = "coords")
    public static class Coords
    {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        long idCoord;
        // широта города
        @ColumnInfo(name = "lat")
        @SerializedName("lat")
        double lat;
        // долгота города
        @ColumnInfo(name = "lon")
        @SerializedName("lon")
        double lon;

        public long getIdCoord() {
            return idCoord;
        }

        public void setIdCoord(long idCoord) {
            this.idCoord = idCoord;
        }

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
    @Entity(tableName = "temperature")
    public static class DayTemperature
    {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        long idTemp;
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

        public long getIdTemp() {
            return idTemp;
        }

        public void setIdTemp(long idTemp) {
            this.idTemp = idTemp;
        }

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
    @Entity(tableName = "weather")
    public static class WeatherDesc
    {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        long idWeather;

        @ColumnInfo(name = "id_weather_day")
        @ForeignKey(entity = WeatherDay.class, parentColumns = "id", childColumns = "id_weather_day",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        long idWeatherDay;

        // weatherId погодных условий в OpenWeatherMap
        @ColumnInfo(name = "weather_id")
        @SerializedName("weatherId")
        int weatherId;
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

        public long getIdWeatherDay() {
            return idWeatherDay;
        }

        public void setIdWeatherDay(long idWeatherDay) {
            this.idWeatherDay = idWeatherDay;
        }

        public long getIdWeather() {
            return idWeather;
        }

        public void setIdWeather(long idWeather) {
            this.idWeather = idWeather;
        }

        public int getWeatherId() {
            return weatherId;
        }

        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
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
    @Entity(tableName = "wind")
    public static class Wind
    {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        long idWind;
        // скорость ветра
        @ColumnInfo(name = "speed")
        @SerializedName("speed")
        double speed;
        // направление (метеорологическое) ветра, в градусах
        @ColumnInfo(name = "wind_deg")
        @SerializedName("deg")
        double deg;

        public long getIdWind() {
            return idWind;
        }

        public void setIdWind(long idWind) {
            this.idWind = idWind;
        }

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
    @Entity(tableName = "sys")
    public static class Sys
    {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        long idSys;
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

        public long getIdSys() {
            return idSys;
        }

        public void setIdSys(long idSys) {
            this.idSys = idSys;
        }

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

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long idd;

    @ColumnInfo(name = "current_or_forecast")
    private long currentOrForecast;

    @ColumnInfo(name = "id_coords")
    private long idCoords;

    @ColumnInfo(name = "id_temp")
    private long idTemp;

    @ColumnInfo(name = "id_wind")
    private long idWind;

    @ColumnInfo(name = "id_sys")
    private long idSys;

    // название города
    @ColumnInfo(name = "city_name")
    @SerializedName("name")
    private String cityName;

    // id города OpenWeatherMap
    @SerializedName("id")
    private long cityID;

    @Ignore
    @SerializedName("coord")
    private Coords coords;

    // температура
    @Ignore
    @SerializedName("main")
    private DayTemperature temp;

    // описание погодных условий
    @Ignore
    @SerializedName("weather")
    private List<WeatherDesc> weatherDesc;

    @Ignore
    @SerializedName("wind")
    private Wind wind;

    // дата и время
    @ColumnInfo(name = "datetime_update")
    @SerializedName("dt")
    private long dateTime;

    @Ignore
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

    //! Возвращает URL иконки погоды
    public String getWeatherIconUrl()
    {
        if ( weatherDesc.isEmpty() )
            return "";

        return "http://openweathermap.org/img/w/" + weatherDesc.get(0).icon + ".png";
    }

    //! Возвращает URL иконки флага страны
    public String getCountryFlagUrl()
    {
        if ( sys.country == null || sys.country.isEmpty() )
            return "";

        return "http://openweathermap.org/images/flags/" + sys.country.toLowerCase() + ".png";
    }

    public long getIdd() {
        return idd;
    }

    public void setIdd(long idd) {
        this.idd = idd;
    }

    public long getCurrentOrForecast() {
        return currentOrForecast;
    }

    public void setCurrentOrForecast(long currentOrForecast) {
        this.currentOrForecast = currentOrForecast;
    }

    public long getIdCoords() {
        return idCoords;
    }

    public void setIdCoords(long idCoords) {
        this.idCoords = idCoords;
    }

    public long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(long idTemp) {
        this.idTemp = idTemp;
    }

    public long getIdWind() {
        return idWind;
    }

    public void setIdWind(long idWind) {
        this.idWind = idWind;
    }

    public long getIdSys() {
        return idSys;
    }

    public void setIdSys(long idSys) {
        this.idSys = idSys;
    }

    public int getWeatherID()
    {
        return weatherDesc.get(0).weatherId;
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
        if ( weatherDesc.isEmpty() )
            return "";

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

    //! Возвращает weatherId города OpenWeatherMap
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

    //! Записывает weatherId города OpenWeatherMap
    public void setCityID( long iCityID )
    {
        this.cityID = iCityID;
    }

    public void setWeatherID( int id )
    {
        weatherDesc.get(0).weatherId = id;
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
