package com.asaks.newweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Класс диалогового окна с информацией о приложении
 */

public class DialogAbout extends DialogFragment
{
    public static DialogAbout newInstance( String message )
    {
        DialogAbout fragment = new DialogAbout();
        Bundle args = new Bundle();
        args.putCharSequence( "message", message );
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle)
    {
        String message = String.valueOf( getArguments().getCharSequence( "message" ) );
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setMessage( message )
                .setTitle( getString( R.string.about_app) )
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
