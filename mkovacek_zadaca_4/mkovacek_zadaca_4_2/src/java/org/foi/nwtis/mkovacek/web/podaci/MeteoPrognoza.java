/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 * Klasa meteoprognoza
 *
 * @author dkermek
 */
public class MeteoPrognoza {

    private String adresa;
    private int dan;
    private MeteoPodaci prognoza;

    /**
     * Konstruktori
     *
     *
     */
    public MeteoPrognoza() {
    }

    public MeteoPrognoza(String adresa, int dan, MeteoPodaci prognoza) {
        this.adresa = adresa;
        this.dan = dan;
        this.prognoza = prognoza;
    }

    /**
     * Geteri i seteri
     *
     *
     */
    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public MeteoPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPodaci prognoza) {
        this.prognoza = prognoza;
    }

}
