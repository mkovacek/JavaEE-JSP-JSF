/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Managed bean EmailPovezivanje, s postavkama potrebnim za povezivanje na
 * poslužitelj
 *
 * @author mkovacek
 */
@ManagedBean
@SessionScoped
public class EmailPovezivanje {

    private String server = SlusacAplikacije.konfig.dajPostavku("server");
    private String korisnik = SlusacAplikacije.konfig.dajPostavku("user");
    private String lozinka = SlusacAplikacije.konfig.dajPostavku("password");


    /**
     * Konstruktor
     */
    public EmailPovezivanje() {
    }

    /**
     * Metoda vraca poslužitelja.
     *
     * @return (String) server
     */
    public String getServer() {
        return server;
    }

    /**
     * Metoda postavlja poslužitelja.
     *
     * @param server - poslužitelj
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Metoda vraca korisnika.
     *
     * @return (String) korisnik
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     * Metoda postavlja korisnika.
     *
     * @param korisnik - korisnika
     */
    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     * Metoda vraca lozinku.
     *
     * @return (String) lozinka
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     * Metoda postavlja lozinku.
     *
     * @param lozinka - lozinka
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     * Metoda je akcija za prijelaz na stranicu za slanje poruka
     *
     * @return OK
     */
    public String saljiPoruku() {
        return "OK";
    }

    /**
     * Metoda je akcija za prijelaz na stranicu za citanje poruka
     *
     * @return OK
     */
    public String citajPoruke() {
        return "OK";
    }

}
