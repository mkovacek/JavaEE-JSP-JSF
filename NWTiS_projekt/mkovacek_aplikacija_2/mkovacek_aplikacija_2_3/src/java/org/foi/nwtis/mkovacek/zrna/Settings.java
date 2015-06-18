/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.sb.KorisniciFacade;

/**
 * ManagedBean za korisničke podatke
 *
 * @author Matija
 */
@ManagedBean
@RequestScoped
public class Settings {

    @EJB
    private KorisniciFacade korisniciFacade;
    private Korisnici korisnik;
    private HttpSession session;
    private FacesContext context;
    private boolean greska;

    /**
     * Creates a new instance of Settings
     */
    public Settings() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Dohvaćanje korisnikovih podataka
     */
    public void getKorisnikoviPodaci() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici user = (Korisnici) session.getAttribute("korisnik");
        Korisnici userData = korisniciFacade.find(user.getId());
        this.setKorisnik(userData);
    }

    /**
     * Ažuriranje korisnikovih podataka
     */
    public String azurirajPodatke() {
        greska = false;
        if (!korisnik.getIme().trim().equals("") && !korisnik.getLozinka().trim().equals("") && !korisnik.getPrezime().trim().equals("") && !korisnik.getEmailAdresa().trim().equals("")) {
            System.out.println("podaci postoje");
            korisniciFacade.edit(korisnik);
            System.out.println("ažurirano");
            return null;
        } else {
            System.out.println("nema podataka");
            greska = true;
        }

        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public Korisnici getKorisnik() {
        if (korisnik == null) {
            this.getKorisnikoviPodaci();
        }
        return korisnik;
    }

    public KorisniciFacade getKorisniciFacade() {
        return korisniciFacade;
    }

    public void setKorisniciFacade(KorisniciFacade korisniciFacade) {
        this.korisniciFacade = korisniciFacade;
    }

    public void setKorisnik(Korisnici korisnik) {
        this.korisnik = korisnik;
    }

    public boolean isGreska() {
        return greska;
    }

    public void setGreska(boolean greska) {
        this.greska = greska;
    }

}
