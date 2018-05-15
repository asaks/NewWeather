package com.asaks.newweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Класс диалогового окна
 */

public class DialogScreen extends DialogFragment
{
    // о приложении
    public static final int IDD_ABOUT = 0;
    // ввод города
    public static final int IDD_SET_CITY = 1;

    public interface NoticeDialogListener
    {
        void onDialogPositiveClick( DialogFragment dialog );
        void onDialogNegativeClick( DialogFragment dialog );
    }

    NoticeDialogListener mListener;

    private EditText edCity;

    /**
     * Создает новый экземпляр диалогового окна
     * @param id - id диалогового окна (его тип)
     * @param message - сообщение для отображения в диалоговом окне
     * @return объект диалогового окна
     */
    public static DialogScreen newInstance( int id, String message )
    {
        DialogScreen fragment = new DialogScreen();
        Bundle args = new Bundle();
        args.putInt( "idd", id );
        args.putCharSequence( "message", message );
        fragment.setArguments( args );

        return fragment;
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

        int id = getArguments() != null ? getArguments().getInt("idd") : -1;

        switch ( id )
        {
            case IDD_ABOUT: // о приложении
            {
                String message = String.valueOf( getArguments().getCharSequence( "message" ) );

                builder.setMessage( message )
                        .setTitle( getString( R.string.about_app) )
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                break;
            }
            case IDD_SET_CITY: // установка города
            {
                LayoutInflater layoutInflater = LayoutInflater.from( getActivity() );
                View promptView = layoutInflater.inflate( R.layout.dialog_set_city, null );
                builder.setView( promptView );
                edCity = promptView.findViewById( R.id.edInputCity );

                builder.setCancelable(false)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if ( !edCity.getText().toString().isEmpty() )
                                    mListener.onDialogPositiveClick( DialogScreen.this );
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mListener.onDialogNegativeClick( DialogScreen.this );
                            }
                        });

                break;
            }
        }

        return builder.create();
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated(savedInstanceState);

        int id = getArguments() != null ? getArguments().getInt("idd") : -1;

        // показываем клавиатуру сразу после появления диалога ввода названия города
        if ( IDD_SET_CITY == id )
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
            return edCity.getText().toString().trim();

        return "";
    }

    /**
     * Возвращает тип диалогового окна
     * @return id (тип) диалогового окна
     */
    public int getDialogID()
    {
        return getArguments() != null ? getArguments().getInt("idd") : -1;
    }
}
