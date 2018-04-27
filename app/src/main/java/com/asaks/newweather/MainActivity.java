package com.asaks.newweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;

import com.asaks.newweather.api.ConstantsAPI;
import com.asaks.newweather.api.WeatherAPI;

import com.asaks.newweather.weather.WeatherDay;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NewWeather";

    TabHost tabHost;

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

    WeatherAPI.InterfaceAPI api;
    ApplicationSettings applicationSettings;
    SharedPreferences sharedPreferences;
    RequestOptions requestOptionsGlide;

    //TODO во время отрисовки могут придти новые данные и возможно половина отрисуется старых данных, а половина новых
    WeatherDay weatherDay;

    final int REQUEST_CODE_SETTINGS = 0;

    //! Константы типа географических координат
    private class GeoCoordsConstant
    {
        public static final int COORD_LAT = 0; // широта
        public static final int COORD_LON = 1; // долгота
    }

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    //! Русские названия месяцев
    private static DateFormatSymbols russiansMonths = new DateFormatSymbols()
    {
        @Override
        public String[] getMonths()
        {
            return new String[]{MainActivity.getContext().getString(R.string.january),
                    MainActivity.getContext().getString(R.string.february),
                    MainActivity.getContext().getString(R.string.march),
                    MainActivity.getContext().getString(R.string.april),
                    MainActivity.getContext().getString(R.string.may),
                    MainActivity.getContext().getString(R.string.june),
                    MainActivity.getContext().getString(R.string.july),
                    MainActivity.getContext().getString(R.string.august),
                    MainActivity.getContext().getString(R.string.september),
                    MainActivity.getContext().getString(R.string.october),
                    MainActivity.getContext().getString(R.string.november),
                    MainActivity.getContext().getString(R.string.december)};
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

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

        tvCity = findViewById( R.id.tvCityName );
        tvLat = findViewById( R.id.tvLatitude );
        tvLon = findViewById( R.id.tvLongitude );
        tvDateTime = findViewById( R.id.tvTimeUpdate );
        tvCurrentTemp = findViewById( R.id.tvCurrentTemp );
        tvPressure = findViewById( R.id.tvPressure );
        tvHumidity = findViewById( R.id.tvHumidity );
        tvWindSpeed = findViewById( R.id.tvWindSpeed );
        tvWeatherConditions = findViewById( R.id.tvWeatherDesc );
        ivWeatherCondition = findViewById( R.id.ivWeatherCondition );
        ivFlag = findViewById( R.id.ivFlag );
        tvSunrise = findViewById( R.id.tvSunrise );
        tvSunset = findViewById( R.id.tvSunset );
        ivThermometer = findViewById( R.id.ivThermometer );
        ivArrow = findViewById( R.id.ivArrow );

        api = WeatherAPI.getClient().create(WeatherAPI.InterfaceAPI.class);
        requestOptionsGlide = new RequestOptions();
        requestOptionsGlide.diskCacheStrategy( DiskCacheStrategy.ALL );

        applicationSettings = new ApplicationSettings();
        sharedPreferences = getPreferences( Context.MODE_PRIVATE );
        if ( null != sharedPreferences )
        {
            applicationSettings.setUnitTemp( sharedPreferences.getInt( getString(R.string.tag_temp_units), Constants.TEMP_KELVIN ) );
            applicationSettings.setUnitPress( sharedPreferences.getInt( getString(R.string.tag_press_units), Constants.PRESS_MM_HG ) );
        }

        //TODO при первом запуске приложения запрашивать город
        ///////////////////////ТЕСТОВЫЕ ДАННЫЕ//////////////////////
        weatherDay = new WeatherDay();
        weatherDay.setTestData( "Пенза", 53.2,45.0,1523385261,
                276.1, 1006.31, 83, 4.31,
                193.505, "Ясно", 1524372257, 1524424277 );
        weatherDay.setWindDeg(153f);

        tvCity.setText( weatherDay.getCityName() );
        String sLatitude = String.valueOf( weatherDay.getLatitude() ) + getString(R.string.gradus)
                + " " + getStringDesignationCoords( weatherDay.getLatitude(), GeoCoordsConstant.COORD_LAT );
        tvLat.setText( sLatitude );
        String sLongitude = String.valueOf( weatherDay.getLongitude() ) + getString(R.string.gradus)
                + " " + getStringDesignationCoords( weatherDay.getLongitude(), GeoCoordsConstant.COORD_LON );
        tvLon.setText( sLongitude );
        Date timeUpd = new Date( weatherDay.getTimeUpdate() * 1000 );
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", russiansMonths).format(timeUpd) );
        tvCurrentTemp.setText( String.valueOf( toCelsius( weatherDay.getCurrentTemp() ) ) + " " + getString(R.string.gradus) + getString(R.string.Celcius) );
        double dPressure =  toMmHg( weatherDay.getPressure() );
        tvPressure.setText( String.format("%.2f", dPressure ) + " " + getString(R.string.mmHg) );
        tvHumidity.setText( String.valueOf( Math.round( weatherDay.getHumidity() ) ) + getString(R.string.procent) );
        tvWindSpeed.setText( String.valueOf( weatherDay.getWindSpeed() ) + " " + getString(R.string.meter_per_sec) );
        tvWeatherConditions.setText( weatherDay.getWeatherDesc() );
        tvSunrise.setText( new SimpleDateFormat("HH:mm")
                            .format( new Date( weatherDay.getTimeSunrise() * 1000 ) ) );
        tvSunset.setText( new SimpleDateFormat("HH:mm")
                            .format( new Date( weatherDay.getTimeSunset() * 1000 ) ) );

        float angle = 180 + (float)weatherDay.getWindDeg();
        ivArrow.setRotation( angle );
        ////////////////////////////////////////////////////////////

        convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );
        convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );
    }

    //! Функция возвращает контекст
    public static Context getContext()
    {
        return mContext;
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
                startActivityForResult( intentSettings, REQUEST_CODE_SETTINGS );

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
                    PackageInfo pinfo = getPackageManager().getPackageInfo( getPackageName(),0 );
                    strAbout += this.getPackageManager().getPackageInfo( getPackageName(),0 ).versionName + "-"
                        + pinfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                DialogAbout dlgAbout = DialogAbout.newInstance( strAbout );

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
        this.finishAffinity();
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == REQUEST_CODE_SETTINGS )
            {
                //TODO пока смотрю только единицы измерения температуры и давления
                //TODO используется глобальный объект weatherDay
                //int t = data.getIntExtra("tempGradus", Constants.TEMP_KELVIN );
                applicationSettings = data.getParcelableExtra(getString(R.string.tag_settings));
                convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );
                convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );

                // записываем установленные настройки единиц температуры
                //applicationSettings.setUnitTemp( t );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt( getString(R.string.tag_temp_units), applicationSettings.getUnitTemp() );
                editor.putInt( getString(R.string.tag_press_units), applicationSettings.getUnitPress() );
                editor.commit();
            }
        }
    }

    //! "Поделиться погодой"
    private void shareWeather()
    {
        //TODO используется глобальный объект weatherDay
        String textMessage = getString(R.string.weather_in_city) +" " + tvCity.getText() + "\n";
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

    //! Отображает температуру в указанных единицах измерения
    private void convertTemperature(int unit, double temp)
    {
        Resources resources = getResources();

        switch ( unit )
        {
            case Constants.TEMP_CELSIUS: // градусы Цельсия
            {
                // если грузить Glide'ом , то нужно делать ресайз изображения, иначе оно слишком большое
                //Glide.with(this).load( R.mipmap.ic_thermometer_celsius ).into(ivThermometer);
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_celsius) );
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s%s", toCelsius(temp),
                        getString(R.string.gradus), getString(R.string.Celcius) ) );
                break;
            }
            case Constants.TEMP_FARENHEIT: // градусы Фаренгейта
            {
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_farenheit) );
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s%s", toFarenheit(temp),
                        getString(R.string.gradus), getString(R.string.Farenheit) ) );
                break;
            }
            case Constants.TEMP_KELVIN: // Кельвины
            default:
            {
                ivThermometer.setImageDrawable(resources.getDrawable(R.mipmap.ic_thermometer));
                tvCurrentTemp.setText( String.format( Locale.getDefault(), "%d %s", Math.round(temp),
                        getString(R.string.Kelvin) ) );
            }
        }
    }

    //! Отображает давление в указанных единицах измерения
    private void convertPressure( int unit, double press )
    {
        if ( Constants.PRESS_HPA == unit )
            tvPressure.setText( String.format( Locale.getDefault(), "%.2f %s", press, getString(R.string.gektopascal) ) );
        else if ( Constants.PRESS_MM_HG == unit )
            tvPressure.setText( String.format( Locale.getDefault(), "%.2f %s", toMmHg( press ), getString(R.string.mmHg) ) );
    }

    //! Функция конвертации температуры из Кельвинов в градусы Цельсия
    private long toCelsius( double dTempKelvin )
    {
        double dCelsiusTemp = dTempKelvin - 273.15;

        return Math.round( dCelsiusTemp );
    }

    //! Функция конвертации температуры из Кельвинов в градусы Фаренгейта
    private long toFarenheit( double dTempKelvin )
    {
        double dTempF = 1.8 * ( dTempKelvin - 273.15 ) + 32;

        return Math.round( dTempF );
    }

    //! Функция конвертации гектопаскалей в мм рт. ст.
    private double toMmHg(double dhPa )
    {
        return dhPa * 0.750064;
    }

    //! Функция возвращает описание географических координат
    private String getStringDesignationCoords(double dCoord, int coordConst )
    {
        String sDesignation;

        switch( coordConst )
        {
            case GeoCoordsConstant.COORD_LAT: // широта
            {
                if ( 0 > dCoord )
                    sDesignation = getString( R.string.south_lat); // ю.ш.
                else
                    sDesignation = getString( R.string.north_lat); // с.ш.

                break;
            }
            case GeoCoordsConstant.COORD_LON: // долгота
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

    //! Обновить данные о погоде
    private void updateWeather()
    {
        updateCurrentWeather();

        updateWeatherForecast();
    }

    //! Обновить данные о текущей погоде
    private void updateCurrentWeather()
    {
        //TODO для теста захардкодил координаты, OpenWeatherMap рекомендует делать запрос по id города
        //TODO В city.list.json перечислены города с id и координатами, но как быть с одинаковыми названиями городов?
        double dLat = 53.2;
        double dLon = 45.0;
        long idCity = 511565;

        Log.d( TAG, "update weather" );

        //TODO прикрутить анимацию ожидания ответа при долгом выполнении запроса
        /*Call<WeatherDay> callCurrentWeather = api.getCurrentWeather( dLat, dLon,
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );*/

        Call<WeatherDay> callCurrentWeather = api.getCurrentWeather( idCity,
                ConstantsAPI.DEFAULT_LANG, ConstantsAPI.APIKEY );

        callCurrentWeather.enqueue(
                new Callback<WeatherDay>()
                {
                    @Override
                    public void onResponse( Call<WeatherDay> call, Response<WeatherDay> response )
                    {
                        //WeatherDay weatherDay = response.body();
                        weatherDay = response.body();

                        if ( response.isSuccessful() )
                            if ( null != weatherDay )
                                updateUICurrentWeather( weatherDay );
                    }

                    @Override
                    public void onFailure( Call<WeatherDay> call, Throwable t )
                    {
                        Log.e( TAG,"fail update weather" );
                        Log.e( TAG, t.toString() );

                        Toast toast = Toast.makeText( getBaseContext(),
                                getString( R.string.err_update_weather) + "\n"
                                        + t.toString(), Toast.LENGTH_SHORT );
                        toast.setGravity( Gravity.BOTTOM, 0, 100 );
                        toast.show();
                    }
                }
        );
    }

    //! Обновить прогноз погоды
    private void updateWeatherForecast()
    {
        //TODO прогноз погоды
    }

    //! Обновление вкладки текущей погоды
    private void updateUICurrentWeather( WeatherDay weatherDay )
    {
        //TODO название города на русском языке
        tvCity.setText( weatherDay.getCityName() );

        String sLatitude = String.valueOf( weatherDay.getLatitude() ) + getString( R.string.gradus)
                + " " + getStringDesignationCoords( weatherDay.getLatitude(), GeoCoordsConstant.COORD_LAT );
        tvLat.setText( sLatitude );

        String sLongitude = String.valueOf( weatherDay.getLongitude() ) + getString( R.string.gradus)
                + " " + getStringDesignationCoords( weatherDay.getLongitude(), GeoCoordsConstant.COORD_LON );
        tvLon.setText( sLongitude );

        //Date timeUpd = new Date( weatherDay.getTimeUpdate() * 1000 );
        Date timeUpd = new Date();
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", russiansMonths ).format( timeUpd ) );

        convertTemperature( applicationSettings.getUnitTemp(), weatherDay.getCurrentTemp() );

        tvHumidity.setText( String.format( Locale.getDefault(), "%d %s", Math.round( weatherDay.getHumidity() ),
                getString( R.string.procent) ) );

        convertPressure( applicationSettings.getUnitPress(), weatherDay.getPressure() );

        tvWindSpeed.setText( String.format( Locale.getDefault(), "%.2f %s", weatherDay.getWindSpeed(),
                getString( R.string.meter_per_sec) ) );

        // угол, приходящий по запросу показывает куда дует ветер, а не откуда
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

}
