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
public class Adresa {

    private long idadresa;
    private String adresa;
    private Lokacija geoloc;
    private String korisnik;
    private int statusPreuzimanja;
    private int brojPreuzimanja;
    /**
     * Konstruktori
     *
     *
     */
    public Adresa() {
    }

    public Adresa(String adresa, int brojPreuzimanja) {
        this.adresa = adresa;
        this.brojPreuzimanja = brojPreuzimanja;
    }
    
    

    public Adresa(String adresa, Lokacija geoloc, String korisnik) {
        this.adresa = adresa;
        this.geoloc = geoloc;
        this.korisnik = korisnik;
    }

    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    public Adresa(long idadresa, String adresa, Lokacija geoloc, String korisnik) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
        this.korisnik = korisnik;
    }

    public Adresa(long idadresa, String adresa, Lokacija geoloc, String korisnik, int statusPreuzimanja) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
        this.korisnik = korisnik;
        this.statusPreuzimanja = statusPreuzimanja;
    }

    /**
     * Geteri i seteri
     *
     *
     */
    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public long getIdadresa() {
        return idadresa;
    }

    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public int getStatusPreuzimanja() {
        return statusPreuzimanja;
    }

    public void setStatusPreuzimanja(int statusPreuzimanja) {
        this.statusPreuzimanja = statusPreuzimanja;
    }

    public int getBrojPreuzimanja() {
        return brojPreuzimanja;
    }

    public void setBrojPreuzimanja(int brojPreuzimanja) {
        this.brojPreuzimanja = brojPreuzimanja;
    }

}
