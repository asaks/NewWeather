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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;

import com.asaks.newweather.api.ConstantsAPI;
import com.asaks.newweather.api.WeatherAPI;

import com.asaks.newweather.weather.WeatherDay;
import com.asaks.newweather.weather.WeatherForecast;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Класс основного окна приложения
 */
public class MainActivity extends AppCompatActivity
        implements DialogScreen.NoticeDialogListener {

    private static final String TAG_MAIN_ACTIVITY = "MainActivity";

    TabHost tabHost;

    WeatherAPI.InterfaceAPI api;
    ApplicationSettings applicationSettings;
    SharedPreferences sharedPreferences;
    RequestOptions requestOptionsGlide;

    WeatherDay weatherDay;
    WeatherForecast weatherForecast;
    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;

    boolean bDoubleBackPressedToExitOnce = false;

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

        tabHost = findViewById( R.id.tabHost );
        tabHost.setup();

        // вкладка с текущей погодой
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("current");
        tabSpec.setContent( R.id.llCurrentWeather );
        tabSpec.setIndicator( getString(R.string.weather_now) );
        tabHost.addTab( tabSpec );

        // вкладка с прогнозом погоды
        tabSpec = tabHost.newTabSpec( "forecast" );
        tabSpec.setContent( R.id.llForecast );
        tabSpec.setIndicator( getString( R.string.weather_forecast) );
        tabHost.addTab( tabSpec );

        // по умолчанию открывается вкладка с текущей погодой
        tabHost.setCurrentTab(0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        //Fragment fragmentMain = fragmentManager.findFragmentById(R.id.llCurrentWeather);

        weatherDay = new WeatherDay();
        weatherForecast = new WeatherForecast();

        ///////////////////////ТЕСТОВЫЕ ДАННЫЕ//////////////////////
        weatherDay.setCityID( 511565 );
        weatherDay.setTestData( "Пенза", 53.2,45.0,1523385261,
                276.1, 1006.31, 83, 4.31,
                193.505, "Ясно", 1524372257, 1524424277 );
        weatherDay.setWindDeg(276f);
        weatherDay.setCountry("ru");
        ////////////////////////////////////////////////////////////

        applicationSettings = new ApplicationSettings();
        sharedPreferences = getPreferences( Context.MODE_PRIVATE );
        if ( null != sharedPreferences )
        {
            applicationSettings.setUnitTemp( sharedPreferences.getInt( getString(R.string.tag_temp_units), GlobalMethodsAndConstants.TEMP_KELVIN ) );
            applicationSettings.setUnitPress( sharedPreferences.getInt( getString(R.string.tag_press_units), GlobalMethodsAndConstants.PRESS_MM_HG ) );
            applicationSettings.setCity( sharedPreferences.getString("city", "" ) );
        }

        //TODO при первом запуске приложения запрашивать город
        // если нет сохраненного города, то показываем диалог
        if ( applicationSettings.getCity().isEmpty() )
        {
            DialogScreen dlgInputCity = DialogScreen.newInstance( DialogScreen.IDD_SET_CITY, "" );
            dlgInputCity.show( getSupportFragmentManager(), "DlgSetCity" );
        }

        if ( null == /*fragmentMain*/savedInstanceState )
        {
            currentWeatherFragment = CurrentWeatherFragment.newInstance( applicationSettings, weatherDay );
            forecastFragment = ForecastFragment.newInstance( applicationSettings, weatherForecast );
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add( R.id.llScrollCurrentWeather, currentWeatherFragment );
            fragmentTransaction.add( R.id.llForecast, forecastFragment );
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        else
        {
            // делаем активной вкладкой прежнюю
            tabHost.setCurrentTab( savedInstanceState.getInt( getString(R.string.tag_current_tab) ) );
        }

        api = WeatherAPI.getClient().create(WeatherAPI.InterfaceAPI.class);
        requestOptionsGlide = new RequestOptions();
        requestOptionsGlide = requestOptionsGlide.diskCacheStrategy( DiskCacheStrategy.ALL );

    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState )
    {
        savedInstanceState.putInt( getString(R.string.tag_current_tab), tabHost.getCurrentTab() );

        super.onSaveInstanceState(savedInstanceState);
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
                intentSettings.putExtra( getString(R.string.tag_settings), applicationSettings );
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
                DialogScreen dlgAbout = DialogScreen.newInstance(DialogScreen.IDD_ABOUT, strAbout);

                dlgAbout.show( getSupportFragmentManager(), getString(R.string.tag_about_app) );
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
                applicationSettings = data.getParcelableExtra(getString(R.string.tag_settings));

                //TODO хранить настройки в базе данных
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt( getString(R.string.tag_temp_units), applicationSettings.getUnitTemp() );
                editor.putInt( getString(R.string.tag_press_units), applicationSettings.getUnitPress() );
                editor.putString( getString(R.string.tag_city), applicationSettings.getCity() );
                editor.apply();

                Fragment fragment = getSupportFragmentManager().findFragmentById( R.id.llScrollCurrentWeather );
                TextView tvCity = fragment.getView().findViewById( R.id.tvCityName );

                if ( !applicationSettings.getCity().equals( tvCity.getText().toString() ) )
                    updateWeather();
                else
                {
                    /*convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );
                    convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );*/
                    Intent intent = new Intent( GlobalMethodsAndConstants.INTENT_NEW_SETTINGS );
                    intent.putExtra( getString(R.string.tag_settings), applicationSettings );
                    LocalBroadcastManager.getInstance( getBaseContext() ).sendBroadcast( intent );
                }

            }
        }
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog )
    {

        int idDialog = ( (DialogScreen)dialog ).getDialogID();

        if ( DialogScreen.IDD_SET_CITY == idDialog )
        {
            Toast.makeText(this, ( (DialogScreen)dialog ).getCity().trim(), Toast.LENGTH_SHORT ).show();
            applicationSettings.setCity( ( (DialogScreen)dialog ).getCity().trim() );
            updateWeather();
        }

    }

    @Override
    public void onDialogNegativeClick( DialogFragment dialog )
    {

        int idDialog = ( (DialogScreen)dialog ).getDialogID();

        if ( DialogScreen.IDD_SET_CITY == idDialog && applicationSettings.getCity().isEmpty() )
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
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById( R.id.llScrollCurrentWeather );

        TextView tvCity = fragment.getView().findViewById( R.id.tvCityName );
        TextView tvCurrentTemp = fragment.getView().findViewById( R.id.tvCurrentTemp );
        TextView tvPressure = fragment.getView().findViewById( R.id.tvPressure );
        TextView tvHumidity = fragment.getView().findViewById( R.id.tvHumidity );

        String textMessage = getString(R.string.weather_in_city) + " " + tvCity.getText() + "\n";
        textMessage += getString(R.string.current_temp) + " " + tvCurrentTemp.getText() + "\n";
        textMessage += getString(R.string.pressure) + " " + tvPressure.getText() + "\n";
        textMessage += getString(R.string.humidity) + " " + tvHumidity.getText() + "\n";
        textMessage += getString(R.string.more) + " ";
        textMessage += ConstantsAPI.URL_CITY_WEATHER_FORECAST + weatherDay.getCityID();

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
        //TODO OpenWeatherMap рекомендуют запрашивать погоду по id города
        //TODO Как связать название города (на русском) с id?
        //TODO В city.list.json перечислены города с id и координатами, но как быть с одинаковыми названиями городов?

        Log.d( TAG_MAIN_ACTIVITY, "update weather" );

        //TODO прикрутить анимацию ожидания ответа при долгом выполнении запроса
        /*Call<WeatherDay> callCurrentWeather = api.getCurrentWeather( dLat, dLon,
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );*/

        /*Call<WeatherDay> callCurrentWeather = api.getCurrentWeather( idCity,
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );*/

        Call<WeatherDay> callCurrentWeather = api.getCurrentWeather( applicationSettings.getCity(),
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );

        callCurrentWeather.enqueue(
                new Callback<WeatherDay>()
                {
                    @Override
                    public void onResponse( @NonNull Call<WeatherDay> call, @NonNull Response<WeatherDay> response )
                    {
                        //WeatherDay weatherDay = response.body();
                        weatherDay = response.body();

                        if ( response.isSuccessful() )
                            if ( null != weatherDay )
                            {
                                // замена английского названия города на введенное пользователем
                                weatherDay.setCityName( applicationSettings.getCity() );
                                // замена времени обновления данных о текущей погоде на сервере на
                                // время обновления данных в приложении
                                Date timeUpd = new Date();
                                weatherDay.setTimeUpdate( timeUpd.getTime() / 1000  );

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

        Call<WeatherForecast> callWeatherForecast = api.getWeatherForecast( applicationSettings.getCity(),
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );

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
                        intent.putExtra( getString(R.string.tag_forecast), weatherForecast );
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
        intent.putExtra( getString(R.string.tag_weather), weatherDay );
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
