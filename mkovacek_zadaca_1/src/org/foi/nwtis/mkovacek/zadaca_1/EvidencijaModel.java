/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Klasa EvidencijaModel implementira Serializable. Sluzi za evidentiranje rada
 * pojedine dretve.
 *
 * @author Matija Kovacek
 */
public class EvidencijaModel implements Serializable {

    private String oznaka;
    private Date prvizahtjev;
    private Date zadnjiZahtjev;
    private int ukupanBrojZahtjeva = 0;
    private int neuspjesanBrojZahtjeva = 0;
    private long ukupnoVrijemeRada = 0;
    private ArrayList<ZahtjevKorisnika> zahtjevi = new ArrayList<>();

    /**
     * Konstruktor
     *
     * @param oznaka - naziv dretve
     */
    public EvidencijaModel(String oznaka) {
        this.oznaka = oznaka;
    }

    /**
     * Metoda dodaje korisnikove zahtjeve u listu zahtjeva i azurira ukupan broj
     * zahtjeva koje je primila dretva.
     *
     * @param zahtjev - zahtjev korisnika
     * @return true
     */
    public synchronized boolean dodajZahtjev(ZahtjevKorisnika zahtjev) {
        this.setUkupanBrojZahtjeva(getUkupanBrojZahtjeva() + 1);
        zahtjevi.add(zahtjev);
        this.setZahtjevi(zahtjevi);
        return true;
    }

    /**
     * Metoda vraca naziv dretve.
     *
     * @return (String) naziv dretve
     */
    public String getOznaka() {
        return oznaka;
    }

    /**
     * Metoda postavlja naziv dretve.
     *
     * @param oznaka - naziv dretve
     */
    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    /**
     * Metoda vraca prvi zahtjev dretve.
     *
     * @return (Date) prvi zahtjev
     */
    public Date getPrvizahtjev() {
        return prvizahtjev;
    }

    /**
     * Metoda postavlja prvi zahtjev dretve.
     *
     * @param prvizahtjev - prvi zahtjev dretve
     */
    public void setPrvizahtjev(Date prvizahtjev) {
        this.prvizahtjev = prvizahtjev;
    }

    /**
     * Metoda vraca zadnji zahtjev dretve.
     *
     * @return (Date) zadnji zahtjev
     */
    public Date getZadnjiZahtjev() {
        return zadnjiZahtjev;
    }

    /**
     * Metoda postavlja zadnji zahtjev dretve.
     *
     * @param oznaka - zadnji zahtjev dretve
     */
    public void setZadnjiZahtjev(Date zadnjiZahtjev) {
        this.zadnjiZahtjev = zadnjiZahtjev;
    }

    /**
     * Metoda vraca ukupan broj zahtjeva dretve.
     *
     * @return (int) ukupan broj zahtjeva
     */
    public int getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    /**
     * Metoda postavlja ukupan broj zahtjeva dretve.
     *
     * @param ukupanBrojZahtjeva - ukupan broj zahtjeva dretve
     */
    public void setUkupanBrojZahtjeva(int ukupanBrojZahtjeva) {
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
    }

    /**
     * Metoda vraca ukupan broj neuspjesnih zahtjeva dretve.
     *
     * @return (int) ukupan broj neuspjesnih zahtjeva
     */
    public int getNeuspjesanBrojZahtjeva() {
        return neuspjesanBrojZahtjeva;
    }

    /**
     * Metoda postavlja ukupan broj neuspjesnih zahtjeva dretve.
     *
     * @param ukupanBrojZahtjeva - ukupan broj neuspjesnih zahtjeva dretve
     */
    public void setNeuspjesanBrojZahtjeva(int neuspjesanBrojZahtjeva) {
        this.neuspjesanBrojZahtjeva = neuspjesanBrojZahtjeva;
    }

    /**
     * Metoda vraca ukupano vrijeme rada dretve.
     *
     * @return (long) ukupano vrijeme rada dretve (milisekunde)
     */
    public long getUkupnoVrijemeRada() {
        return ukupnoVrijemeRada;
    }

    /**
     * Metoda postavlja ukupano vrijeme rada dretve.
     *
     * @param ukupnoVrijemeRada - ukupano vrijeme rada dretve
     */
    public void setUkupnoVrijemeRada(long ukupnoVrijemeRada) {
        this.ukupnoVrijemeRada = ukupnoVrijemeRada;
    }

    /**
     * Metoda vraca listu korisnikovih zahtjeva.
     *
     * @return (ArrayList<ZahtjevKorisnika>) korisnikovi zahtjevi
     */
    public ArrayList<ZahtjevKorisnika> getZahtjevi() {
        return zahtjevi;
    }

    /**
     * Metoda postavlja listu korisnikovih zahtjeva.
     *
     * @param zahtjevi - listu korisnikovih zahtjeva
     */
    public void setZahtjevi(ArrayList<ZahtjevKorisnika> zahtjevi) {
        this.zahtjevi = zahtjevi;
    }

    /**
     * Klasa ZahtjevKorisnika implementira Serializable. Sluzi za evidentiranje
     * korisnikovih zahtjeva.
     *
     * @author Matija Kovacek
     */
    public class ZahtjevKorisnika implements Serializable {

        private String vrijeme;
        private String ipAdresa;
        private String zahtjev;
        private String odgovor;

        /**
         * Konstruktor
         *
         * @param vrijeme - vrijeme korisnikovog zahtjeva
         * @param ipAdresa - IP adresa korisnika
         * @param zahtjev - zahtjev korisnika
         * @param odgovor - odgovor na korisnikov zahtjev
         */
        public ZahtjevKorisnika(String vrijeme, String ipAdresa, String zahtjev, String odgovor) {
            this.vrijeme = vrijeme;
            this.ipAdresa = ipAdresa;
            this.zahtjev = zahtjev;
            this.odgovor = odgovor;
        }

        /**
         * Metoda vraca vrijeme korisnikovog zahtjeva.
         *
         * @return (String) vrijeme korisnikovog zahtjeva
         */
        public String getVrijeme() {
            return vrijeme;
        }

        /**
         * Metoda postavlja vrijeme korisnikovog zahtjeva.
         *
         * @param vrijeme - vrijeme korisnikovog zahtjeva
         */
        public void setVrijeme(String vrijeme) {
            this.vrijeme = vrijeme;
        }

        /**
         * Metoda vraca IP adresu korisnika.
         *
         * @return (String) IP adresa korisnika
         */
        public String getIpAdresa() {
            return ipAdresa;
        }

        /**
         * Metoda postavlja IP adresu korisnika.
         *
         * @param ipAdresa - IP adresa korisnika
         */
        public void setIpAdresa(String ipAdresa) {
            this.ipAdresa = ipAdresa;
        }

        /**
         * Metoda vraca korisnikov zahtjev.
         *
         * @return (String) korisnikov zahtjev
         */
        public String getZahtjev() {
            return zahtjev;
        }

        /**
         * Metoda postavlja korisnikov zahtjev.
         *
         * @param zahtjev - korisnikov zahtjev
         */
        public void setZahtjev(String zahtjev) {
            this.zahtjev = zahtjev;
        }

        /**
         * Metoda vraca odgovor na korisnikov zahtjev.
         *
         * @return (String) odgovor na korisnikov zahtjev
         */
        public String getOdgovor() {
            return odgovor;
        }

        /**
         * Metoda postavlja odgovor na korisnikov zahtjev
         *
         * @param odgovor - odgovor na korisnikov zahtjev
         */
        public void setOdgovor(String odgovor) {
            this.odgovor = odgovor;
        }

    }
}
