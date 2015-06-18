/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.AdreseOM;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.EmailOM;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.Korisnik;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator2;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * ManagedBean za pregled JMS
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledJMS implements Serializable {

    private Korisnik korisnik;
    private List<AdreseOM> adrese;
    private AdreseOM odabranaAdresa;
    private List<EmailOM> email;
    private EmailOM odabranEmail;
    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));
    private boolean obrisanEmail = false;
    private boolean obrisanaAdresa = false;
    private String zahtjev;
    private boolean poslanZahtjev = false;
    private boolean nijePoslanZahtjev = false;

    /**
     * Creates a new instance of PregledJMS
     */
    public PregledJMS() {
        obrisanEmail = false;
        obrisanaAdresa = false;
        poslanZahtjev = false;
        nijePoslanZahtjev = false;

    }

    /**
     * Slanje zahtjeva socket serveru za dodavanje novog korisnika
     *
     */
    public String kreirajKorisnika() {
        poslanZahtjev = false;
        nijePoslanZahtjev = false;
        if (!zahtjev.trim().equals("")) {
            SlanjeZahtjeva sz = new SlanjeZahtjeva(zahtjev);
            String odgovor = sz.posaljiZahtjev();
            if (odgovor.equals("OK 10;")) {
                poslanZahtjev = true;
            } else if (odgovor.equals("ERR 50;")) {
                nijePoslanZahtjev = false;
            }
        }
        return null;
    }

    /**
     * Brisanje JMS email
     *
     */
    public String brisanjeEmail() {
        obrisanEmail = false;
        if (odabranEmail != null) {
            email.remove(odabranEmail);
            obrisanEmail = true;
        }
        return null;
    }

    /**
     * Brisanje JMS adresa
     *
     */
    public String brisanjeAdresa() {
        obrisanaAdresa = false;
        if (odabranaAdresa != null) {
            adrese.remove(odabranaAdresa);
            obrisanaAdresa = true;
        }
        return null;
    }

    /**
     * Brisanje svih JMS adresa
     *
     */
    public String brisanjeSvihAdresa() {
        obrisanaAdresa = false;
        if (!adrese.isEmpty()) {
            adrese.clear();
            obrisanaAdresa = true;
        }
        return null;
    }

    /**
     * Brisanje svih JMS email
     *
     */
    public String brisanjeSvihEmail() {
        obrisanEmail = false;
        if (!email.isEmpty()) {
            email.clear();
            obrisanEmail = true;
        }
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public List<AdreseOM> getAdrese() {
        adrese = Serijalizator2.getInstance().getListaAdresa();
        return adrese;
    }

    public void setAdrese(List<AdreseOM> adrese) {
        this.adrese = adrese;
    }

    public AdreseOM getOdabranaAdresa() {
        return odabranaAdresa;
    }

    public void setOdabranaAdresa(AdreseOM odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    public List<EmailOM> getEmail() {
        email = Serijalizator2.getInstance().getListaEmail();
        return email;
    }

    public void setEmail(List<EmailOM> email) {
        this.email = email;
    }

    public EmailOM getOdabranEmail() {
        return odabranEmail;
    }

    public void setOdabranEmail(EmailOM odabranEmail) {
        this.odabranEmail = odabranEmail;
    }

    public int getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(int stranicenje) {
        this.stranicenje = stranicenje;
    }

    public boolean isObrisanEmail() {
        return obrisanEmail;
    }

    public void setObrisanEmail(boolean obrisanEmail) {
        this.obrisanEmail = obrisanEmail;
    }

    public boolean isObrisanaAdresa() {
        return obrisanaAdresa;
    }

    public void setObrisanaAdresa(boolean obrisanaAdresa) {
        this.obrisanaAdresa = obrisanaAdresa;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    public boolean isPoslanZahtjev() {
        return poslanZahtjev;
    }

    public void setPoslanZahtjev(boolean poslanZahtjev) {
        this.poslanZahtjev = poslanZahtjev;
    }

    public boolean isNijePoslanZahtjev() {
        return nijePoslanZahtjev;
    }

    public void setNijePoslanZahtjev(boolean nijePoslanZahtjev) {
        this.nijePoslanZahtjev = nijePoslanZahtjev;
    }

}
