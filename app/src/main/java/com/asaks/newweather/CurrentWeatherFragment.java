package com.asaks.newweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asaks.newweather.weather.WeatherDay;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Класс фрагмента, отображающего текущую погоду
 */
public class CurrentWeatherFragment extends Fragment
{
    TextView tvCity;
    TextView tvLat;
    TextView tvLon;
    TextView tvDateTime;
    TextView tvCurrentTemp;
    TextView tvPressure;
    TextView tvHumidity;
    TextView tvWindSpeed;
    TextView tvWeatherConditions;
    ImageView ivWeatherCondition;
    ImageView ivFlag;
    TextView tvSunrise;
    TextView tvSunset;
    ImageView ivThermometer;
    ImageView ivArrow;

    // опции загрузки изображений с помощью Glide
    static RequestOptions requestOptionsGlide;
    // настройки приложения
    ApplicationSettings applicationSettings;
    // данные о текущей погоде
    WeatherDay weatherDay;

    /**
     * Приемник новых данных о погоде
     */
    private final BroadcastReceiver brNewWeather = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            weatherDay = intent.getParcelableExtra( getString(R.string.tag_weather) );

            if ( null != weatherDay )
                updateUi( weatherDay );
        }
    };

    /**
     * Приемник новых данных о настройках
     */
    private final BroadcastReceiver brNewSettings = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            applicationSettings = intent.getParcelableExtra( getString(R.string.tag_settings) );

            tvCity.setText( applicationSettings.getCity() );
            convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );
            convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );
        }
    };

    /**
     * Функция создания нового фрагмента
     * @param applicationSettings - настройки приложения
     * @param weatherDay - данные о погоде
     * @return новый экземпляр фрагмента
     */
    public static CurrentWeatherFragment newInstance( ApplicationSettings applicationSettings, WeatherDay weatherDay)
    {
        requestOptionsGlide = new RequestOptions();
        requestOptionsGlide = requestOptionsGlide.diskCacheStrategy( DiskCacheStrategy.ALL );

        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable( "settings", applicationSettings );
        args.putParcelable( "weather", weatherDay );
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        return inflater.inflate( R.layout.current_weather_layout, container, false );
    }

    /**
     * Функция обновления пользовательского интерфейса.
     * Отображает принятые данные о погоде
     * @param weatherDay - данные о погоде
     */
    private void updateUi( @NonNull WeatherDay weatherDay )
    {
        if ( weatherDay == null )
            return;

        tvCity.setText( weatherDay.getCityName().toUpperCase( Locale.getDefault() ) );

        String sLatitude = String.format( Locale.getDefault(), "%.2f %s%s",
                weatherDay.getLatitude(), getString( R.string.gradus),
                getStringDesignationCoords( weatherDay.getLatitude(), GlobalMethodsAndConstants.COORD_LAT ) );

        tvLat.setText( sLatitude );

        String sLongitude = String.format( Locale.getDefault(), "%.2f %s%s",
                weatherDay.getLongitude(), getString( R.string.gradus),
                getStringDesignationCoords( weatherDay.getLongitude(), GlobalMethodsAndConstants.COORD_LON ) );

        tvLon.setText( sLongitude );

        Date timeUpd = new Date( weatherDay.getTimeUpdate() * 1000 );
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault() ).format( timeUpd ) );

        convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );

        tvHumidity.setText( String.format( Locale.getDefault(), "%d %s",
                Math.round( weatherDay.getHumidity() ),
                getString( R.string.procent) ) );

        convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );

        tvWindSpeed.setText( String.format( Locale.getDefault(), "%.2f %s",
                weatherDay.getWindSpeed(),
                getString( R.string.meter_per_sec) ) );

        // угол, приходящий по запросу показывает откуда дует ветер, а не куда
        // поэтому такие манипуляции
        float angle = 180 + (float)weatherDay.getWindDeg();
        ivArrow.setRotation( angle );

        tvWeatherConditions.setText( weatherDay.getWeatherDesc() );

        Date timeSunrise = new Date( weatherDay.getTimeSunrise() * 1000 );
        tvSunrise.setText( new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( timeSunrise ) );

        Date timeSunset = new Date( weatherDay.getTimeSunset() * 1000 );
        tvSunset.setText( new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( timeSunset ) );

        Glide.with( this ).load( weatherDay.getWeatherIconUrl() )
                .apply(requestOptionsGlide)
                .into( ivWeatherCondition );
        Glide.with( this ).load( weatherDay.getCountryFlagUrl() )
                .apply(requestOptionsGlide)
                .into( ivFlag );
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if ( null != view )
        {
            tvCity = view.findViewById( R.id.tvCityName );
            tvLat = view.findViewById( R.id.tvLatitude );
            tvLon = view.findViewById( R.id.tvLongitude );
            tvDateTime = view.findViewById( R.id.tvTimeUpdate );
            tvCurrentTemp = view.findViewById( R.id.tvCurrentTemp );
            tvPressure = view.findViewById( R.id.tvPressure );
            tvHumidity = view.findViewById( R.id.tvHumidity );
            tvWindSpeed = view.findViewById( R.id.tvWindSpeed );
            tvWeatherConditions = view.findViewById( R.id.tvWeatherDesc );
            ivWeatherCondition = view.findViewById( R.id.ivWeatherCondition);
            ivFlag = view.findViewById( R.id.ivFlag );
            tvSunrise = view.findViewById( R.id.tvSunrise );
            tvSunset = view.findViewById( R.id.tvSunset );
            ivThermometer = view.findViewById( R.id.ivThermometer );
            ivArrow = view.findViewById( R.id.ivArrow );

            if ( null != savedInstanceState )
            {
                this.weatherDay = savedInstanceState.getParcelable( getString(R.string.tag_weather) );
                this.applicationSettings = savedInstanceState.getParcelable( getString(R.string.tag_settings) );
            }
            else
            {
                Bundle args = getArguments();

                if ( null != args )
                {
                    this.weatherDay = args.getParcelable( getString(R.string.tag_weather) );
                    this.applicationSettings = args.getParcelable( getString(R.string.tag_settings) );
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState( @NonNull Bundle savedInstanceState )
    {
        savedInstanceState.putParcelable( getString(R.string.tag_weather), weatherDay );
        savedInstanceState.putParcelable( getString(R.string.tag_settings), applicationSettings );

        super.onSaveInstanceState( savedInstanceState );
    }

    @Override
    public void onResume()
    {
        super.onResume();

        IntentFilter ifNewWeather = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_WEATHER);
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( brNewWeather, ifNewWeather );

        IntentFilter ifNewSettings = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_SETTINGS);
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( brNewSettings, ifNewSettings );

        updateUi( weatherDay );
    }

    @Override
    public void onPause()
    {
        super.onPause();

        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(brNewWeather);
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(brNewSettings);
    }

    /**
     * Функция возвращает описание географических координат
     * @param dCoord - координаты
     * @param coordConst - тип координат (широта/долгота)
     * @return строковое описание географических координат
     */
    private String getStringDesignationCoords( double dCoord, int coordConst )
    {
        String sDesignation;

        switch( coordConst )
        {
            case GlobalMethodsAndConstants.COORD_LAT: // широта
            {
                if ( 0 > dCoord )
                    sDesignation = getString( R.string.south_lat); // ю.ш.
                else
                    sDesignation = getString( R.string.north_lat); // с.ш.

                break;
            }
            case GlobalMethodsAndConstants.COORD_LON: // долгота
            {
                if ( 0 > dCoord )
                    sDesignation = getString( R.string.west_lon); // з.д.
                else
                    sDesignation = getString( R.string.east_lon); // в.д.

                break;
            }

            default:
                sDesignation = "";
        }

        return sDesignation;
    }

    /**
     * Отображает температуру в указанных единицах измерения
     * @param unit - единицы измерения
     * @param temp - температура в кельвинах
     */
    private void convertTemperature( int unit, double temp )
    {
        Resources resources = getResources();

        switch ( unit )
        {
            case GlobalMethodsAndConstants.TEMP_CELSIUS: // градусы Цельсия
            {
                // если грузить Glide'ом , то нужно делать ресайз изображения, иначе оно слишком большое
                //Glide.with(this).load( R.mipmap.ic_thermometer_celsius ).into(ivThermometer);
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_celsius) );
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s%s",
                        GlobalMethodsAndConstants.toCelsius(temp),
                        getString(R.string.gradus), getString(R.string.Celcius) ) );
                break;
            }
            case GlobalMethodsAndConstants.TEMP_FARENHEIT: // градусы Фаренгейта
            {
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_farenheit) );
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s%s",
                        GlobalMethodsAndConstants.toFarenheit(temp),
                        getString(R.string.gradus), getString(R.string.Farenheit) ) );
                break;
            }
            case GlobalMethodsAndConstants.TEMP_KELVIN: // Кельвины
            default:
            {
                ivThermometer.setImageDrawable(resources.getDrawable(R.mipmap.ic_thermometer));
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s",
                        Math.round(temp),
                        getString(R.string.Kelvin) ) );
            }
        }
    }

    /**
     * Отображает давление в указанных единицах измерения
     * @param unit - единицы измерения
     * @param press - давление в гектопаскалях
     */
    private void convertPressure( int unit, double press )
    {
        if ( GlobalMethodsAndConstants.PRESS_HPA == unit )
            tvPressure.setText( String.format( Locale.getDefault(), "%.2f %s", press,
                    getString(R.string.gektopascal) ) );
        else if ( GlobalMethodsAndConstants.PRESS_MM_HG == unit )
            tvPressure.setText( String.format( Locale.getDefault(), "%.2f %s",
                    GlobalMethodsAndConstants.toMmHg( press ),
                    getString(R.string.mmHg) ) );
    }
}
