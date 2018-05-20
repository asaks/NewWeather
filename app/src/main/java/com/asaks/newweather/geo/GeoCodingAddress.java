package com.asaks.newweather.geo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий информацию о координатах указанного адреса
 */
public class GeoCodingAddress
{
    // компоненты адреса места
    @SerializedName("results")
    private List<AddressComponents> addressComponents;
    // статус запроса
    @SerializedName("status")
    private String sStatus;

    public GeoCodingAddress()
    {
        addressComponents = new ArrayList<>();
        sStatus = "";
    }

    public GeoCodingAddress( List<AddressComponents> addressComponents, String sStatus )
    {
        this.addressComponents = addressComponents;
        this.sStatus = sStatus;
    }

    /**
     * Функция запроса списка найденных адресов
     * @return список адресов
     */
    public List<AddressComponents> getAddressComponents()
    {
        return addressComponents;
    }

    /**
     * Функция запроса широты места
     * @param position - позиция в списке результатов
     * @return широта места в градусах
     */
    public double getLat( int position )
    {
        return addressComponents.get(position).getLatitude();
    }

    /**
     * Функция запроса долготы места
     * @param position - позиция в списке результатов
     * @return долгота места в градусах
     */
    public double getLon( int position )
    {
        return addressComponents.get(position).getLongitude();
    }

    /**
     * Функция запроса полного адреса места
     * @param position - позиция в списке результатов
     * @return полный адрес места
     */
    public String getFormattedAddress( int position )
    {
        return addressComponents.get(position).getFormattedAddress();
    }

    /**
     * Функция запроса статуса запроса
     * @return статус запроса
     */
    public String getStatus()
    {
        return sStatus;
    }

    public void setLocation( int position, double lat, double lon )
    {
        addressComponents.get(position).setLocation( lat, lon );
    }

    public void setFormattedAddress( int position, String sFormattedAddress )
    {
        addressComponents.get(position).setFormattedAddress( sFormattedAddress );
    }

    public void setStatus( String sStatus )
    {
        this.sStatus = sStatus;
    }
}
