package com.asaks.newweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asaks.newweather.ApplicationSettings;

@Dao
public interface SettingsDAO
{
    @Query("select * from settings")
    ApplicationSettings getSettings();

    @Insert
    void insert( ApplicationSettings settings );

    @Update
    void update( ApplicationSettings settings );

    @Delete
    void delete( ApplicationSettings settings );

    @Query("select exists (select city from settings where city =  :city)")
    boolean isExist( String city );
}
