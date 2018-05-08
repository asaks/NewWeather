package com.asaks.newweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asaks.newweather.weather.WeatherDay;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Адаптер для отображения прогноза погоды
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder>
{
    private List<WeatherDay> listForecast;

    public static class ForecastHolder extends RecyclerView.ViewHolder
    {
        private CardView cardForecast;
        private TextView tvDay;
        private TextView tvTemp;
        private ImageView ivWeatherCondition;

        public ForecastHolder(View itemView)
        {
            super(itemView);
            cardForecast = itemView.findViewById( R.id.cardForecast );
            tvDay = itemView.findViewById( R.id.tvDay );
            tvTemp = itemView.findViewById( R.id.tvTemp );
        }
    }

    ForecastAdapter( List<WeatherDay> listForecast )
    {
        this.listForecast = listForecast;
    }

    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView )
    {
        super.onAttachedToRecyclerView( recyclerView );
    }

    @NonNull
    @Override
    public ForecastAdapter.ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.card_forecast, parent, false );
        ForecastHolder holder = new ForecastHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ForecastHolder holder, int position)
    {
        Date timeUpd = new Date( listForecast.get(position).getTimeUpdate() * 1000 );
        holder.tvDay.setText( new SimpleDateFormat( "dd.MM.yyyy hh:mm", Locale.getDefault() ).format( timeUpd ) );
        holder.tvTemp.setText( String.valueOf( listForecast.get(position).getCurrentTemp() ) );

        //TODO грузить иконку с помощью Glide
    }

    @Override
    public int getItemCount()
    {
        return listForecast.size();
    }

}
