/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 * Klasa lokacija
 *
 * @author dkermek
 */
public class Lokacija {

    private String latitude;
    private String longitude;

    /**
     * Konstruktori
     *
     *
     */
    public Lokacija() {
    }

    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Geteri i seteri
     *
     *
     */
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}