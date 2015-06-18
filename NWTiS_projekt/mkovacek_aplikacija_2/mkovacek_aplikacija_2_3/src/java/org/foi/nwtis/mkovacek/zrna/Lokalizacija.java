/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Managed bean lokalizacija, služi za lokalizaciju cijele aplikacije.
 *
 * @author mkovacek
 */
@ManagedBean(name = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {

    private static final Map<String, Object> jezici;
    private String odabraniJezik;
    private Locale vazecaLokalizacija;

    private HttpSession session;
    private FacesContext context;

    static {
        jezici = new HashMap<>();
        jezici.put("Hrvatski", new Locale("hr"));
        jezici.put("English", Locale.ENGLISH);
    }

    /**
     * Konstruktor Postavlja se da je vazeci jezik lokalizacije Hrvatski
     */
    public Lokalizacija() {
        Locale l = (Locale) jezici.get("Hrvatski");
        vazecaLokalizacija = l;
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Metoda postavlja jezik kao važeću lokaclizaciju. Metoda redirecta
     * korisnika na stranicu s ovisno koje se došlo na mijenjanje jezika
     *
     */
    public Object odaberiJezik() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));

        Iterator i = jezici.keySet().iterator();
        while (i.hasNext()) {
            String kljuc = (String) i.next();
            Locale l = (Locale) jezici.get(kljuc);
            if (odabraniJezik.equals(l.getLanguage())) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale(l);
                vazecaLokalizacija = l;

                if (session == null || session.getAttribute("url") == null) {
                    try {
                        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
                        context.getExternalContext().redirect(servletRequest.getRequestURI());
                        return "OK";
                    } catch (IOException ex) {
                        Logger.getLogger(Lokalizacija.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String backUrl = (String) session.getAttribute("url");
                    System.out.println("Lokalizacija: " + backUrl);
                    if (backUrl.endsWith(".xhtml")) {
                        try {
                            context.getExternalContext().redirect(backUrl);
                            return "OK";
                        } catch (IOException ex) {
                            Logger.getLogger(Lokalizacija.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    return "OK";
                }
            }
        }
        return "ERROR";
    }

    /**
     * Metoda vraca odabrani jezik lokalizacije.
     *
     * @return (String) odabraniJezik
     */
    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    /**
     * Metoda postavlja odabrani jezik.
     *
     * @param odabraniJezik - odabrani jezik
     */
    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    /**
     * Metoda vraca vazecu lokalizaciju.
     *
     * @return (Locale) vazecaLokalizacija
     */
    public Locale getVazecaLokalizacija() {
        return vazecaLokalizacija;
    }

    /**
     * Metoda vazecu lokalizaciju.
     *
     * @param vazecaLokalizacija - vazeca lokalizacija
     */
    public void setVazecaLokalizacija(Locale vazecaLokalizacija) {
        this.vazecaLokalizacija = vazecaLokalizacija;
    }

    /**
     * Metoda vraca dostupne jezike.
     *
     * @return (Map<String, Object>) jezici
     */
    public Map<String, Object> getJezici() {
        return jezici;
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

}
