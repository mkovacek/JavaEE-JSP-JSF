/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.mkovacek.rest.klijent.AktivniKorisniciKlijent;
import org.foi.nwtis.mkovacek.rest.klijent.KorisnikoveAdreseKlijent;
import org.foi.nwtis.mkovacek.rest.klijent.ResponseHandler;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * ManagedBean za pregled aktivnih korisnika
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledAktivnihKorisnika implements Serializable {

    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));
    private boolean tblAktivniKorisnici = false;
    private boolean tblKorisnikoveAdrese = false;
    private boolean trenutniMeteoPodaci = false;
    private boolean nedovoljnoSredstava = false;
    private boolean nepostojePodaci = false;
    private List<Korisnik> aktivniKorisnici;
    private Korisnik odabraniKorisnik;
    private List<Adresa> korisnikoveAdrese;
    private Adresa odabranaAdresa;
    private String korIme;
    private String lozinka;
    private String adresa;
    private String meteoPodaci;

    /**
     * Creates a new instance of PregledAktivnihKorisnika
     */
    public PregledAktivnihKorisnika() {
        tblAktivniKorisnici = false;
        tblKorisnikoveAdrese = false;
        trenutniMeteoPodaci = false;
        nedovoljnoSredstava = false;
        nepostojePodaci = false;
    }
    /**
     * Prikaz aktivnih korisnik, korištenje REST-a
     *
     */
    public String dajAktivneKorisnike() {
        AktivniKorisniciKlijent akk = new AktivniKorisniciKlijent();
        ResponseHandler rh = new ResponseHandler();
        if (aktivniKorisnici == null) {
            aktivniKorisnici = new ArrayList<>();
        }
        aktivniKorisnici = rh.listaAktivnihKorisnika(akk.getJson());
        tblAktivniKorisnici = true;
        return null;
    }

    /**
     * Prikaz korisnikovih adresa, korištenje REST-a
     *
     */
    public String dajKorisnikoveAdrese() {
        if (odabraniKorisnik != null) {
            KorisnikoveAdreseKlijent ka = new KorisnikoveAdreseKlijent(odabraniKorisnik.getKorIme());
            ResponseHandler rh = new ResponseHandler();
            if (korisnikoveAdrese == null) {
                korisnikoveAdrese = new ArrayList<>();
            }
            korisnikoveAdrese = rh.listaKorisnikovihAdresa(ka.getJson());
            korIme = odabraniKorisnik.getKorIme();
            lozinka = odabraniKorisnik.getLozinka();
            tblKorisnikoveAdrese = true;
        }
        return null;
    }

    /**
     * Slanje zahtjeva socket serveru za meteo podatke odbrane adrese i prikaz
     *
     */
    public String dajMeteoPodatke() {
        trenutniMeteoPodaci = false;
        nedovoljnoSredstava = false;
        nepostojePodaci = false;
        if (odabranaAdresa != null) {
            String zahtjev = "USER " + korIme + "; PASSWD " + lozinka + "; GET " + odabranaAdresa.getAdresa() + ";";
            SlanjeZahtjeva sz = new SlanjeZahtjeva(zahtjev);
            String odgovor = sz.posaljiZahtjev();
            if (odgovor.startsWith("OK 10;")) {
                String[] vrijeme = odgovor.split(";");
                meteoPodaci = vrijeme[1];
                trenutniMeteoPodaci = true;
            } else if (odgovor.equals("ERR 40")) {
                nedovoljnoSredstava = true;
            } else if (odgovor.equals("ERR 43")) {
                nepostojePodaci = true;
            }
        }
        return null;
    }

    /**
     * Zatvaranje pogleda
     *
     */
    public String zatvoriAktivneKorisnike() {
        tblAktivniKorisnici = false;
        return null;
    }

    public String zatvoriKorisnikoveAdrese() {
        tblKorisnikoveAdrese = false;
        return null;
    }

    public String zatvoriMeteoPodatke() {
        trenutniMeteoPodaci = false;
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public boolean isTblAktivniKorisnici() {
        return tblAktivniKorisnici;
    }

    public void setTblAktivniKorisnici(boolean tblAktivniKorisnici) {
        this.tblAktivniKorisnici = tblAktivniKorisnici;
    }

    public boolean isTblKorisnikoveAdrese() {
        return tblKorisnikoveAdrese;
    }

    public void setTblKorisnikoveAdrese(boolean tblKorisnikoveAdrese) {
        this.tblKorisnikoveAdrese = tblKorisnikoveAdrese;
    }

    public boolean isTrenutniMeteoPodaci() {
        return trenutniMeteoPodaci;
    }

    public void setTrenutniMeteoPodaci(boolean trenutniMeteoPodaci) {
        this.trenutniMeteoPodaci = trenutniMeteoPodaci;
    }

    public int getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(int stranicenje) {
        this.stranicenje = stranicenje;
    }

    public List<Korisnik> getAktivniKorisnici() {
        return aktivniKorisnici;
    }

    public void setAktivniKorisnici(List<Korisnik> aktivniKorisnici) {
        this.aktivniKorisnici = aktivniKorisnici;
    }

    public Korisnik getOdabraniKorisnik() {
        return odabraniKorisnik;
    }

    public void setOdabraniKorisnik(Korisnik odabraniKorisnik) {
        this.odabraniKorisnik = odabraniKorisnik;
    }

    public List<Adresa> getKorisnikoveAdrese() {
        return korisnikoveAdrese;
    }

    public void setKorisnikoveAdrese(List<Adresa> korisnikoveAdrese) {
        this.korisnikoveAdrese = korisnikoveAdrese;
    }

    public Adresa getOdabranaAdresa() {
        return odabranaAdresa;
    }

    public void setOdabranaAdresa(Adresa odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
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

    public String getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(String meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    public boolean isNedovoljnoSredstava() {
        return nedovoljnoSredstava;
    }

    public void setNedovoljnoSredstava(boolean nedovoljnoSredstava) {
        this.nedovoljnoSredstava = nedovoljnoSredstava;
    }

    public boolean isNepostojePodaci() {
        return nepostojePodaci;
    }

    public void setNepostojePodaci(boolean nepostojePodaci) {
        this.nepostojePodaci = nepostojePodaci;
    }

}
