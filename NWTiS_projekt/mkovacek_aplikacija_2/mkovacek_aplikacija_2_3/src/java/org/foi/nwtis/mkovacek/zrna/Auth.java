/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.sb.KorisniciFacade;
import org.foi.nwtis.mkovacek.zrna.AktivniKorisnici;

/**
 * ManagedBean za autentifikaciju korisnika
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class Auth implements Serializable {

    @EJB
    private KorisniciFacade korisniciFacade;
    private Korisnici korisnik;
    private AktivniKorisnici aktivniKorisnik;
    private HttpSession session;
    private FacesContext context;

    private String korIme;
    private String lozinka;
    private boolean neuspjesnaPrijava;
    private String adminPanel;
    private String userHome;

    /**
     * Creates a new instance of Auth
     */
    public Auth() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Prijava korisnika
     *
     */
    public String prijava() {
        neuspjesnaPrijava = false;
        if (!korIme.trim().equals("") && !lozinka.trim().equals("")) {
            korisnik = new Korisnici();
            korisnik = korisniciFacade.autentifikacija(korIme, lozinka);
            if (korisnik != null) {
                session.setAttribute("korisnik", korisnik);
                if (aktivniKorisnik == null) {
                    aktivniKorisnik = new AktivniKorisnici();
                }
                AktivniKorisnici.korisnik = korisnik;
                session.setAttribute("aktivni", aktivniKorisnik);
                if (korisnik.getVrsta() == 1) {
                    System.out.println("admin");
                    return "OK ADMIN";
                } else {
                    if (korisnik.getIme().trim().equals("")) {
                        System.out.println("user data");
                        return "OK USER DATA";
                    }
                    System.out.println("user");
                    return "OK USER";
                }
            } else {
                neuspjesnaPrijava = true;
                System.out.println("error");
                return "ERROR";
            }
        } else {
            return "ERROR";
        }
    }

    /**
     * Odjava korisnika, brisanje iz aktivnih korisnika
     *
     */
    public String odjava() {
        System.out.println("logout");
        if (session.getAttribute("korisnik") != null) {
            Korisnici user = (Korisnici) session.getAttribute("korisnik");
            session.removeAttribute("korisnik");
            System.out.println("logout: " + user.getKorime());
            if (aktivniKorisnik == null) {
                aktivniKorisnik = new AktivniKorisnici();
            }
            AktivniKorisnici.korisnik = user;
            session.removeAttribute("aktivni");
        }

        return "OK";
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

    public boolean isNeuspjesnaPrijava() {
        return neuspjesnaPrijava;
    }

    public void setNeuspjesnaPrijava(boolean neuspjesnaPrijava) {
        this.neuspjesnaPrijava = neuspjesnaPrijava;
    }

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

    public String getAdminPanel() {
        String url = "/mkovacek_aplikacija_2_3/faces/privatno/admin/adminPanel.xhtml";
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        session.setAttribute("url", url);
        //System.out.println("auth: "+(String)session.getAttribute("url"));
        return adminPanel;
    }

    public void setAdminPanel(String adminPanel) {
        this.adminPanel = adminPanel;
    }

    public String getUserHome() {
        String url = "/mkovacek_aplikacija_2_3/faces/privatno/user/home.xhtml";
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        session.setAttribute("url", url);
        //System.out.println("auth: "+(String)session.getAttribute("url"));
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }
}
