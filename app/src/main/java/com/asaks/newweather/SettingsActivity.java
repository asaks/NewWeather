package com.asaks.newweather;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Класс окна настроек приложения
 */

public class SettingsActivity extends AppCompatActivity
        implements DialogScreen.NoticeDialogListener {

    Spinner spinnerTemperature;
    Spinner spinnerPressure;
    Button btnSetCity;
    ApplicationSettings applicationSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //setTitle( getString(R.string.settings) );

        ActionBar actionBar = getSupportActionBar();

        if ( null != actionBar )
            actionBar.setDisplayHomeAsUpEnabled( true );

        spinnerTemperature = findViewById( R.id.spinnerTemp );
        spinnerPressure = findViewById( R.id.spinnerPressure );
        btnSetCity = findViewById( R.id.btnChangeCity );

        View.OnClickListener oclBtnSetCity = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogScreen dlgInputCity = DialogScreen.newInstance( DialogScreen.IDD_SET_CITY, "" );
                dlgInputCity.show( getSupportFragmentManager(), "DlgSetCity" );
            }
        };

        btnSetCity.setOnClickListener( oclBtnSetCity );

        ArrayAdapter<CharSequence> adapterTemp = ArrayAdapter.createFromResource( this,
                R.array.temperature_units, android.R.layout.simple_spinner_item );
        adapterTemp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerTemperature.setAdapter( adapterTemp );
        spinnerTemperature.setPrompt( getString(R.string.temp_units) );

        ArrayAdapter<CharSequence> adapterPress = ArrayAdapter.createFromResource( this,
                R.array.pressure_units, android.R.layout.simple_spinner_item );
        adapterPress.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerPressure.setAdapter( adapterPress );
        spinnerPressure.setPrompt( getString(R.string.pressure_units) );

        Intent intent = getIntent();
        if ( null != intent )
        {
            applicationSettings = intent.getParcelableExtra( getString(R.string.tag_settings) );
            spinnerTemperature.setSelection( applicationSettings.getUnitTemp() );
            spinnerPressure.setSelection( applicationSettings.getUnitPress() );
            btnSetCity.setText( String.format( "%s: %s", getString(R.string.change_city),
                    applicationSettings.getCity() ) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.menu_settings, menu );
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
        switch ( item.getItemId() )
        {
            case android.R.id.home: // назад
            {
                this.finish();

                return true;
            }
            case R.id.action_save: // сохранить
            {
                Intent intent = new Intent();
                applicationSettings.setUnitTemp( spinnerTemperature.getSelectedItemPosition() );
                applicationSettings.setUnitPress( spinnerPressure.getSelectedItemPosition() );
                intent.putExtra( getString(R.string.tag_settings), applicationSettings );
                setResult( RESULT_OK, intent );

                this.finish();

                return true;
            }
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        int idDialog = ( (DialogScreen)dialog ).getDialogID();

        if ( DialogScreen.IDD_SET_CITY == idDialog )
        {
            applicationSettings.setCity( ( (DialogScreen)dialog ).getCity().trim() );
            btnSetCity.setText( String.format( "%s: %s", getString(R.string.change_city),
                    applicationSettings.getCity() ) );
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
