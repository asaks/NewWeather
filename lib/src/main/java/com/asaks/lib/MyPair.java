package com.asaks.lib;


/**
 * Пример generic
 */

//TODO почему нельзя использовать примитивные типы?

class MyPair<T1, T2>
{
    T1 first;
    T2 second;

    MyPair( T1 first, T2 second )
    {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst()
    {
        return first;
    }

    public T2 getSecond()
    {
        return second;
    }
}
