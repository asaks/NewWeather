package com.asaks.lib;

public class MyClass
{
    public static void main( String[] args )
    {
        InfoCity city = new InfoCity( 511565, "Penza", "RU", 53.2, 45 );
        WeatherData weather = new WeatherData( 276.1, 1006.31, 83, 276.1, 276.1,
                1032.21, 1006.31, 1523385261, 4.31, 193.505, 0,
                0, 0, "clear", "clear sky" );
        WeatherStorage weatherStorage = new WeatherStorage( city, weather );

    }
}
