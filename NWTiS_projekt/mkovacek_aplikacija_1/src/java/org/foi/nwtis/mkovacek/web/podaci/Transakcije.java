/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

import java.util.Date;

/**
 * Klasa transakcije
 * @author Matija
 */
public class Transakcije {
    private int id;
    private String korisnik;
    private String usluga;
    private double staroStanje;
    private double cijena;
    private double novoStanje;
    private Date vrijemeTransakcije;
    private String status;

    public Transakcije(String korisnik, String usluga, double staroStanje, double cijena, double novoStanje, Date vrijemeTransakcije,String status) {
        this.korisnik = korisnik;
        this.usluga = usluga;
        this.staroStanje = staroStanje;
        this.cijena = cijena;
        this.novoStanje = novoStanje;
        this.vrijemeTransakcije = vrijemeTransakcije;
        this.status=status;
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

    public String getUsluga() {
        return usluga;
    }

    public void setUsluga(String usluga) {
        this.usluga = usluga;
    }

    public double getStaroStanje() {
        return staroStanje;
    }

    public void setStaroStanje(double staroStanje) {
        this.staroStanje = staroStanje;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public double getNovoStanje() {
        return novoStanje;
    }

    public void setNovoStanje(double novoStanje) {
        this.novoStanje = novoStanje;
    }

    public Date getVrijemeTransakcije() {
        return vrijemeTransakcije;
    }

    public void setVrijemeTransakcije(Date vrijemeTransakcije) {
        this.vrijemeTransakcije = vrijemeTransakcije;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
