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
            applicationSettings = intent.getParcelableExtra( getString(R.string.tag_settings) );
        }
    };

    /**
     * Приемник новых данных о прогнозе погоды
     */
    private final BroadcastReceiver brNewForecast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WeatherForecast forecast = intent.getParcelableExtra( getString(R.string.tag_forecast) );
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
        return inflater.inflate( R.layout.forecast_layout, container, false );
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
                    this.weatherForecast = args.getParcelable( getString(R.string.tag_forecast) );
                    this.applicationSettings = args.getParcelable( getString(R.string.tag_settings) );
                }
            }
            else
            {
                this.weatherForecast = savedInstanceState.getParcelable( getString(R.string.tag_forecast) );
                this.applicationSettings = savedInstanceState.getParcelable( getString(R.string.tag_settings) );
            }

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new ForecastAdapter( weatherForecast );
            mRecyclerView.setAdapter( mAdapter );
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        IntentFilter ifNewSettings = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_SETTINGS);
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( brNewSettings, ifNewSettings );

        IntentFilter ifNewForecast = new IntentFilter(GlobalMethodsAndConstants.INTENT_NEW_FORECAST);
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( brNewForecast, ifNewForecast );
    }

    @Override
    public void onPause()
    {
        super.onPause();

        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(brNewForecast);
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(brNewSettings);
    }

    @Override
    public void onSaveInstanceState( @NonNull Bundle savedInstanceState )
    {
        savedInstanceState.putParcelable( getString(R.string.tag_forecast), weatherForecast );
        savedInstanceState.putParcelable( getString(R.string.tag_settings), applicationSettings );

        super.onSaveInstanceState( savedInstanceState );
    }
}
