/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 * Klasa adresa
 *
 * @author dkermek
 */
public class Lokacija {

    private String latitude;
    private String longitude;

    /**
     * Konstruktor
     *
     *
     */
    public Lokacija() {
    }

    /**
     * Konstruktor
     *
     * @param latitude - latitude
     * @param longitude - longitude
     */
    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Metoda za postavljanje latitude i longitude
     *
     * @param latitude - latitude
     * @param longitude - longitude
     */
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Metoda vraca latitude.
     *
     * @return (String) latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Metoda postavlja latitude.
     *
     * @param latitude - latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Metoda vraca longitude.
     *
     * @return (String) longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Metoda postavlja longitude.
     *
     * @param longitude - longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
