package com.asaks.newweather.fragments;

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

import com.asaks.newweather.ApplicationSettings;
import com.asaks.newweather.GlobalMethodsAndConstants;
import com.asaks.newweather.R;
import com.asaks.newweather.weather.WeatherDay;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Класс фрагмента, отображающего текущую погоду
 */
public class CurrentWeatherFragment extends Fragment
{
    private Unbinder unbinder;

    @BindView(R.id.tvCityName)
    TextView tvCity;

    @BindView(R.id.tvLatitude)
    TextView tvLat;

    @BindView(R.id.tvLongitude)
    TextView tvLon;

    @BindView(R.id.tvTimeUpdate)
    TextView tvDateTime;

    @BindView(R.id.tvCurrentTemp)
    TextView tvCurrentTemp;

    @BindView(R.id.tvPressure)
    TextView tvPressure;

    @BindView(R.id.tvHumidity)
    TextView tvHumidity;

    @BindView(R.id.tvWindSpeed)
    TextView tvWindSpeed;

    @BindView(R.id.tvWeatherDesc)
    TextView tvWeatherConditions;

    @BindView(R.id.ivWeatherCondition)
    ImageView ivWeatherCondition;

    @BindView(R.id.ivFlag)
    ImageView ivFlag;

    @BindView(R.id.tvSunrise)
    TextView tvSunrise;

    @BindView(R.id.tvSunset)
    TextView tvSunset;

    @BindView(R.id.ivThermometer)
    ImageView ivThermometer;

    @BindView(R.id.ivArrow)
    ImageView ivArrow;

    // опции загрузки изображений с помощью Glide
    static RequestOptions requestOptionsGlide;
    // настройки приложения
    ApplicationSettings applicationSettings = new ApplicationSettings();
    // данные о текущей погоде
    WeatherDay weatherDay = new WeatherDay();

    /**
     * Приемник новых данных о погоде
     */
    private final BroadcastReceiver brNewWeather = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            weatherDay = intent.getParcelableExtra( GlobalMethodsAndConstants.TAG_WEATHER );

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
            applicationSettings = intent.getParcelableExtra( GlobalMethodsAndConstants.TAG_SETTINGS );

            if ( applicationSettings != null )
            {
                tvCity.setText( applicationSettings.getCity() );
                convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );
                convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );
            }
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
        View view = inflater.inflate( R.layout.fragment_current_weather, container, false );

        unbinder = ButterKnife.bind( this, view );

        return view;
    }

    /**
     * Функция обновления пользовательского интерфейса.
     * Отображает принятые данные о погоде
     * @param weatherDay - данные о погоде
     */
    private void updateUi( WeatherDay weatherDay )
    {
        if ((weatherDay == null) || (weatherDay.getCityID() == 0))
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

        Date timeUpd = new Date( weatherDay.getDateTime() * 1000 );
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

        tvWeatherConditions.setText( weatherDay.getWeatherDescription() );

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
            if ( null != savedInstanceState )
            {
                this.weatherDay = savedInstanceState.getParcelable( GlobalMethodsAndConstants.TAG_WEATHER );
                this.applicationSettings = savedInstanceState.getParcelable( GlobalMethodsAndConstants.TAG_SETTINGS );
            }
            else
            {
                Bundle args = getArguments();

                if ( null != args )
                {
                    this.weatherDay = args.getParcelable( GlobalMethodsAndConstants.TAG_WEATHER );
                    this.applicationSettings = args.getParcelable( GlobalMethodsAndConstants.TAG_SETTINGS );
                }
            }
        }

        Context context = getContext();

        if ( context != null )
        {
            IntentFilter ifNewWeather = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_WEATHER);
            LocalBroadcastManager.getInstance( context ).registerReceiver( brNewWeather, ifNewWeather );

            IntentFilter ifNewSettings = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_SETTINGS);
            LocalBroadcastManager.getInstance( context ).registerReceiver( brNewSettings, ifNewSettings );
        }
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState )
    {
        if ( savedInstanceState != null )
        {
            savedInstanceState.putParcelable( GlobalMethodsAndConstants.TAG_WEATHER, weatherDay );
            savedInstanceState.putParcelable( GlobalMethodsAndConstants.TAG_SETTINGS, applicationSettings );
        }

        super.onSaveInstanceState( savedInstanceState );
    }

    @Override
    public void onResume()
    {
        super.onResume();

        updateUi( weatherDay );
    }

    @Override
    public void onDestroy()
    {
        Context context = getContext();

        if ( context != null )
        {
            LocalBroadcastManager.getInstance( context ).unregisterReceiver(brNewWeather);
            LocalBroadcastManager.getInstance( context ).unregisterReceiver(brNewSettings);
        }

        super.onDestroy();
    }

    @Override
    public void onDestroyView()
    {
        unbinder.unbind();

        super.onDestroyView();
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
