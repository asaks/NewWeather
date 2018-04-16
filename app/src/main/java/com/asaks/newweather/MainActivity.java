package com.asaks.newweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TabHost;

import com.asaks.newweather.weather.CurrentWeather;

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
    //TextView tvWindDir;
    TextView tvWeatherConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = findViewById( R.id.tabHost );
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("current");
        tabSpec.setContent( R.id.llCurrentWeather );
        tabSpec.setIndicator( getString(R.string.strWeatherNow) );
        tabHost.addTab( tabSpec );

        tabSpec = tabHost.newTabSpec( "prognoz" );
        tabSpec.setContent( R.id.llPrognoz );
        tabSpec.setIndicator( getString( R.string.strPrognoz ) );
        tabHost.addTab( tabSpec );

        tabHost.setCurrentTab(0);


        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTestData( "Пенза", 53.2,45.0,1523385261,
                276.1, 1006.31, 83, 4.31,
                193.505, "Ясно", "Чистое небо" );

        tvCity = findViewById( R.id.tvCityName );
        tvCity.setText( currentWeather.getCityName() );
        tvLat = findViewById( R.id.tvLatitude );
        tvLat.setText( String.valueOf( currentWeather.getLatitude() ) + getString(R.string.strGradus) );
        tvLon = findViewById( R.id.tvLongitude );
        tvLon.setText( String.valueOf( currentWeather.getLongitude() ) + getString(R.string.strGradus) );
        tvDateTime = findViewById( R.id.tvTimeUpdate );
        Date timeUpd = new Date( currentWeather.getDateTime() * 1000 );
        tvDateTime.setText( new SimpleDateFormat("HH:mm").format(timeUpd) );
        tvCurrentTemp = findViewById( R.id.tvCurrentTemp );
        double dTempC = toCelsius( currentWeather.getCurrentTemp() );
        tvCurrentTemp.setText( String.format("%.2f", dTempC ) + " " + getString(R.string.strGradus) + getString(R.string.strCelcius) );
        tvPressure = findViewById( R.id.tvPressure );
        double dPressure =  toMmMercuryColumn( currentWeather.getPressure() );
        tvPressure.setText( String.format("%.2f", dPressure ) + " " + getString(R.string.strMmHg) );
        tvHumidity = findViewById( R.id.tvHumidity );
        tvHumidity.setText( String.valueOf( currentWeather.getHumidity() ) + getString(R.string.strProcent) );
        tvWindSpeed = findViewById( R.id.tvWindSpeed );
        tvWindSpeed.setText( String.valueOf( currentWeather.getWindSpeed() ) + " " + getString(R.string.strMeterToSec) );
        tvWeatherConditions = findViewById( R.id.tvWeatherDesc );
        tvWeatherConditions.setText( currentWeather.getWeatherDesc() );
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
        //TODO
        return super.onPrepareOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        //TODO

        switch( item.getItemId() )
        {
            case R.id.action_update:
                return true;

            default:
                return super.onOptionsItemSelected( item );
        }
    }

    //! Функция конвертации температуры из Кельвинов в градусы Цельсия
    private double toCelsius( double dTempKelvin )
    {
        double dCelsiusTemp = dTempKelvin - 273.15;

        return dCelsiusTemp;
    }

    //! Функция конвертации температуры из градусов Кельвина в градусы Фаренгейта
    private double toFarengheit( double dTempKelvin )
    {
        double dTempF = 1.8 * ( dTempKelvin - 273.15 ) + 32;

        return dTempF;
    }

    //! Функция конвертации гектопаскалей в мм рт. ст.
    private double toMmMercuryColumn( double dhPa )
    {
        double dMm = dhPa * 0.750064;

        return dMm;
    }
}
