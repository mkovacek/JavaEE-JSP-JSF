/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.sb.KorisniciFacade;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * ManagedBean za pregled korisnika
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledKorisnika {

    @EJB
    private KorisniciFacade korisniciFacade;
    private Korisnici korisnik;
    private Korisnici odabraniKorisnik;
    private int stariTip;
    private List<Korisnici> korisnici;
    private HttpSession session;
    private FacesContext context;
    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));
    private boolean greska = false;
    private boolean nemaRezultata = false;
    private boolean prikazForme = false;
    private boolean zatvoriFormu = false;
    private boolean azurirano = false;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledKorisnika() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Metoda za preuzimanje svih korisnika iz db
     *
     */
    public void preuzmiSveKorisnike() {
        List<Korisnici> users = korisniciFacade.findAll();
        this.setKorisnici(users);
    }

    /**
     * Metoda za ažuriranje korisnikovih podataka i slanje zahtjeva socket
     * serveru za promjenu tipa korisnika
     *
     */
    public String azurirajPodatke() {
        greska = false;
        if (!korisnik.getLozinka().trim().equals("")) {
            System.out.println("min. podaci postoje");
            korisniciFacade.edit(korisnik);
            System.out.println("ažurirano");
            if (stariTip != korisnik.getVrsta()) {
                stariTip = korisnik.getVrsta();
                System.out.println("slanje zahtjeva");
                context = FacesContext.getCurrentInstance();
                session = (HttpSession) (context.getExternalContext().getSession(true));
                Korisnici admin = (Korisnici) session.getAttribute("korisnik");
                String tip = korisnik.getVrsta() == 0 ? "NOADMIN" : "ADMIN";
                String zahtjev = "USER " + admin.getKorime() + "; PASSWD " + admin.getLozinka() + "; " + tip + " " + korisnik.getKorime() + ";";
                SlanjeZahtjeva sz = new SlanjeZahtjeva(zahtjev);
                String odgovor = sz.posaljiZahtjev();
                if (odgovor.equals("OK 10;")) {
                    azurirano = true;
                    return null;
                } else {
                    System.out.println("socket odg: " + odgovor);
                    greska = true;
                    return null;
                }
            }
            azurirano = true;
        } else {
            System.out.println("nema podataka");
            greska = true;
        }

        return null;
    }

    public String azuriranje() {
        azurirano = false;
        greska = false;
        if (odabraniKorisnik != null) {
            korisnik = odabraniKorisnik;
            stariTip = odabraniKorisnik.getVrsta();
            prikazForme = true;
        }
        return null;
    }

    public String zatvoriFormu() {
        prikazForme = false;
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public KorisniciFacade getKorisniciFacade() {
        return korisniciFacade;
    }

    public void setKorisniciFacade(KorisniciFacade korisniciFacade) {
        this.korisniciFacade = korisniciFacade;
    }

    public Korisnici getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnici korisnik) {
        this.korisnik = korisnik;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public boolean isGreska() {
        return greska;
    }

    public void setGreska(boolean greska) {
        this.greska = greska;
    }

    public List<Korisnici> getKorisnici() {
        if (korisnici == null) {
            this.preuzmiSveKorisnike();
        }
        return korisnici;
    }

    public void setKorisnici(List<Korisnici> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnici getOdabraniKorisnik() {
        return odabraniKorisnik;
    }

    public void setOdabraniKorisnik(Korisnici odabraniKorisnik) {
        this.odabraniKorisnik = odabraniKorisnik;
    }

    public int getStariTip() {
        return stariTip;
    }

    public void setStariTip(int stariTip) {
        this.stariTip = stariTip;
    }

    public boolean isNemaRezultata() {
        return nemaRezultata;
    }

    public void setNemaRezultata(boolean nemaRezultata) {
        this.nemaRezultata = nemaRezultata;
    }

    public boolean isPrikazForme() {
        return prikazForme;
    }

    public void setPrikazForme(boolean prikazForme) {
        this.prikazForme = prikazForme;
    }

    public boolean isZatvoriFormu() {
        return zatvoriFormu;
    }

    public void setZatvoriFormu(boolean zatvoriFormu) {
        this.zatvoriFormu = zatvoriFormu;
    }

    public boolean isAzurirano() {
        return azurirano;
    }

    public void setAzurirano(boolean azurirano) {
        this.azurirano = azurirano;
    }

    public int getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(int stranicenje) {
        this.stranicenje = stranicenje;
    }

}
