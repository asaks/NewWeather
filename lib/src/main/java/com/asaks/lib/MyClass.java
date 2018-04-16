package com.asaks.lib;


import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClass
{
    public static void main( String[] args )
    {
        long t = 1523385261;
        Date timeUpd = new Date( t * 1000 );
        String s = new SimpleDateFormat("HH:mm").format(timeUpd);

        System.out.println( s );
    }


}
