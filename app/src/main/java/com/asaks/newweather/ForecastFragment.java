package com.asaks.newweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asaks.newweather.weather.WeatherForecast;

public class ForecastFragment extends Fragment
{
    private WeatherForecast weatherForecast;
    private RecyclerView.Adapter mAdapter;

    private ApplicationSettings applicationSettings;

    /**
     * Приемник новых данных о настройках
     */
    private final BroadcastReceiver brNewSettings = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            applicationSettings = intent.getParcelableExtra( GlobalMethodsAndConstants.TAG_SETTINGS );
        }
    };

    /**
     * Приемник новых данных о прогнозе погоды
     */
    private final BroadcastReceiver brNewForecast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WeatherForecast forecast = intent.getParcelableExtra( GlobalMethodsAndConstants.TAG_FORECAST );
            weatherForecast.setWeatherItems( forecast.getWeatherItems() );
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * Функция создания нового фрагмента
     * @param applicationSettings - настройки приложения
     * @param weatherForecast - данные о прогнозе погоды
     * @return новый экземпляр фрагмента
     */
    public static ForecastFragment newInstance( ApplicationSettings applicationSettings, WeatherForecast weatherForecast )
    {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putParcelable( "settings", applicationSettings );
        args.putParcelable( "forecast", weatherForecast );
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        return inflater.inflate( R.layout.fragment_forecast, container, false );
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if ( null != view )
        {
            RecyclerView mRecyclerView = view.findViewById(R.id.rvForecast);

            if ( null == savedInstanceState )
            {
                Bundle args = getArguments();

                if ( null != args )
                {
                    this.weatherForecast = args.getParcelable( GlobalMethodsAndConstants.TAG_FORECAST );
                    this.applicationSettings = args.getParcelable( GlobalMethodsAndConstants.TAG_SETTINGS );
                }
            }
            else
            {
                this.weatherForecast = savedInstanceState.getParcelable( GlobalMethodsAndConstants.TAG_FORECAST );
                this.applicationSettings = savedInstanceState.getParcelable( GlobalMethodsAndConstants.TAG_SETTINGS );
            }

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new ForecastAdapter( weatherForecast );
            mRecyclerView.setAdapter( mAdapter );
        }

        Context context = getContext();

        if ( context != null )
        {
            IntentFilter ifNewSettings = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_SETTINGS);
            LocalBroadcastManager.getInstance( context ).registerReceiver( brNewSettings, ifNewSettings );

            IntentFilter ifNewForecast = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_FORECAST);
            LocalBroadcastManager.getInstance( context ).registerReceiver( brNewForecast, ifNewForecast );
        }

    }

    @Override
    public void onDestroy()
    {
        Context context = getContext();

        if ( context != null )
        {
            LocalBroadcastManager.getInstance( context ).unregisterReceiver(brNewForecast);
            LocalBroadcastManager.getInstance( context ).unregisterReceiver(brNewSettings);
        }

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState )
    {
        if ( savedInstanceState != null )
        {
            savedInstanceState.putParcelable( GlobalMethodsAndConstants.TAG_FORECAST, weatherForecast );
            savedInstanceState.putParcelable( GlobalMethodsAndConstants.TAG_SETTINGS, applicationSettings );
        }

        super.onSaveInstanceState( savedInstanceState );
    }
}
