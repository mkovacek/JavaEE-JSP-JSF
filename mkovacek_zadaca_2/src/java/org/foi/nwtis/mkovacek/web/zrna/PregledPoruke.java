/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mkovacek.web.kontrole.Poruka;

/**
 * Managed bean PregledPoruke, služi za prikaz detalja poruke
 *
 * @author mkovacek
 */
@ManagedBean
@RequestScoped
public class PregledPoruke {

    public static Poruka poruka;

    /**
     * Konstruktor
     */
    public PregledPoruke() {
    }

    /**
     * Metoda je akcija za prijelaz na stranicu za pregled svih poruka.
     *
     * @return OK
     */
    public String povratakPregledSvihPoruka() {
        return "OK";
    }

    /**
     * Metoda vraca poruku.
     *
     * @return (Poruka) poruka
     */
    public Poruka getPoruka() {
        return poruka;
    }

    /**
     * Metoda postavlja poruku.
     *
     * @param poruka - poruka
     */
    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }
}
