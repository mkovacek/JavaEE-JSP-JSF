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

    /**
     * Konstruktor
     *
     *
     */
    public Adresa() {
    }

    /**
     * Konstruktor
     *
     * @param adresa - naziv adrese
     * @param geoloc - geolokacijski podaci adrese
     */
    public Adresa(String adresa, Lokacija geoloc) {
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    /**
     * Konstruktor
     *
     * @param idadresa - id adrese
     * @param adresa - naziv adrese
     * @param geoloc - geolokacijski podaci adrese
     */
    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    /**
     * Metoda vraca geolokaciju adrese.
     *
     * @return (Lokacija) geoloc
     */
    public Lokacija getGeoloc() {
        return geoloc;
    }

    /**
     * Metoda geolokacijske podatke adrese.
     *
     * @param geoloc - geolokacijski podaci
     */
    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    /**
     * Metoda vraca id adrese.
     *
     * @return (long) idadresa
     */
    public long getIdadresa() {
        return idadresa;
    }

    /**
     * Metoda postavlja id adrese.
     *
     * @param idadresa - id adrese
     */
    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

    /**
     * Metoda vraca naziv adrese.
     *
     * @return (String) adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * Metoda postavlja naziv adrese.
     *
     * @param adresa - naziv adrese
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

}
