/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Managed bean lokalizacija, služi za lokalizaciju cijele aplikacije.
 *
 * @author mkovacek
 */
@ManagedBean(name = "lokalizacija")
@SessionScoped
public class Lokalizacija {

    private static final Map<String, Object> jezici;
    private String odabraniJezik;
    private Locale vazecaLokalizacija;

    static {
        jezici = new HashMap<>();
        jezici.put("Hrvatski", new Locale("hr"));
        jezici.put("English", Locale.ENGLISH);
        jezici.put("Deutsch", Locale.GERMAN);
    }

    /**
     * Konstruktor Postavlja se da je vazeci jezik lokalizacije Hrvatski
     */
    public Lokalizacija() {
        Locale l = (Locale) jezici.get("Hrvatski");
        vazecaLokalizacija = l;
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

    /**
     * Metoda odabrani jezik postavlja kao važeću lokaclizaciju. Ovisno o
     * returnu slijedi prijelaz na novu stranicu ili ostaje se na trenutnoj
     *
     * @return "OK"/"ERROR"
     */
    public Object odaberiJezik() {
        Iterator i = jezici.keySet().iterator();
        while (i.hasNext()) {
            String kljuc = (String) i.next();
            Locale l = (Locale) jezici.get(kljuc);
            if (odabraniJezik.equals(l.getLanguage())) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale(l);
                vazecaLokalizacija = l;
                return "OK";
            }
        }
        return "ERROR";
    }
}
