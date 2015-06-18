/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.jms.objectMessage;

import java.io.Serializable;

/**
 *
 * @author Matija
 */
public class AdreseOM implements Serializable {
    private String korisnik;
    private String lozinka;
    private String adresa;

    public AdreseOM(String korisnik, String lozinka, String adresa) {
        this.korisnik = korisnik;
        this.lozinka = lozinka;
        this.adresa = adresa;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    
    
}
