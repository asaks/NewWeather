package com.asaks.newweather.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

public class DelayAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView
{
    private static final int AUTOCOMPLETE_DELAY = 750;

    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            DelayAutoCompleteTextView.super.performFiltering( (CharSequence)msg.obj, msg.arg1 );
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void performFiltering( CharSequence text, int keyCode )
    {
        mHandler.removeMessages( 100 );
        mHandler.sendMessageDelayed( mHandler.obtainMessage(100, text), AUTOCOMPLETE_DELAY );
    }
}
