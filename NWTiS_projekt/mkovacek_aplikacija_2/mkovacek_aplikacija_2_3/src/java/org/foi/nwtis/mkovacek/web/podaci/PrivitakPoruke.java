/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 *
 * @author dkermek
 */
public class PrivitakPoruke {

    private int broj;
    private String vrsta;
    private int velicina;
    private String datoteka;

    /**
     * Konstruktor
     *
     * @param broj - broj privitaka
     * @param vrsta - vrsta privitka
     * @param velicina - velicina privitka
     * @param datoteka - datoteka
     */
    public PrivitakPoruke(int broj, String vrsta, int velicina, String datoteka) {
        this.broj = broj;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.datoteka = datoteka;
    }

    /**
     * Metoda vraca broj privitaka.
     *
     * @return (int) broj
     */
    public int getBroj() {
        return broj;
    }

    /**
     * Metoda vraca datoteku.
     *
     * @return (String) datoteka
     */
    public String getDatoteka() {
        return datoteka;
    }

    /**
     * Metoda vraca velicinu.
     *
     * @return (int) velicina
     */
    public int getVelicina() {
        return velicina;
    }

    /**
     * Metoda vraca vrstu.
     *
     * @return (String) vrsta
     */
    public String getVrsta() {
        return vrsta;
    }

}
