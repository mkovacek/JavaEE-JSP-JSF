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
 *
 * @author Matija
 */
public class EvidencijaModel implements Serializable{
    private String oznaka;
    private Date prvizahtjev;
    private Date zadnjiZahtjev;
    private int ukupanBrojZahtjeva=0;
    private int neuspjesanBrojZahtjeva=0;
    private long ukupnoVrijemeRada=0;

    private ArrayList<ZahtjevKorisnika> zahtjevi = new ArrayList<>();

    public EvidencijaModel(String oznaka) {
        this.oznaka = oznaka;
    }

    
    
    public synchronized boolean dodajZahtjev(ZahtjevKorisnika zahtjev) {
        //TODO dovrši unos/azuriranje podatka zahtjeva
        //dobiješ zahtjev i ažuriraj , izračunaj broj zahtjeva, vrijem itd...
        /*
         private Date prvizahtjev;
         private Date zadnjiZahtjev;
         private int ukupanBrojZahtjeva;
         private int neuspjesanBrojZahtjeva;
         private long ukupnoVrijemeRada;
         */
        
        this.setUkupanBrojZahtjeva(getUkupanBrojZahtjeva()+1);
        zahtjevi.add(zahtjev);  
        this.setZahtjevi(zahtjevi);
        return true;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public Date getPrvizahtjev() {
        return prvizahtjev;
    }

    public void setPrvizahtjev(Date prvizahtjev) {
        this.prvizahtjev = prvizahtjev;
    }

    public Date getZadnjiZahtjev() {
        return zadnjiZahtjev;
    }

    public void setZadnjiZahtjev(Date zadnjiZahtjev) {
        this.zadnjiZahtjev = zadnjiZahtjev;
    }

    public int getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    public void setUkupanBrojZahtjeva(int ukupanBrojZahtjeva) {
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
    }

    public int getNeuspjesanBrojZahtjeva() {
        return neuspjesanBrojZahtjeva;
    }

    public void setNeuspjesanBrojZahtjeva(int neuspjesanBrojZahtjeva) {
        this.neuspjesanBrojZahtjeva = neuspjesanBrojZahtjeva;
    }

    public long getUkupnoVrijemeRada() {
        return ukupnoVrijemeRada;
    }

    public void setUkupnoVrijemeRada(long ukupnoVrijemeRada) {
        this.ukupnoVrijemeRada = ukupnoVrijemeRada;
    }

    public ArrayList<ZahtjevKorisnika> getZahtjevi() {
        return zahtjevi;
    }

    public void setZahtjevi(ArrayList<ZahtjevKorisnika> zahtjevi) {
        this.zahtjevi = zahtjevi;
    }

    public class ZahtjevKorisnika implements Serializable {

        private String vrijeme;
        private String ipAdresa;
        private String zahtjev;
        private String odgovor;

        public ZahtjevKorisnika(String vrijeme, String ipAdresa, String zahtjev, String odgovor) {
            this.vrijeme = vrijeme;
            this.ipAdresa = ipAdresa;
            this.zahtjev = zahtjev;
            this.odgovor = odgovor;
        }

        public String getVrijeme() {
            return vrijeme;
        }

        public void setVrijeme(String vrijeme) {
            this.vrijeme = vrijeme;
        }

        public String getIpAdresa() {
            return ipAdresa;
        }

        public void setIpAdresa(String ipAdresa) {
            this.ipAdresa = ipAdresa;
        }

        public String getZahtjev() {
            return zahtjev;
        }

        public void setZahtjev(String zahtjev) {
            this.zahtjev = zahtjev;
        }

        public String getOdgovor() {
            return odgovor;
        }

        public void setOdgovor(String odgovor) {
            this.odgovor = odgovor;
        }

    }
}
