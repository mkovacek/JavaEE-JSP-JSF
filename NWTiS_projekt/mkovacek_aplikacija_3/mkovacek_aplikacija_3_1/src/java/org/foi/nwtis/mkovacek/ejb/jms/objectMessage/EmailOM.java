/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.jms.objectMessage;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Matija
 */
public class EmailOM implements Serializable{

    private String vrijemePocetak;
    private String vrijemeKraj;
    private int brojProcitanihPoruka;
    private int brojNWTiSporuka;
    private List<Korisnik> dodaniKorisnici;
    private List<Korisnik> ostaliKorisnici;

    public EmailOM(String vrijemePocetak, String vrijemeKraj, int brojProcitanihPoruka, int brojNWTiSporuka, List<Korisnik> dodaniKorisnici, List<Korisnik> ostaliKorisnici) {
        this.vrijemePocetak = vrijemePocetak;
        this.vrijemeKraj = vrijemeKraj;
        this.brojProcitanihPoruka = brojProcitanihPoruka;
        this.brojNWTiSporuka = brojNWTiSporuka;
        this.dodaniKorisnici = dodaniKorisnici;
        this.ostaliKorisnici = ostaliKorisnici;
    }

    public String getVrijemePocetak() {
        return vrijemePocetak;
    }

    public void setVrijemePocetak(String vrijemePocetak) {
        this.vrijemePocetak = vrijemePocetak;
    }

    public String getVrijemeKraj() {
        return vrijemeKraj;
    }

    public void setVrijemeKraj(String vrijemeKraj) {
        this.vrijemeKraj = vrijemeKraj;
    }

    public int getBrojProcitanihPoruka() {
        return brojProcitanihPoruka;
    }

    public void setBrojProcitanihPoruka(int brojProcitanihPoruka) {
        this.brojProcitanihPoruka = brojProcitanihPoruka;
    }

    public int getBrojNWTiSporuka() {
        return brojNWTiSporuka;
    }

    public void setBrojNWTiSporuka(int brojNWTiSporuka) {
        this.brojNWTiSporuka = brojNWTiSporuka;
    }

    public List<Korisnik> getDodaniKorisnici() {
        return dodaniKorisnici;
    }

    public void setDodaniKorisnici(List<Korisnik> dodaniKorisnici) {
        this.dodaniKorisnici = dodaniKorisnici;
    }

    public List<Korisnik> getOstaliKorisnici() {
        return ostaliKorisnici;
    }

    public void setOstaliKorisnici(List<Korisnik> ostaliKorisnici) {
        this.ostaliKorisnici = ostaliKorisnici;
    }

}
