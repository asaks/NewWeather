package com.asaks.newweather;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TabHost;

import com.asaks.newweather.weather.WeatherDay;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

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

    //! Константы типа географических координат
    private class GeoCoordsConstant
    {
        public static final int COORD_LAT = 0; // широта
        public static final int COORD_LON = 1; // долгота
    }

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
        tabSpec.setContent( R.id.llPrognoz );
        tabSpec.setIndicator( getString( R.string.strPrognoz ) );
        tabHost.addTab( tabSpec );

        // по умолчанию открывается вкладка с текущей погодой
        tabHost.setCurrentTab(0);


        ///////////////////////ТЕСТОВЫЕ ДАННЫЕ//////////////////////
        WeatherDay weatherDay = new WeatherDay();
        weatherDay.setTestData( "Пенза", 53.2,45.0,1523385261,
                276.1, 1006.31, 83, 4.31,
                193.505, "Ясно");
        ////////////////////////////////////////////////////////////

        tvCity = findViewById( R.id.tvCityName );
        tvCity.setText( weatherDay.getCityName() );
        tvLat = findViewById( R.id.tvLatitude );
        String sLatitude = String.valueOf( weatherDay.getLatitude() ) + getString(R.string.strGradus)
                + " " + getStringDesignationCoords( weatherDay.getLatitude(), GeoCoordsConstant.COORD_LAT );
        tvLat.setText( sLatitude );
        tvLon = findViewById( R.id.tvLongitude );
        String sLongitude = String.valueOf( weatherDay.getLongitude() ) + getString(R.string.strGradus)
                + " " + getStringDesignationCoords( weatherDay.getLongitude(), GeoCoordsConstant.COORD_LON );
        tvLon.setText( sLongitude );
        tvDateTime = findViewById( R.id.tvTimeUpdate );
        Date timeUpd = new Date( weatherDay.getDateTime() * 1000 );
        tvDateTime.setText( new SimpleDateFormat("dd MMMM HH:mm", russiansMonths).format(timeUpd) );
        tvCurrentTemp = findViewById( R.id.tvCurrentTemp );
        tvCurrentTemp.setText( String.valueOf( toCelsius( weatherDay.getCurrentTemp() ) ) + " " + getString(R.string.strGradus) + getString(R.string.strCelcius) );
        tvPressure = findViewById( R.id.tvPressure );
        double dPressure =  toMmMercuryColumn( weatherDay.getPressure() );
        tvPressure.setText( String.format("%.2f", dPressure ) + " " + getString(R.string.strMmHg) );
        tvHumidity = findViewById( R.id.tvHumidity );
        tvHumidity.setText( String.valueOf( weatherDay.getHumidity() ) + getString(R.string.strProcent) );
        tvWindSpeed = findViewById( R.id.tvWindSpeed );
        tvWindSpeed.setText( String.valueOf( weatherDay.getWindSpeed() ) + " " + getString(R.string.strMeterToSec) );
        tvWeatherConditions = findViewById( R.id.tvWeatherDesc );
        tvWeatherConditions.setText( weatherDay.getWeatherDesc() );
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
                return true;
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

    //! Функция конвертации температуры из Кельвинов в градусы Цельсия
    private long toCelsius(double dTempKelvin )
    {
        double dCelsiusTemp = dTempKelvin - 273.15;

        return Math.round( dCelsiusTemp );
    }

    //! Функция конвертации температуры из Кельвинов в градусы Фаренгейта
    private long toFarengheit( double dTempKelvin )
    {
        double dTempF = 1.8 * ( dTempKelvin - 273.15 ) + 32;

        return Math.round( dTempF );
    }

    //! Функция конвертации гектопаскалей в мм рт. ст.
    private double toMmMercuryColumn( double dhPa )
    {
        double dMm = dhPa * 0.750064;

        return dMm;
    }

    //! Функция возвращает описание географических координат
    private String getStringDesignationCoords(double dCoord, int coordConst )
    {
        String sDesignation = "";

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
        //TODO
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
    }
}
