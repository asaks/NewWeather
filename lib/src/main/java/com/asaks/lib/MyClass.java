package com.asaks.lib;


public class MyClass
{
    public static void main( String[] args )
    {
        MyPair<Integer,String> pair = new MyPair(1,"one");

        System.out.println( "pair: <" + pair.getFirst() + ", " + pair.getSecond() + ">" );

    }


}
