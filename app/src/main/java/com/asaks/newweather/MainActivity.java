package com.asaks.newweather;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
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
                    WeatherDay weatherDay = AppDatabase.getInstance().getCurrentWeather();

                    if ( weatherDay == null )
                        weatherDay = new WeatherDay();

                    return CurrentWeatherFragment.newInstance( applicationSettings, weatherDay );
                }
                case GlobalMethodsAndConstants.PAGE_FORECAST_WEATHER:
                {
                    List<WeatherDay> lst = db.weatherDayDao().getForecast();

                    if ( lst != null && !lst.isEmpty() )
                    {
                        for ( int i = 0; i < lst.size(); i++ )
                        {
                            long idd = lst.get(i).getIdd();
                            long idTemp = lst.get(i).getIdTemp();
                            long idWind = lst.get(i).getIdWind();
                            long idSys = lst.get(i).getIdSys();

                            lst.get(i).setTemp( db.weatherDayDao().getTemp( idTemp ) );
                            lst.get(i).setWind( db.weatherDayDao().getWind( idWind ) );
                            lst.get(i).setSys( db.weatherDayDao().getSys( idSys ) );

                            lst.get(i).setWeatherDesc( db.weatherDayDao().getWeatherDesc( idd ) );
                        }

                        weatherForecast.setWeatherItems( lst );
                    }

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

    @BindView(R.id.pager)
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    WeatherAPI.InterfaceAPI api;
    ApplicationSettings applicationSettings;
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

        applicationSettings = db.settingsDao().getSettings();

        if ( applicationSettings == null )
        {
            applicationSettings = new ApplicationSettings();
            applicationSettings.setLon( 0.0 );
            applicationSettings.setLat( 0.0 );
            applicationSettings.setUnitPressPos( GlobalMethodsAndConstants.PRESS_MM_HG );
            applicationSettings.setUnitTempPos( GlobalMethodsAndConstants.TEMP_KELVIN );
            applicationSettings.setCity( "" );

            DialogInputCity dlgInputCity = DialogInputCity.newInstance();
            dlgInputCity.show( getSupportFragmentManager(), "DlgSetCity" );

        }

        ButterKnife.bind(this);
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
                    String sCity = db.settingsDao().getSettings().getCity();

                    // если названия не совпадаю - значит сменили город
                    if ( sCity != null && !sCity.equals( applicationSettings.getCity() ) )
                    {
                        // удаляем старую запись о погоде, т.к. сменили город
                        List<WeatherDay> lst = db.weatherDayDao().getCurrentWeather();
                        if ( !lst.isEmpty() && lst.get(0) != null )
                            db.weatherDayDao().delete( lst.get(0) );

                        updateWeather();
                    }

                    db.settingsDao().deleteAll();
                    db.settingsDao().insert( applicationSettings );

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

        db.settingsDao().deleteAll();
        db.settingsDao().insert( applicationSettings );

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
                        {
                            if (null != weatherDay)
                            {
                                //TODO а если координаты должны быть (0,0)?
                                if (applicationSettings.getLon() == 0.0 && applicationSettings.getLat() == 0.0) {
                                    applicationSettings.setLat(weatherDay.getLatitude());
                                    applicationSettings.setLon(weatherDay.getLongitude());

                                    db.settingsDao().deleteAll();
                                    db.settingsDao().insert(applicationSettings);
                                }

                                // замена английского названия города на введенное пользователем
                                weatherDay.setCityName(applicationSettings.getCity());
                                // замена времени обновления данных о текущей погоде на сервере на
                                // время обновления данных в приложении
                                Date timeUpd = new Date();
                                weatherDay.setTimeUpdate(timeUpd.getTime() / 1000);

                                weatherDay.setCurrentOrForecast(GlobalMethodsAndConstants.IDD_CURRENT_WEATHER);

                                //TODO вынести в отдельный поток
                                //TODO пока перед сохранением удаляю все записи
                                List<WeatherDay> lstTemp = db.weatherDayDao().getCurrentWeather();

                                if (lstTemp != null && !lstTemp.isEmpty()) {
                                    WeatherDay dayInBd = lstTemp.get(0);

                                    if (dayInBd != null) {
                                        db.weatherDayDao().deleteAllCoordData();
                                        db.weatherDayDao().deleteTempData(dayInBd.getIdTemp());
                                        db.weatherDayDao().deleteSysData(dayInBd.getIdSys());
                                        db.weatherDayDao().deleteWindData(dayInBd.getIdWind());
                                        db.weatherDayDao().deleteWeatherDescData(dayInBd.getIdd());
                                    }

                                    db.weatherDayDao().deleteWeather(GlobalMethodsAndConstants.IDD_CURRENT_WEATHER);
                                }

                                weatherDay.setIdCoords(db.weatherDayDao().saveCoords(weatherDay.getCoords()));
                                weatherDay.setIdTemp(db.weatherDayDao().saveTemp(weatherDay.getTemp()));
                                weatherDay.setIdWind(db.weatherDayDao().saveWind(weatherDay.getWind()));
                                weatherDay.setIdSys(db.weatherDayDao().saveSys(weatherDay.getSys()));

                                weatherDay.setIdd(db.weatherDayDao().saveWeatherDay(weatherDay));

                                for (int i = 0; i < weatherDay.getWeatherDesc().size(); i++)
                                    weatherDay.getWeatherDesc().get(i).setIdWeatherDay(weatherDay.getIdd());

                                db.weatherDayDao().saveWeatherDesc(weatherDay.getWeatherDesc());

                                updateUICurrentWeather(weatherDay);
                            }
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

                        for ( int j = 0; j < weatherForecast.getWeatherItems().size(); j++ )
                        {
                            WeatherDay item = weatherForecast.getWeatherItems().get(j);

                            if ( item != null )
                            {
                                db.weatherDayDao().deleteWeather( GlobalMethodsAndConstants.IDD_WEATHER_FORECAST );

                                item.setIdCoords( db.weatherDayDao().saveCoords(item.getCoords()));
                                item.setIdTemp( db.weatherDayDao().saveTemp(item.getTemp()) );
                                item.setIdWind( db.weatherDayDao().saveWind(item.getWind()) );
                                item.setIdSys( db.weatherDayDao().saveSys(item.getSys() ));

                                item.setIdd( db.weatherDayDao().saveWeatherDay(item) );

                                for ( int i = 0; i < item.getWeatherDesc().size(); i++ )
                                    item.getWeatherDesc().get(i).setIdWeatherDay( item.getIdd() );

                                db.weatherDayDao().saveWeatherDesc( item.getWeatherDesc() );
                            }
                        }

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
