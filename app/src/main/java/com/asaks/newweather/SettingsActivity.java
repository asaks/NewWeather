package com.asaks.newweather;

//import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinnerTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //setTitle( getString(R.string.settings) );

        ActionBar actionBar = getSupportActionBar();

        if ( null != actionBar )
            actionBar.setDisplayHomeAsUpEnabled( true );

        spinnerTemperature = findViewById( R.id.spinnerTemp );
        ArrayAdapter<CharSequence> adapterTemp = ArrayAdapter.createFromResource( this,
                R.array.temperature_gradus, android.R.layout.simple_spinner_item );
        adapterTemp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerTemperature.setAdapter( adapterTemp );
        spinnerTemperature.setPrompt( "Единицы измерения температуры" );

        Intent intent = getIntent();
        if ( null != intent )
        {
            int unitTemp = intent.getIntExtra( "tempGradus", Constants.TEMP_KELVIN );
            spinnerTemperature.setSelection( unitTemp );
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
            case android.R.id.home:
            {
                this.finish();

                return true;
            }
            case R.id.action_save:
            {
                Intent intent = new Intent();
                intent.putExtra( "tempGradus", spinnerTemperature.getSelectedItemPosition() );
                setResult( RESULT_OK, intent );

                this.finish();

                return true;
            }
            default:
                return super.onOptionsItemSelected( item );
        }
    }
}
