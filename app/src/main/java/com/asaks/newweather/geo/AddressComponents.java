package com.asaks.newweather.geo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный класс. Содержит информацию о результатах геокодирования
 */
public class AddressComponents
{
    private class Address
    {
        @SerializedName("long_name")
        private String longName;

        @SerializedName("short_name")
        private String shortName;

        @SerializedName("types")
        private List<String> types;

        Address()
        {
            longName = "";
            shortName = "";
            types = new ArrayList<>();
        }

        public String getLongName()
        {
            return longName;
        }

        public void setLongName(String longName)
        {
            this.longName = longName;
        }

        public String getShortName()
        {
            return shortName;
        }

        public void setShortName(String shortName)
        {
            this.shortName = shortName;
        }

        public List<String> getTypes()
        {
            return types;
        }

        public void setTypes(List<String> types)
        {
            this.types = types;
        }
    }

    /**
     * Вспомогательный класс. Координаты места
     */
    private class Location
    {
        // широта места в градусах
        @SerializedName("lat")
        private double dLat;

        // долгота места в градусах
        @SerializedName("lng")
        private double dLon;

        double getLat()
        {
            return dLat;
        }

        double getLon()
        {
            return dLon;
        }

        void setLat( double dLat )
        {
            this.dLat = dLat;
        }

        void setLon( double dLon )
        {
            this.dLon = dLon;
        }
    }

    /**
     * Вспомогательный класс. "Геометрия" места
     */
    private class Geometry
    {
        @SerializedName("location")
        private Location location;

        Geometry()
        {
            location = new Location();
        }

        Location getLocation()
        {
            return location;
        }

        void setLocation( Location location )
        {
            this.location = location;
        }

        void setLocation( double lat, double lon )
        {
            this.location.setLat( lat );
            this.location.setLon( lon );
        }
    }

    @SerializedName("address_components")
    private List<Address> addressComponents;

    // полный адрес мест
    @SerializedName("formatted_address")
    private String sFormattedAddress;

    // "геометрия" места
    @SerializedName("geometry")
    private Geometry geometry;

    AddressComponents()
    {
        addressComponents = new ArrayList<>();
        sFormattedAddress = "";
        geometry = new Geometry();
    }

    public List<Address> getAddressComponents()
    {
        return addressComponents;
    }

    public void setAddressComponents( List<Address> addressComponents )
    {
        this.addressComponents = addressComponents;
    }

    public String getShortName()
    {
        for ( Address addr : addressComponents )
        {
            List<String> types = addr.getTypes();

            if ( types != null && types.contains( "locality" )/* && types.contains( "political" )*/ )
                return addr.getShortName();
        }

        return "";
    }

    /**
     * Функция запроса "геометрии" места
     * @return "геометрия" места
     */
    public Geometry getGeometry()
    {
        return geometry;
    }

    /**
     * Функция запроса координат места
     * @return координаты места
     */
    public Location getLocation()
    {
        return geometry.location;
    }

    /**
     * Функция запроса широты места
     * @return широта места в градусах
     */
    public double getLatitude()
    {
        return geometry.getLocation().getLat();
    }

    /**
     * Функция запроса долготы места
     * @return долгота места в градусах
     */
    public double getLongitude()
    {
        return geometry.getLocation().getLon();
    }

    /**
     * Функция запроса полного адреса места
     * @return полный адрес места
     */
    public String getFormattedAddress()
    {
        return sFormattedAddress;
    }

    /**
     * Функция записи полного адреса места
     * @param sFormattedAddress - полный адрес места
     */
    public void setFormattedAddress( String sFormattedAddress )
    {
        this.sFormattedAddress = sFormattedAddress;
    }

    /**
     * Функция записи координат места
     * @param lat - широта места в градусах
     * @param lon - долгота места в градусах
     */
    public void setLocation( double lat, double lon )
    {
        geometry.setLocation( lat, lon );
    }

    /**
     * Функция записи координат места
     * @param location - объект, содержащий координаты места
     */
    public void setLocation( Location location )
    {
        geometry.setLocation( location );
    }

    /**
     * Функция записи "геометрии" места
     * @param geometry - "геометрия" места
     */
    public void setGeometry( Geometry geometry )
    {
        this.geometry = geometry;
    }
}
