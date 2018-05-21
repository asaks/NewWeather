package com.asaks.newweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.asaks.newweather.api.ConstantsWeatherAPI;
import com.asaks.newweather.api.WeatherAPI;

import com.asaks.newweather.db.AppDatabase;
import com.asaks.newweather.db.WeatherDatabase;
import com.asaks.newweather.dialogs.DialogAbout;
import com.asaks.newweather.dialogs.DialogInputCity;
import com.asaks.newweather.fragments.CurrentWeatherFragment;
import com.asaks.newweather.fragments.ForecastFragment;
import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Класс основного окна приложения
 */
public class MainActivity extends AppCompatActivity
        implements DialogInputCity.NoticeDialogListener {

    private static final String TAG_MAIN_ACTIVITY = "MainActivity";

    /**
     * Вспомогательный класс. Адаптер для показа вкладок
     */
    private class WeatherFragmentPagerAdapter extends FragmentPagerAdapter
    {
        SparseArray<Fragment> saCreatedFragments = new SparseArray<>();

        WeatherFragmentPagerAdapter( FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch ( position )
            {
                case GlobalMethodsAndConstants.PAGE_CURRENT_WEATHER:
                {
                    List<WeatherDay> lst = db.weatherDayDao().getCurrentWeather();
                    WeatherDay weatherDay = new WeatherDay();

                    if ( !lst.isEmpty() )
                        weatherDay = lst.get(0);

                    return CurrentWeatherFragment.newInstance( applicationSettings, weatherDay );
                }
                case GlobalMethodsAndConstants.PAGE_FORECAST_WEATHER:
                {
                    return ForecastFragment.newInstance( applicationSettings, weatherForecast );
                }
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case GlobalMethodsAndConstants.PAGE_CURRENT_WEATHER:
                {
                    return GlobalMethodsAndConstants.TITLE_FIRST_TAB;
                }
                case GlobalMethodsAndConstants.PAGE_FORECAST_WEATHER:
                {
                    return GlobalMethodsAndConstants.TITLE_SECOND_TAB;
                }
                default:
                    return "";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            Fragment createdFragment = (Fragment)super.instantiateItem(container, position);
            saCreatedFragments.put( position, createdFragment );

            return createdFragment;
        }

        /**
         * Функция возвращает фрагмент по номеру его позиции
         * @param position
         * @return фрагмент или null, если фрагмента с таким номером позиции не существует
         */
        public Fragment getFragmentByPosition( int position )
        {
            return saCreatedFragments.get( position, null );
        }
    }

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    WeatherAPI.InterfaceAPI api;
    ApplicationSettings applicationSettings;
    SharedPreferences sharedPreferences;
    RequestOptions requestOptionsGlide;

    WeatherForecast weatherForecast;

    boolean bDoubleBackPressedToExitOnce = false;

    WeatherDatabase db = AppDatabase.getInstance().getWeatherDatabase();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if ( null != actionBar )
        {
            actionBar.setDisplayShowHomeEnabled( true );
            actionBar.setIcon( R.mipmap.ic_launcher );
        }

        weatherForecast = new WeatherForecast();

        applicationSettings = new ApplicationSettings();
        sharedPreferences = getPreferences( Context.MODE_PRIVATE );
        if ( null != sharedPreferences )
        {
            applicationSettings.setUnitTemp( sharedPreferences.getInt( GlobalMethodsAndConstants.TAG_TEMP_UNITS,
                    GlobalMethodsAndConstants.TEMP_KELVIN ) );
            applicationSettings.setUnitPress( sharedPreferences.getInt( GlobalMethodsAndConstants.TAG_PRESS_UNITS,
                    GlobalMethodsAndConstants.PRESS_MM_HG ) );
            applicationSettings.setCity( sharedPreferences.getString(GlobalMethodsAndConstants.TAG_CITY,
                    "" ) );
            applicationSettings.setLat( sharedPreferences.getFloat( GlobalMethodsAndConstants.TAG_LAT, 0 ) );
            applicationSettings.setLon( sharedPreferences.getFloat( GlobalMethodsAndConstants.TAG_LON, 0 ) );
        }

        //TODO при первом запуске приложения запрашивать город
        // если нет сохраненного города, то показываем диалог
        if ( applicationSettings.getCity().isEmpty() )
        {
            DialogInputCity dlgInputCity = DialogInputCity.newInstance();
            dlgInputCity.show( getSupportFragmentManager(), "DlgSetCity" );
        }
        else
        {
            List<WeatherDay> lst = db.weatherDayDao().getCurrentWeather();

            if ( !lst.isEmpty() )
                // т.к. у нас пока только один город, всегда берем первую (и единственную) запись
                updateUICurrentWeather( lst.get(0) );
            else
                Toast.makeText(this,"пусто",Toast.LENGTH_SHORT).show();
        }

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new WeatherFragmentPagerAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( pagerAdapter );

        api = WeatherAPI.getClient().create(WeatherAPI.InterfaceAPI.class);
        requestOptionsGlide = new RequestOptions();
        requestOptionsGlide = requestOptionsGlide.diskCacheStrategy( DiskCacheStrategy.ALL );

    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }
    
    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        return super.onPrepareOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.action_update: // обновить
            {
                updateWeather();
                return true;
            }
            case R.id.action_settings: // настройки
            {
                Intent intentSettings = new Intent( this, SettingsActivity.class );
                intentSettings.putExtra( GlobalMethodsAndConstants.TAG_SETTINGS, applicationSettings );
                startActivityForResult( intentSettings, GlobalMethodsAndConstants.REQUEST_CODE_SETTINGS );

                return true;
            }
            case R.id.action_share: // поделиться
            {
                shareWeather();

                return true;
            }
            case R.id.action_about: // о приложении
            {
                String strAbout = getString( R.string.message_about) + "\n\n";
                strAbout += getString( R.string.version ) + ": ";
                try {
                    strAbout += getPackageManager().getPackageInfo( getPackageName(),0 ).versionName + "-"
                        + getPackageManager().getPackageInfo( getPackageName(),0 ).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                DialogAbout dlgAbout = DialogAbout.newInstance( strAbout );

                dlgAbout.show( getSupportFragmentManager(), GlobalMethodsAndConstants.TAG_ABOUT_APP );
                return true;
            }
            case R.id.action_exit: // выход
            {
                this.finishAffinity();
                return true;
            }

            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onBackPressed()
    {
        if ( bDoubleBackPressedToExitOnce )
        {
            //super.onBackPressed();
            this.finishAffinity();
            return;
        }

        this.bDoubleBackPressedToExitOnce = true;
        Toast toast = Toast.makeText( getBaseContext(),
                getString( R.string.hint_double_press_exit ),
                Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.BOTTOM, 0, GlobalMethodsAndConstants.Y_OFFSET_TOAST );

        toast.show();

        new Handler().postDelayed( new Runnable()
        {
            @Override
            public void run()
            {
                bDoubleBackPressedToExitOnce = false;
            }
        }, 2000 );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == GlobalMethodsAndConstants.REQUEST_CODE_SETTINGS )
            {
                applicationSettings = data.getParcelableExtra(GlobalMethodsAndConstants.TAG_SETTINGS);

                if ( applicationSettings != null )
                {
                    SharedPreferences sf = getPreferences( Context.MODE_PRIVATE );
                    String sCity = sf.getString( GlobalMethodsAndConstants.TAG_CITY, "" );

                    // если названия не совпадаю - значит сменили город
                    if ( !sCity.equals( applicationSettings.getCity() ) )
                    {
                        // удаляем старую запись о погоде, т.к. сменили город
                        List<WeatherDay> lst = db.weatherDayDao().getCurrentWeather();
                        if ( !lst.isEmpty() && lst.get(0) != null )
                            db.weatherDayDao().delete( lst.get(0) );

                        updateWeather();
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt( GlobalMethodsAndConstants.TAG_TEMP_UNITS, applicationSettings.getUnitTemp() );
                    editor.putInt( GlobalMethodsAndConstants.TAG_PRESS_UNITS, applicationSettings.getUnitPress() );
                    editor.putString( GlobalMethodsAndConstants.TAG_CITY, applicationSettings.getCity() );
                    editor.putFloat( GlobalMethodsAndConstants.TAG_LAT, (float)applicationSettings.getLat() );
                    editor.putFloat( GlobalMethodsAndConstants.TAG_LON, (float)applicationSettings.getLon() );
                    editor.apply();

                    Intent intent = new Intent( GlobalMethodsAndConstants.INTENT_NEW_SETTINGS );
                    intent.putExtra( GlobalMethodsAndConstants.TAG_SETTINGS, applicationSettings );
                    LocalBroadcastManager.getInstance( this ).sendBroadcast( intent );

                }
            }
        }
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog )
    {
        applicationSettings.setCity( ( (DialogInputCity)dialog ).getCity().trim() );
        applicationSettings.setLat( ( (DialogInputCity)dialog ).getLatitude() );
        applicationSettings.setLon( ( (DialogInputCity)dialog ).getLongitude() );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( GlobalMethodsAndConstants.TAG_CITY, applicationSettings.getCity() );
        editor.putFloat( GlobalMethodsAndConstants.TAG_LAT, (float)applicationSettings.getLat() );
        editor.putFloat( GlobalMethodsAndConstants.TAG_LON, (float)applicationSettings.getLon() );
        editor.apply();

        updateWeather();
    }

    @Override
    public void onDialogNegativeClick( DialogFragment dialog )
    {
        if ( applicationSettings.getCity().isEmpty() )
        {
            //TODO диалог вызывается в двух местах: при первом запуске и из настроек
            // как отловить что вызов был при первом запуске? пока проверяю что в классе
            // настроек название города пусто - тогда завершаю приложение
            this.finishAffinity();
        }
    }

    /**
     * Поделиться погодой
     */
    private void shareWeather()
    {
        String textMessage = "";
        Fragment fragment = ( (WeatherFragmentPagerAdapter)pagerAdapter )
                .getFragmentByPosition(GlobalMethodsAndConstants.PAGE_CURRENT_WEATHER);

        if ( fragment != null )
        {
            View view = fragment.getView();

            if ( view != null )
            {
                List<WeatherDay> lst = db.weatherDayDao().getCurrentWeather();
                long idCity = 0;

                if ( !lst.isEmpty() )
                    idCity = lst.get(0).getCityID();

                TextView tvCity = view.findViewById( R.id.tvCityName );
                TextView tvCurrentTemp = view.findViewById( R.id.tvCurrentTemp );
                TextView tvPressure = view.findViewById( R.id.tvPressure );
                TextView tvHumidity = view.findViewById( R.id.tvHumidity );

                textMessage = getString(R.string.weather_in_city) + " " + tvCity.getText() + "\n";
                textMessage += getString(R.string.current_temp) + " " + tvCurrentTemp.getText() + "\n";
                textMessage += getString(R.string.pressure) + " " + tvPressure.getText() + "\n";
                textMessage += getString(R.string.humidity) + " " + tvHumidity.getText() + "\n";
                textMessage += getString(R.string.more) + " ";
                textMessage += ConstantsWeatherAPI.URL_CITY_WEATHER_FORECAST + idCity;
            }
        }

        Intent intentShare = new Intent();
        intentShare.setAction( Intent.ACTION_SEND );
        intentShare.putExtra( Intent.EXTRA_TEXT, textMessage );
        intentShare.setType( "text/plain" );

        if ( null != intentShare.resolveActivity( getPackageManager() ) )
            startActivity( intentShare );
    }

    /**
     * Обновить данные о погоде
     */
    private void updateWeather()
    {
        updateCurrentWeather();

        updateWeatherForecast();
    }

    /**
     * Обновить данные о текущей погоде
     */
    private void updateCurrentWeather()
    {
        Log.d( TAG_MAIN_ACTIVITY, "update weather" );
        Call<WeatherDay> callCurrentWeather;

        //TODO прикрутить анимацию ожидания ответа при долгом выполнении запроса

        if ( applicationSettings.getLat() != 0.0 && applicationSettings.getLon() != 0.0 )
        {
            callCurrentWeather = api.getCurrentWeather( applicationSettings.getLat(),
                    applicationSettings.getLon(), ConstantsWeatherAPI.DEFAULT_LANG, ConstantsWeatherAPI.APIKEY );
        }
        else
            callCurrentWeather = api.getCurrentWeather( applicationSettings.getCity(),
                    ConstantsWeatherAPI.DEFAULT_LANG, ConstantsWeatherAPI.APIKEY );


        callCurrentWeather.enqueue(
                new Callback<WeatherDay>()
                {
                    @Override
                    public void onResponse( @NonNull Call<WeatherDay> call, @NonNull Response<WeatherDay> response )
                    {
                        WeatherDay weatherDay = response.body();

                        if ( response.isSuccessful() )
                            if ( null != weatherDay )
                            {
                                //TODO а если координаты должны быть (0,0)?
                                if ( applicationSettings.getLon() == 0.0 && applicationSettings.getLat() == 0.0 )
                                {
                                    applicationSettings.setLat( weatherDay.getLatitude() );
                                    applicationSettings.setLon( weatherDay.getLongitude() );

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString( GlobalMethodsAndConstants.TAG_CITY, applicationSettings.getCity() );
                                    editor.putFloat( GlobalMethodsAndConstants.TAG_LAT, (float)applicationSettings.getLat() );
                                    editor.putFloat( GlobalMethodsAndConstants.TAG_LON, (float)applicationSettings.getLon() );
                                    editor.apply();
                                }

                                // замена английского названия города на введенное пользователем
                                weatherDay.setCityName( applicationSettings.getCity() );
                                // замена времени обновления данных о текущей погоде на сервере на
                                // время обновления данных в приложении
                                Date timeUpd = new Date();
                                weatherDay.setTimeUpdate( timeUpd.getTime() / 1000  );

                                //TODO вынести в отдельный поток
                                if ( db.weatherDayDao().isExist( weatherDay.getCityID() ) )
                                    db.weatherDayDao().update(weatherDay);
                                else
                                    db.weatherDayDao().insert(weatherDay);

                                updateUICurrentWeather( weatherDay );
                            }
                        else
                        {
                            try
                            {
                                if ( null != response.body() )
                                {
                                    Toast toast = Toast.makeText( getBaseContext(),
                                            getString(R.string.err_update_current_weather) + "\n"
                                                    + response.errorBody().string(), Toast.LENGTH_SHORT );
                                    toast.setGravity( Gravity.BOTTOM, 0, GlobalMethodsAndConstants.Y_OFFSET_TOAST );
                                    toast.show();
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure( @NonNull Call<WeatherDay> call, @NonNull Throwable t )
                    {
                        Log.e( TAG_MAIN_ACTIVITY,"fail update weather" );
                        Log.e( TAG_MAIN_ACTIVITY, t.toString() );

                        Toast toast = Toast.makeText( getBaseContext(),
                                getString( R.string.err_update_current_weather) + "\n"
                                        + t.toString(), Toast.LENGTH_SHORT );
                        toast.setGravity( Gravity.BOTTOM, 0, GlobalMethodsAndConstants.Y_OFFSET_TOAST );
                        toast.show();
                    }
                }
        );
    }

    /**
     * Обновить прогноз погоды
     */
    private void updateWeatherForecast()
    {
        Log.d( TAG_MAIN_ACTIVITY, "update weather forecast" );

        Call<WeatherForecast> callWeatherForecast = api.getWeatherForecast( applicationSettings.getLat(),
                applicationSettings.getLon(), ConstantsWeatherAPI.DEFAULT_LANG, ConstantsWeatherAPI.APIKEY );

        callWeatherForecast.enqueue( new Callback<WeatherForecast>() {
            @Override
            public void onResponse( @NonNull Call<WeatherForecast> call, @NonNull Response<WeatherForecast> response )
            {
                if ( response.isSuccessful() )
                {
                    weatherForecast = response.body();

                    if ( null != weatherForecast )
                    {
                        //weatherForecast.setWeatherItems( wf.getWeatherItems() );
                        //mAdapter.notifyDataSetChanged();

                        Intent intent = new Intent( GlobalMethodsAndConstants.INTENT_NEW_FORECAST );
                        intent.putExtra( GlobalMethodsAndConstants.TAG_FORECAST, weatherForecast );
                        LocalBroadcastManager.getInstance( getBaseContext() ).sendBroadcast( intent );
                    }
                }
                else
                {
                    try
                    {
                        if ( null != response.errorBody() )
                        {
                            Toast toast = Toast.makeText( getBaseContext(),
                                    getString(R.string.err_update_forecast_weather) + "\n"
                                            + response.errorBody().string(), Toast.LENGTH_SHORT);
                            toast.setGravity( Gravity.BOTTOM, 0, GlobalMethodsAndConstants.Y_OFFSET_TOAST );
                            toast.show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure( @NonNull Call<WeatherForecast> call, @NonNull Throwable t )
            {
                Log.e( TAG_MAIN_ACTIVITY,"fail update weather forecast" );
                Log.e( TAG_MAIN_ACTIVITY, t.toString() );

                Toast toast = Toast.makeText( getBaseContext(),
                        getString(R.string.err_update_forecast_weather) + "\n"
                                + t.toString(), Toast.LENGTH_SHORT );
                toast.setGravity( Gravity.BOTTOM, 0, GlobalMethodsAndConstants.Y_OFFSET_TOAST );
                toast.show();
            }
        });
    }

    /**
     * Обновление вкладки текущей погоды
     * @param weatherDay - объект с данными о текущей погоде
     */
    private void updateUICurrentWeather( WeatherDay weatherDay )
    {
        Intent intent = new Intent( GlobalMethodsAndConstants.INTENT_NEW_WEATHER);
        intent.putExtra( GlobalMethodsAndConstants.TAG_WEATHER, weatherDay );
        LocalBroadcastManager.getInstance( getBaseContext() ).sendBroadcast( intent );
    }

    /**
     * Функция возвращает объект с настройками приложения
     * @return объект, содержащий настройки приложения
     */
    public ApplicationSettings getApplicationSettings()
    {
        return applicationSettings;
    }
}
