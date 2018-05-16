package com.asaks.newweather.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.asaks.newweather.GlobalMethodsAndConstants;
import com.asaks.newweather.R;

/**
 * Класс диалогового окна с информацией о приложении
 */
public class DialogAbout extends DialogFragment
{
    public static DialogAbout newInstance( String message )
    {
        DialogAbout fragment = new DialogAbout();
        Bundle args = new Bundle();
        args.putCharSequence( GlobalMethodsAndConstants.TAG_MESSAGE, message );

        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        Bundle args = getArguments();
        String message = "";

        if ( args != null )
            message = String.valueOf( args.getCharSequence( GlobalMethodsAndConstants.TAG_MESSAGE ) );

        builder.setMessage( message != null ? message : "" )
                .setTitle( getString(R.string.about_app) )
                .setCancelable(true)
                .setPositiveButton( getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.create();
    }
}
