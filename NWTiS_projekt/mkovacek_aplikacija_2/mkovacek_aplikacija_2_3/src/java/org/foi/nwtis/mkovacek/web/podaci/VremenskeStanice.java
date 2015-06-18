/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 *
 * @author Matija
 */
public class VremenskeStanice {
    private String naziv;
    private int id;
    private Lokacija lokacija;

    public VremenskeStanice() {
    }

    public VremenskeStanice(String naziv, int id, Lokacija lokacija) {
        this.naziv = naziv;
        this.id = id;
        this.lokacija = lokacija;
    }
    
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }
    
    
}
