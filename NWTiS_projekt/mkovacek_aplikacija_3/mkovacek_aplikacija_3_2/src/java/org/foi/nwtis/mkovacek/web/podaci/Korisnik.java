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
public class Korisnik {

    private long id;
    private String ime;
    private String prezime;
    private String korIme;
    private String email;
    private String lozinka;
    private double racun;
    private int tip;

    public Korisnik() {
    }

    public Korisnik(String korIme, String lozinka) {
        this.korIme = korIme;
        this.lozinka = lozinka;
    }
   

    public Korisnik(double racun, int tip) {
        this.racun = racun;
        this.tip = tip;
    }
    
    

    public Korisnik(String korIme, String lozinka, int tip) {
        this.korIme = korIme;
        this.lozinka = lozinka;
        this.tip = tip;
    }

    public Korisnik(long id, String ime, String prezime, String korIme, String email, String lozinka, double racun, int tip) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.korIme = korIme;
        this.email = email;
        this.lozinka = lozinka;
        this.racun = racun;
        this.tip = tip;
    }
    
   
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public double getRacun() {
        return racun;
    }

    public void setRacun(double racun) {
        this.racun = racun;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }
    
    
    
}
