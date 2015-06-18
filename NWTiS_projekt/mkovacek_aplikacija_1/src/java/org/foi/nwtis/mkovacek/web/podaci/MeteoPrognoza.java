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
    private String sati;

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

    public MeteoPrognoza(String adresa, MeteoPodaci prognoza, String sati) {
        this.adresa = adresa;
        this.prognoza = prognoza;
        this.sati = sati;
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

    public String getSati() {
        return sati;
    }

    public void setSati(String sati) {
        this.sati = sati;
    }
    
    

}
