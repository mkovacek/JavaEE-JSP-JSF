/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

import java.util.Date;

/**
 *
 * @author Matija
 */
public class Dnevnik {
    private int id;
    private String korisnik;
    private String zahtjev;
    private Date vrijeme;
    private String ipAdresa;


    public Dnevnik() {
    }

    public Dnevnik(String korisnik, String zahtjev, Date vrijeme, String ipAdresa) {
        this.korisnik = korisnik;
        this.zahtjev = zahtjev;
        this.vrijeme = vrijeme;
        this.ipAdresa=ipAdresa;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }    

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }
    
}
