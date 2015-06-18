/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;

/**
 * ManagedBean za registraciju
 *
 * @author Matija
 */
@ManagedBean
@RequestScoped
public class Registracija implements Serializable {

    private HttpSession session;
    private FacesContext context;
    private String korIme;
    private String lozinka;
    private String ponLozinka;
    private boolean uspjesnaRegistracija = false;
    private boolean diffPasswords = false;
    private boolean error = false;

    /**
     * Creates a new instance of Registracija
     */
    public Registracija() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Metoda šalje zahtjev za dodavanje korisnika socket serveru
     *
     */
    public String registracija() {
        diffPasswords = false;
        error = false;
        uspjesnaRegistracija = false;
        if (!lozinka.equals(ponLozinka)) {
            diffPasswords = true;
        } else {
            String zahtjev = "ADD " + korIme + "; PASSWD " + lozinka + ";";
            SlanjeZahtjeva sz = new SlanjeZahtjeva(zahtjev);
            String odgovor = sz.posaljiZahtjev();
            if (odgovor.equals("OK 10;")) {
                uspjesnaRegistracija = true;
            } else if (odgovor.equals("ERR 50;")) {
                error = true;
            }
        }
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
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

    public String getPonLozinka() {
        return ponLozinka;
    }

    public void setPonLozinka(String ponLozinka) {
        this.ponLozinka = ponLozinka;
    }

    public boolean isUspjesnaRegistracija() {
        return uspjesnaRegistracija;
    }

    public void setUspjesnaRegistracija(boolean uspjesnaRegistracija) {
        this.uspjesnaRegistracija = uspjesnaRegistracija;
    }

    public boolean isDiffPasswords() {
        return diffPasswords;
    }

    public void setDiffPasswords(boolean diffPasswords) {
        this.diffPasswords = diffPasswords;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
