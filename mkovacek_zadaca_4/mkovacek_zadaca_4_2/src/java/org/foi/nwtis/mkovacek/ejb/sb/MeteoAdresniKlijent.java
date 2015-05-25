/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.mkovacek.rest.klijenti.GMKlijent;
import org.foi.nwtis.mkovacek.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPrognoza;

/**
 * Stateless SessionBean MeteoAdresniKlijent klasa
 *
 * @author mkovacek
 */
@Stateless
@LocalBean
public class MeteoAdresniKlijent {

    //private String apiKey = "e0106ebd9d3390c88e3cf08a3de01fe4";
    private String apiKey;
    /**
     * Geteri i seteri
     *
     *
     */
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Metoda vraća meteo podatke za unesenu adresu
     *
     * @param adresa - naziv adrese
     * @return MeteoPodaci
     */
    public MeteoPodaci dajVazeceMeteoPodatke(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        OWMKlijent owmk = new OWMKlijent(this.apiKey);
        MeteoPodaci mp = owmk.getRealTimeWeather(l.getLatitude(), l.getLongitude());
        return mp;
    }

    /**
     * Metoda vraća meteo prognozu za nekoliko dana, za određenu adresu
     *
     * @param adresa - naziv adrese
     * @param brojDana -  broja dana prognoze
     * @return 
     */
    public org.foi.nwtis.mkovacek.web.podaci.MeteoPrognoza[] dajMeteoPrognoze(String adresa, int brojDana) {
        Lokacija l = dajLokaciju(adresa);
        OWMKlijent owmk = new OWMKlijent(this.apiKey);
        MeteoPrognoza[] mp = owmk.getWeatherForecast(adresa, l.getLatitude(), l.getLongitude(), brojDana);
        return mp;
    }

    /**
     * Metoda vraća lokaciju za određenu adresu
     *
     *
     * @param adresa - nazvi adrese
     * @return Lokacija
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        return l;
    }

}
