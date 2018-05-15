package com.asaks.newweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asaks.newweather.weather.WeatherForecast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Адаптер для отображения прогноза погоды
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder>
{
    private Context mContext;
    private RequestOptions requestOptionsGlide = new RequestOptions();

    private WeatherForecast weatherForecast;

    static class ForecastHolder extends RecyclerView.ViewHolder
    {
        private TextView tvDay;
        private TextView tvForecastTemp;
        private TextView tvForecastPressure;
        private TextView tvForecastHumidity;
        private TextView tvForecastWind;
        private ImageView ivWeatherCondition;

        ForecastHolder(View itemView)
        {
            super(itemView);
            tvDay = itemView.findViewById( R.id.tvDay );
            tvForecastTemp = itemView.findViewById( R.id.tvForecastTemp );
            tvForecastPressure = itemView.findViewById( R.id.tvForecastPressure );
            tvForecastHumidity = itemView.findViewById( R.id.tvForecastHumidity );
            tvForecastWind = itemView.findViewById( R.id.tvForecastWind );
            ivWeatherCondition = itemView.findViewById( R.id.ivWeatherCondition);
        }
    }

    ForecastAdapter( WeatherForecast weatherForecast )
    {
        this.weatherForecast = weatherForecast;
    }

    @Override
    public void onAttachedToRecyclerView( @NonNull RecyclerView recyclerView )
    {
        super.onAttachedToRecyclerView( recyclerView );
    }

    @NonNull
    @Override
    public ForecastAdapter.ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.card_forecast, parent, false );

        mContext = parent.getContext();
        requestOptionsGlide = requestOptionsGlide.diskCacheStrategy( DiskCacheStrategy.ALL );

        return new ForecastHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ForecastHolder holder, int position)
    {
        //TODO отображение в заисимости от установленных настроек

        //ApplicationSettings applicationSettings = ( (MainActivity)mContext).getApplicationSettings();

        Date timeUpd = new Date( weatherForecast.getWeatherItems().get(position).getTimeUpdate() * 1000 );
        holder.tvDay.setText( new SimpleDateFormat( "dd.MM.yyyy HH:mm", Locale.getDefault() ).format( timeUpd ) );

        long temp = Math.round( weatherForecast.getWeatherItems().get(position).getCurrentTemp() );
        holder.tvForecastTemp.setText( String.format( Locale.getDefault(), "%d %s", temp,
                mContext.getString(R.string.Kelvin) ) );

        holder.tvForecastPressure.setText( String.format( Locale.getDefault(), "%.2f %s",
                GlobalMethodsAndConstants.toMmHg( weatherForecast.getWeatherItems().get(position).getPressure() ),
                mContext.getString(R.string.mmHg) ) );

        holder.tvForecastHumidity.setText( String.format( Locale.getDefault(), "%d %s",
                Math.round( weatherForecast.getWeatherItems().get(position).getHumidity() ),
                mContext.getString(R.string.procent) ) );

        holder.tvForecastWind.setText( String.format( Locale.getDefault(), "%.2f %s",
                weatherForecast.getWeatherItems().get(position).getWindSpeed(),
                mContext.getString(R.string.meter_per_sec) ) );

        Glide.with( mContext ).load( weatherForecast.getWeatherItems().get(position).getWeatherIconUrl() )
                .apply( requestOptionsGlide )
                .into( holder.ivWeatherCondition );
    }

    @Override
    public int getItemCount()
    {
        return weatherForecast.getWeatherItems().size();
    }

}
