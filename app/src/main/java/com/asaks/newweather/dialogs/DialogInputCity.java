package com.asaks.newweather.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.asaks.newweather.adapters.GeoCodingAdapter;
import com.asaks.newweather.R;
import com.asaks.newweather.geo.AddressComponents;
import com.asaks.newweather.ui.DelayAutoCompleteTextView;

/**
 * Класс диалогового окна ввода названия города
 */
public class DialogInputCity extends DialogFragment
{
    public interface NoticeDialogListener
    {
        void onDialogPositiveClick( DialogFragment dialog );
        void onDialogNegativeClick( DialogFragment dialog );
    }

    NoticeDialogListener mListener;

    private DelayAutoCompleteTextView edCity;
    private double lat;
    private double lon;

    /**
     * Создает новый экземпляр диалогового окна
     * @return объект диалогового окна
     */
    public static DialogInputCity newInstance()
    {
        return new DialogInputCity();
    }


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );

        try
        {
            mListener = (NoticeDialogListener)context;
        }
        catch ( ClassCastException e )
        {
            throw new ClassCastException( context.toString()
                    + "must implement NoticeDialogListener" );
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater layoutInflater = LayoutInflater.from( getActivity() );
        View promptView = layoutInflater.inflate( R.layout.dialog_set_city, null );
        builder.setView( promptView );
        edCity = promptView.findViewById( R.id.edInputCity );

        if ( edCity != null )
        {
            edCity.setAdapter( new GeoCodingAdapter( getContext() ) );
            edCity.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    AddressComponents address = (AddressComponents)adapterView.getItemAtPosition(i);

                    //TODO выделять название города из ответа по-другому
                    //String sCity = address.getFormattedAddress().trim().split(",")[0];
                    String sCity = address.getShortName();
                    edCity.setText( sCity );
                    lat = address.getLatitude();
                    lon = address.getLongitude();
                }
            });

            builder.setCancelable(false)
                    .setPositiveButton( getString(R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if ( !edCity.getText().toString().isEmpty() )
                                mListener.onDialogPositiveClick( DialogInputCity.this );
                        }
                    })
                    .setNegativeButton( getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.onDialogNegativeClick( DialogInputCity.this );
                        }
                    });
        }

        return builder.create();
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated(savedInstanceState);

        // показываем клавиатуру сразу после появления диалога ввода названия города
        if ( null != getDialog().getWindow() )
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
    }

    /**
     * Возвращает название города, введенное пользователем в диалоге ввода города
     * @return название города
     */
    public String getCity()
    {
        if ( null != edCity )
            return edCity.getText().toString().toUpperCase().trim();

        return "";
    }

    /**
     * Функция запроса широты города
     * @return широта города в градусах
     */
    public double getLatitude()
    {
        return lat;
    }

    /**
     * Функция запроса долготы города
     * @return долгота города в градусах
     */
    public double getLongitude()
    {
        return lon;
    }
}
