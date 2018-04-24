package com.asaks.newweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.asaks.newweather.weather.ApplicationSettings;

import com.asaks.newweather.weather.WeatherDay;
import com.bumptech.glide.Glide;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    WeatherAPI.InterfaceAPI api;
    ApplicationSettings applicationSettings;
    SharedPreferences sharedPreferences;

    long cityID;
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
            return new String[]{MainActivity.getContext().getString(R.string.strJanuary),
                    MainActivity.getContext().getString(R.string.strFebruary),
                    MainActivity.getContext().getString(R.string.strMarch),
                    MainActivity.getContext().getString(R.string.strApril),
                    MainActivity.getContext().getString(R.string.strMay),
                    MainActivity.getContext().getString(R.string.strJune),
                    MainActivity.getContext().getString(R.string.strJuly),
                    MainActivity.getContext().getString(R.string.strAugust),
                    MainActivity.getContext().getString(R.string.strSeptember),
                    MainActivity.getContext().getString(R.string.strOctober),
                    MainActivity.getContext().getString(R.string.strNovember),
                    MainActivity.getContext().getString(R.string.strDecember)};
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
        tabSpec.setIndicator( getString(R.string.strWeatherNow) );
        tabHost.addTab( tabSpec );

        // вкладка с прогнозом погоды
        tabSpec = tabHost.newTabSpec( "forecast" );
        tabSpec.setContent( R.id.llForecast );
        tabSpec.setIndicator( getString( R.string.strPrognoz ) );
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

        api = WeatherAPI.getClient().create(WeatherAPI.InterfaceAPI.class);

        applicationSettings = new ApplicationSettings();
        sharedPreferences = getPreferences( Context.MODE_PRIVATE );
        if ( null != sharedPreferences )
        {
            applicationSettings.setUnitTempPos( sharedPreferences.getInt("tempGradus", Constants.TEMP_KELVIN ) );
        }

        ///////////////////////ТЕСТОВЫЕ ДАННЫЕ//////////////////////
        weatherDay = new WeatherDay();
        weatherDay.setTestData( "Пенза", 53.2,45.0,1523385261,
                276.1, 1006.31, 83, 4.31,
                193.505, "Ясно", 1524372257, 1524424277 );

        tvCity.setText( weatherDay.getCityName() );
        String sLatitude = String.valueOf( weatherDay.getLatitude() ) + getString(R.string.strGradus)
                + " " + getStringDesignationCoords( weatherDay.getLatitude(), GeoCoordsConstant.COORD_LAT );
        tvLat.setText( sLatitude );
        String sLongitude = String.valueOf( weatherDay.getLongitude() ) + getString(R.string.strGradus)
                + " " + getStringDesignationCoords( weatherDay.getLongitude(), GeoCoordsConstant.COORD_LON );
        tvLon.setText( sLongitude );
        Date timeUpd = new Date( weatherDay.getTimeUpdate() * 1000 );
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", russiansMonths).format(timeUpd) );
        tvCurrentTemp.setText( String.valueOf( toCelsius( weatherDay.getCurrentTemp() ) ) + " " + getString(R.string.strGradus) + getString(R.string.strCelcius) );
        double dPressure =  toMmHg( weatherDay.getPressure() );
        tvPressure.setText( String.format("%.2f", dPressure ) + " " + getString(R.string.strMmHg) );
        tvHumidity.setText( String.valueOf( Math.round( weatherDay.getHumidity() ) ) + getString(R.string.strProcent) );
        tvWindSpeed.setText( String.valueOf( weatherDay.getWindSpeed() ) + " " + getString(R.string.strMeterPerSec) );
        tvWeatherConditions.setText( weatherDay.getWeatherDesc() );
        tvSunrise.setText( new SimpleDateFormat("HH:mm")
                            .format( new Date( weatherDay.getTimeSunrise() * 1000 ) ) );
        tvSunset.setText( new SimpleDateFormat("HH:mm")
                            .format( new Date( weatherDay.getTimeSunset() * 1000 ) ) );
        ////////////////////////////////////////////////////////////

        convertTemperature( applicationSettings.getUnitTempPos() );
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
        //TODO

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
                intentSettings.putExtra( "tempGradus", applicationSettings.getUnitTempPos() );
                startActivityForResult( intentSettings, REQUEST_CODE_SETTINGS );

                return true;
            }
            case R.id.action_share: // поделиться
            {
                //TODO дополнить данными о температуре, давлении, влажности
                //TODO и добавить к ссылке "Подробнее"
                String textMessage = getString(R.string.weatherInCity) +" " + tvCity.getText() + "\n";
                textMessage += ConstantsAPI.URL_CITY_WEATHER_FORECAST + cityID;

                Intent intentShare = new Intent();
                intentShare.setAction( Intent.ACTION_SEND );
                intentShare.putExtra( Intent.EXTRA_TEXT, textMessage );
                intentShare.setType( "text/plain" );

                if ( null != intentShare.resolveActivity( getPackageManager() ) )
                    startActivity( intentShare );

                return true;
            }
            case R.id.action_about: // о приложении
                return true;
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
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == REQUEST_CODE_SETTINGS )
            {
                //TODO пока смотрю только единицы измерения температуры
                int t = data.getIntExtra("tempGradus", Constants.TEMP_KELVIN );
                convertTemperature( t );

                // записываем установленные настройки единиц температуры
                applicationSettings.setUnitTempPos( t );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt( "tempGradus", t );
                editor.commit();
            }
        }
    }

    //! Отображает температуру в указанных единицах измерения
    private void convertTemperature( int unit )
    {
        Resources resources = getResources();

        switch ( unit )
        {
            case Constants.TEMP_CELSIUS: // градусы Цельсия
            {
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_celsius) );
                tvCurrentTemp.setText( String.valueOf( toCelsius(weatherDay.getCurrentTemp()) )
                        + getString(R.string.strGradus) + getString(R.string.strCelcius) );
                break;
            }
            case Constants.TEMP_FARENHEIT: // градусы Фаренгейта
            {
                ivThermometer.setImageDrawable( resources.getDrawable(R.mipmap.ic_thermometer_farenheit) );
                tvCurrentTemp.setText( String.valueOf( toFarenheit(weatherDay.getCurrentTemp()) )
                        + getString(R.string.strGradus) + getString(R.string.strFarenheit) );
                break;
            }
            case Constants.TEMP_KELVIN: // Кельвины
            default:
            {
                ivThermometer.setImageDrawable(resources.getDrawable(R.mipmap.ic_thermometer));
                tvCurrentTemp.setText( String.valueOf( Math.round( weatherDay.getCurrentTemp() ) )
                        + getString(R.string.strKelvin) );
            }
        }
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
                    sDesignation = getString( R.string.strSouthLat ); // ю.ш.
                else
                    sDesignation = getString( R.string.strNorthLat ); // с.ш.

                break;
            }
            case GeoCoordsConstant.COORD_LON: // долгота
            {
                if ( 0 > dCoord )
                    sDesignation = getString( R.string.strWestLon ); // з.д.
                else
                    sDesignation = getString( R.string.strEastLon ); // в.д.

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
        //TODO сохранять id города для отправки ссылки на погоду
        cityID = idCity;

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
                                getString( R.string.strErrUpdateWeather ) + "\n"
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
        //TODO
    }

    //! Обновление вкладки текущей погоды
    private void updateUICurrentWeather( WeatherDay weatherDay )
    {
        //TODO название города на русском языке
        tvCity.setText( weatherDay.getCityName() );

        String sLatitude = String.valueOf( weatherDay.getLatitude() ) + getString( R.string.strGradus )
                + " " + getStringDesignationCoords( weatherDay.getLatitude(), GeoCoordsConstant.COORD_LAT );
        tvLat.setText( sLatitude );

        String sLongitude = String.valueOf( weatherDay.getLongitude() ) + getString( R.string.strGradus )
                + " " + getStringDesignationCoords( weatherDay.getLongitude(), GeoCoordsConstant.COORD_LON );
        tvLon.setText( sLongitude );

        //Date timeUpd = new Date( weatherDay.getTimeUpdate() * 1000 );
        Date timeUpd = new Date();
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", russiansMonths ).format( timeUpd ) );

        //TODO сделать настройку градусов, пока всегда переводится в градусы Цельсия
        tvCurrentTemp.setText( String.valueOf( toCelsius( weatherDay.getCurrentTemp() ) )
                + " " + getString(R.string.strGradus) + getString(R.string.strCelcius) );

        tvHumidity.setText( String.valueOf( Math.round( weatherDay.getHumidity() ) ) + getString( R.string.strProcent ) );

        double dPressure =  toMmHg( weatherDay.getPressure() );
        tvPressure.setText( String.format("%.2f", dPressure ) + " " + getString( R.string.strMmHg ) );

        tvWindSpeed.setText( String.valueOf( weatherDay.getWindSpeed() ) + " " + getString( R.string.strMeterPerSec ) );

        tvWeatherConditions.setText( weatherDay.getWeatherDesc() );

        Date timeSunrise = new Date( weatherDay.getTimeSunrise() * 1000 );
        tvSunrise.setText( new SimpleDateFormat( "HH:mm" ).format( timeSunrise ) );

        Date timeSunset = new Date( weatherDay.getTimeSunset() * 1000 );
        tvSunset.setText( new SimpleDateFormat( "HH:mm" ).format( timeSunset ) );

        Glide.with( this ).load( weatherDay.getWeatherIconUrl() ).into( ivWeatherCondition );
        Glide.with( this ).load( weatherDay.getCountryFlagUrl() ).into( ivFlag );
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
    }
}
