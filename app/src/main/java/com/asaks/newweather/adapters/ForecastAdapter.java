package com.asaks.newweather.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asaks.newweather.ApplicationSettings;
import com.asaks.newweather.GlobalMethodsAndConstants;
import com.asaks.newweather.R;
import com.asaks.newweather.db.AppDatabase;
import com.asaks.newweather.db.WeatherDatabase;
import com.asaks.newweather.weather.WeatherForecast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Адаптер для отображения прогноза погоды
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder>
{
    private Context mContext;
    private RequestOptions requestOptionsGlide = new RequestOptions();
    private WeatherDatabase db = AppDatabase.getInstance().getWeatherDatabase();

    private WeatherForecast weatherForecast;
    private Resources res;

    static class ForecastHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvDay)
        protected TextView tvDay;

        @BindView(R.id.tvForecastTemp)
        protected TextView tvForecastTemp;

        @BindView(R.id.tvForecastPressure)
        protected TextView tvForecastPressure;

        @BindView(R.id.tvForecastHumidity)
        protected TextView tvForecastHumidity;

        @BindView(R.id.tvForecastWind)
        protected TextView tvForecastWind;

        @BindView(R.id.ivWeatherCondition)
        protected ImageView ivWeatherCondition;

        ForecastHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ForecastAdapter( WeatherForecast weatherForecast )
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
        //TODO пока каждый раз лезу в БД и смотрю настройки

        res = holder.itemView.getResources();
        ApplicationSettings settings = db.settingsDao().getSettings();

        if ( settings == null )
            settings = new ApplicationSettings();

        Date timeUpd = new Date( weatherForecast.getWeatherItems().get(position).getDateTime() * 1000 );
        holder.tvDay.setText( new SimpleDateFormat( "dd.MM.yyyy HH:mm", Locale.getDefault() ).format( timeUpd ) );

        long temp = Math.round( weatherForecast.getWeatherItems().get(position).getCurrentTemp() );
        holder.tvForecastTemp.setText( convertTemperature( settings.getUnitTempPos(), temp ) );

        holder.tvForecastPressure.setText( convertPressure( settings.getUnitPressPos(),
                weatherForecast.getWeatherItems().get(position).getPressure() ) );

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

    /**
     * Отображает температуру в указанных единицах измерения
     * @param unit - единицы измерения
     * @param temp - температура в кельвинах
     * return строковое описание температуры
     */
    private String convertTemperature( int unit, double temp )
    {
        String sTemp = "";

        if ( res != null )
        {
            switch ( unit )
            {
                case GlobalMethodsAndConstants.TEMP_CELSIUS: // градусы Цельсия
                {
                    sTemp = String.format( Locale.getDefault(), "%d %s%s",
                            GlobalMethodsAndConstants.toCelsius(temp),
                            res.getString(R.string.gradus), res.getString(R.string.Celcius) );
                    break;
                }
                case GlobalMethodsAndConstants.TEMP_FARENHEIT: // градусы Фаренгейта
                {
                    sTemp = String.format( Locale.getDefault(), "%d %s%s",
                            GlobalMethodsAndConstants.toFarenheit(temp),
                            res.getString(R.string.gradus), res.getString(R.string.Farenheit) );
                    break;
                }
                case GlobalMethodsAndConstants.TEMP_KELVIN: // Кельвины
                default:
                {
                    sTemp = String.format( Locale.getDefault(), "%d %s",
                            Math.round(temp),
                            res.getString(R.string.Kelvin) );
                }
            }
        }

        return sTemp;
    }

    /**
     * Отображает давление в указанных единицах измерения
     * @param unit - единицы измерения
     * @param press - давление в гектопаскалях
     * return строковое описание давления
     */
    private String convertPressure( int unit, double press )
    {
        String sPress = "";

        if ( res != null )
        {
            if ( GlobalMethodsAndConstants.PRESS_HPA == unit )
                sPress = String.format( Locale.getDefault(), "%.2f %s", press,
                        res.getString(R.string.gektopascal) );
            else if ( GlobalMethodsAndConstants.PRESS_MM_HG == unit )
                sPress = String.format( Locale.getDefault(), "%.2f %s",
                        GlobalMethodsAndConstants.toMmHg( press ),
                        res.getString(R.string.mmHg) );
        }

        return sPress;
    }
}
