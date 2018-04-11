package com.asaks.newweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.asaks.lib.InfoCity;
import com.asaks.lib.WeatherData;
import com.asaks.lib.WeatherStorage;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    InfoCity city = new InfoCity( 511565, "Penza", "RU", 53.2, 45 );
    WeatherData weather = new WeatherData( 276.1, 1006.31, 83, 276.1, 276.1,
            1032.21, 1006.31, 1523385261, 4.31, 193.505, 0,
            0, 0, "clear", "clear sky" );
    WeatherStorage weatherStorage = new WeatherStorage( city, weather );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvCity = findViewById( R.id.tvCityName );
        TextView tvLat = findViewById( R.id.tvLat );
        TextView tvLon = findViewById( R.id.tvLon );
        TextView tvCurTemp = findViewById( R.id.tvCurrTemp );
        TextView tvPress = findViewById( R.id.tvPressure );
        TextView tvHumidity = findViewById( R.id.tvHumidity );
        TextView tvWindSpeed = findViewById( R.id.tvWindSpeed );
        TextView tvSkyDesc = findViewById( R.id.tvSkyDesc );
        TextView tvDateTime = findViewById( R.id.tvDateTime );

        tvCity.setText( weatherStorage.getCityName() );
        tvLat.setText( String.valueOf( weatherStorage.getLatitude() ) );
        tvLon.setText( String.valueOf( weatherStorage.getLongitude() ) );
        tvCurTemp.setText( String.valueOf( weatherStorage.getCurrentTemp() ) );
        tvPress.setText( String.valueOf( weatherStorage.getPressure() ) );
        tvHumidity.setText( String.valueOf( weatherStorage.getHumidity() ) );
        tvWindSpeed.setText( String.valueOf( weatherStorage.getWindSpeed() ) );
        tvSkyDesc.setText( weatherStorage.getSkyDesc() );

        Date weatherDate = new Date( (long)weatherStorage.getDateTime() * 1000 );
        tvDateTime.setText( weatherDate.toString() );
    }
}
