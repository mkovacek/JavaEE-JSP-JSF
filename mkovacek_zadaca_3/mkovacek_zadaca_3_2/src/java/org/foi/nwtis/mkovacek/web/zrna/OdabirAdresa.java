/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mkovacek.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.mkovacek.ws.serveri.Adresa;
import org.foi.nwtis.mkovacek.ws.serveri.MeteoPodaci;

/**
 * Managed bean klasa za obradu korisničkih zahtjeva
 *
 * @author mkovacek
 */
@ManagedBean
@RequestScoped
public class OdabirAdresa implements Serializable {

    private List<Adresa> popisAdresa;
    private List<String> odabranaAdresa;
    private List<MeteoPodaci> meteoPodaci;
    private List<MeteoPodaci> meteoPodaci2;

    private boolean prikaz = false;

    /**
     * Konstruktor Creates a new instance of OdabirAdresa
     */
    public OdabirAdresa() {

    }

    /**
     * Metoda vraća listu pohranjenih adresa iz baze podataka
     */
    public List<Adresa> getPopisAdresa() {
        popisAdresa = MeteoWSKlijent.dajSveAdrese();
        return popisAdresa;
    }

    /**
     * Metoda preuzima sve meteo podatke jedne adrese iz db kako bi se prikazali
     * korisniku na ekranu
     */
    public String preuzmiSveMeteoPodatke() {
        if (!odabranaAdresa.isEmpty() && odabranaAdresa.size() == 1) {
            meteoPodaci = new ArrayList<MeteoPodaci>();
            meteoPodaci = MeteoWSKlijent.dajSveMeteoPodatkeZaAdresu(odabranaAdresa.get(0));
        }

        return null;
    }

    /**
     * Metoda preuzima zadnje meteo podatke dviju adresa iz db kako bi se
     * prikazali korisniku na ekranu
     */
    public String preuzmiZadnjeMeteoPodatke() {
        if (!odabranaAdresa.isEmpty() && odabranaAdresa.size() == 2) {
            meteoPodaci = new ArrayList<MeteoPodaci>();
            int index = 0;
            for (String adresa : odabranaAdresa) {
                System.out.println("adr: " + adresa);
                MeteoPodaci mp = new MeteoPodaci();
                mp = MeteoWSKlijent.dajZadnjeMeteoPodatkeZaAdresu(adresa);
                meteoPodaci.add(index, mp);
                index++;
            }
        }
        return null;
    }

    /**
     * Metoda preuzima važeće meteo podatke dviju adresa pomoću servisa kako bi
     * se prikazali korisniku na ekranu
     */
    public String preuzmiVazeceMeteoPodatke() {
        System.out.println("preuzmiVazeceMeteoPodatke");
        if (!odabranaAdresa.isEmpty() && odabranaAdresa.size() == 2) {
            meteoPodaci2 = new ArrayList<MeteoPodaci>();
            int index = 0;
            for (String adresa : odabranaAdresa) {
                MeteoPodaci mp = new MeteoPodaci();
                mp = MeteoWSKlijent.dajVazeceMeteoPodatkeZaAdresu(adresa);
                meteoPodaci2.add(index, mp);
                index++;
            }
            prikaz = true;
        }
        return null;
    }

    /**
     * Metoda vraća listu odabranih adresa
     */
    public List<String> getOdabranaAdresa() {
        return odabranaAdresa;
    }

    /**
     * Metoda postavlja listu odabranih adresa
     */
    public void setOdabranaAdresa(List<String> odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    /**
     * Metoda vraća listu meteo podataka za prikaz u tbl
     */
    public List<MeteoPodaci> getMeteoPodaci() {
        return meteoPodaci;
    }

    /**
     * Metoda postavlja listu meteo podataka
     */
    public void setMeteoPodaci(List<MeteoPodaci> meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    /**
     * Metoda vraća true/false za renderiranje dijela html korisniku na ekranu
     */
    public boolean isPrikaz() {
        return prikaz;
    }

    /**
     * Metoda postavlja true/false za renderiranje html korisniku na ekranu
     */
    public void setPrikaz(boolean prikaz) {
        this.prikaz = prikaz;
    }

    /**
     * Metoda postavlja listu adresa
     */
    public void setPopisAdresa(List<Adresa> popisAdresa) {
        this.popisAdresa = popisAdresa;
    }

    /**
     * Metoda vraća listu meteo podataka za prikaz u bloku
     */
    public List<MeteoPodaci> getMeteoPodaci2() {
        return meteoPodaci2;
    }

    /**
     * Metoda postavlja listu meteo podataka
     */
    public void setMeteoPodaci2(List<MeteoPodaci> meteoPodaci2) {
        this.meteoPodaci2 = meteoPodaci2;
    }
}
