package com.asaks.newweather.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.asaks.newweather.R;
import com.asaks.newweather.api.ConstantsGeoCodingAPI;
import com.asaks.newweather.api.GeoCodingAPI;
import com.asaks.newweather.geo.AddressComponents;
import com.asaks.newweather.geo.GeoCodingAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

/**
 * Адаптер для поля ввода названия города
 */
public class GeoCodingAdapter extends BaseAdapter implements Filterable
{
    //private static final String TAG_ADAPTER = "GeoCodingAdapter";

    private final Context mContext;
    private GeoCodingAPI.InterfaceGeoAPI geoCodingClient;
    private List<AddressComponents> mAddress;

    public GeoCodingAdapter( Context context )
    {
        mContext = context;
        mAddress = new ArrayList<>();
        geoCodingClient = GeoCodingAPI.getClient().create( GeoCodingAPI.InterfaceGeoAPI.class );
    }

    @Override
    public int getCount()
    {
        return mAddress.size();
    }

    @Override
    public AddressComponents getItem(int i)
    {
        return mAddress.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if ( view == null )
        {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate( R.layout.auto_complete_item, viewGroup, false );
        }

        AddressComponents address = getItem(i);
        ( (TextView)view.findViewById(R.id.tvItemText) ).setText( address.getFormattedAddress() );

        return view;
    }

    @Override
    public Filter getFilter()
    {

        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults filterResults = new FilterResults();

                if ( charSequence != null )
                {
                    List<AddressComponents> addresses = findAddress( charSequence.toString() );

                    filterResults.values = addresses;
                    filterResults.count = addresses.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                if ( filterResults != null && filterResults.count > 0 )
                {
                    mAddress = (List<AddressComponents>)filterResults.values;
                    notifyDataSetChanged();
                }
                else
                    notifyDataSetInvalidated();
            }
        };
    }

    private List<AddressComponents> findAddress(final String addr )
    {
        Call<GeoCodingAddress> callGeoCoding = geoCodingClient.getGeoAddress( addr,
                ConstantsGeoCodingAPI.RUSSIAN_LANGUAGE_ANSWER_API, ConstantsGeoCodingAPI.KEY );

        /*callGeoCoding.enqueue(new Callback<GeoCodingAddress>() {
            @Override
            public void onResponse( Call<GeoCodingAddress> call, Response<GeoCodingAddress> response )
            {
                if ( response.isSuccessful() )
                {
                    GeoCodingAddress address = response.body();

                    if ( address != null
                            && ConstantsGeoCodingAPI.STATUS_OK.equals( address.getStatus() ) )
                    {
                        mAddress = address.getAddressComponents();
                    }
                }
            }

            @Override
            public void onFailure( Call<GeoCodingAddress> call, Throwable t )
            {
                Log.e( TAG_ADAPTER, "fail geocoding" );
                Log.e( TAG_ADAPTER, t.toString() );
            }
        });

        return mAddress;*/

        List<AddressComponents> lst = new ArrayList<>();

        try {
            Response<GeoCodingAddress> response = callGeoCoding.execute();

            if ( response.isSuccessful() )
            {
                GeoCodingAddress address = response.body();

                if ( address != null
                        && ConstantsGeoCodingAPI.STATUS_OK.equals( address.getStatus() ) )
                {
                    lst = address.getAddressComponents();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lst;
    }
}
