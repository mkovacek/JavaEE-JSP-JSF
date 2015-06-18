/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

import java.util.Date;
import java.util.List;
import javax.mail.Flags;

/**
 *
 * @author dkermek
 */
public class Poruka {

    private String id;
    private Date vrijeme;
    private String salje;
    private String predmet;
    private String vrsta;
    private int velicina;
    private int brojPrivitaka;
    private Flags zastavice;
    private List<PrivitakPoruke> privitciPoruke;
    private boolean brisati;
    private boolean procitano;
    private String tekst;

    /**
     * Konstruktor
     *
     * @param id - id poruke
     * @param vrijeme - vrijeme poruke
     * @param salje - tko salje poruke
     * @param predmet - predmet poruke
     * @param vrsta - vrsta poruke
     * @param velicina - velicina poruke
     * @param brojPrivitaka - brojPrivitaka poruke
     * @param zastavice - zastavice poruke
     * @param privitciPoruke - privitci poruke
     * @param brisati - brisati poruku
     * @param procitano - procitano poruka
     * @param tekst - tekst poruke
     */
    public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano, String tekst) {
        this.id = id;
        this.vrijeme = poslano;
        this.salje = salje;
        this.predmet = predmet;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.brojPrivitaka = brojPrivitaka;
        this.zastavice = zastavice;
        this.privitciPoruke = privitciPoruke;
        this.brisati = brisati;
        this.procitano = procitano;
        this.tekst = tekst;
    }

    /**
     * Metoda vraca tekst poruke.
     *
     * @return (String) tekst
     */
    public String getTekst() {
        return tekst;
    }

    /**
     * Metoda postavlja tekst poruke.
     *
     * @param tekst - tekst poruke
     */
    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    /**
     * Metoda vraca id poruke.
     *
     * @return (String) id
     */
    public String getId() {
        return id;
    }

    /**
     * Metoda vraca brisanje poruke.
     *
     * @return (boolean) brisati
     */
    public boolean isBrisati() {
        return brisati;
    }

    /**
     * Metoda postavlja brisanje poruke.
     *
     * @param brisati - true/false
     */
    public void setBrisati(boolean brisati) {
        this.brisati = brisati;
    }

    /**
     * Metoda vraca broj privitaka poruke.
     *
     * @return (int) brojPrivitaka
     */
    public int getBrojPrivitaka() {
        return brojPrivitaka;
    }

    /**
     * Metoda vraca zastavice poruke.
     *
     * @return (Flags) zastavice
     */
    public Flags getZastavice() {
        return zastavice;
    }

    /**
     * Metoda vraca vrijeme .
     *
     * @return (Date) vrijeme
     */
    public Date getVrijeme() {
        return vrijeme;
    }

    /**
     * Metoda vraca predmet poruke.
     *
     * @return (String) predmet
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     * Metoda vraca listu privitaka poruke.
     *
     * @return (List<PrivitakPoruke>) privitciPoruke
     */
    public List<PrivitakPoruke> getPrivitciPoruke() {
        return privitciPoruke;
    }

    /**
     * Metoda vraca da li je procitana poruka.
     *
     * @return (boolean) procitano
     */
    public boolean isProcitano() {
        return procitano;
    }

    /**
     * Metoda postavlja da li je procitana poruka.
     *
     * @param procitano - true/false
     */
    public void setProcitano(boolean procitano) {
        this.procitano = procitano;
    }

    /**
     * Metoda vraca tko salje poruku.
     *
     * @return (String) salje
     */
    public String getSalje() {
        return salje;
    }

    /**
     * Metoda vraca vrstu poruke.
     *
     * @return (String) vrsta
     */
    public String getVrsta() {
        return vrsta;
    }

    /**
     * Metoda vraca velicinu poruke.
     *
     * @return (int) velicina
     */
    public int getVelicina() {
        return velicina;
    }

    /**
     * Metoda postavlja vrijeme poruke.
     *
     * @param vrijeme - datum,vrijeme
     */
    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

}
