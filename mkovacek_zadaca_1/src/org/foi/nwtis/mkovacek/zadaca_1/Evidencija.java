/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Matija
 */
public class Evidencija implements Serializable{
    //dretve
    private String oznakaDretve;
    private Date prviZahtjev;
    private Date zadnjiZahtjev;
    private int ukupanBrojZahtjeva;
    private int neuspjesniZahtjevi;
    private Date vrijemeRada;
    
    private Date vrijemeZahtjeva;
    private String ip;
    private String zahtjev;
    private String odgovor;

    public Evidencija(String oznakaDretve, Date prviZahtjev, Date zadnjiZahtjev, int ukupanBrojZahtjeva, Date vrijemeRada, Date vrijemeZahtjeva, String ip, String zahtjev, String odgovor) {
        this.oznakaDretve = oznakaDretve;
        this.prviZahtjev = prviZahtjev;
        this.zadnjiZahtjev = zadnjiZahtjev;
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
        this.vrijemeRada = vrijemeRada;
        this.vrijemeZahtjeva = vrijemeZahtjeva;
        this.ip = ip;
        this.zahtjev = zahtjev;
        this.odgovor = odgovor;
    }

    public Evidencija(String oznakaDretve, String zahtjev, String odgovor) {
        this.oznakaDretve = oznakaDretve;
        this.zahtjev = zahtjev;
        this.odgovor = odgovor;
    }
    
    

    public String getOznakaDretve() {
        return oznakaDretve;
    }

    public Date getPrviZahtjev() {
        return prviZahtjev;
    }

    public Date getZadnjiZahtjev() {
        return zadnjiZahtjev;
    }

    public int getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    public int getNeuspjesniZahtjevi() {
        return neuspjesniZahtjevi;
    }

    public Date getVrijemeRada() {
        return vrijemeRada;
    }

    public Date getVrijemeZahtjeva() {
        return vrijemeZahtjeva;
    }

    public String getIp() {
        return ip;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public String getOdgovor() {
        return odgovor;
    }
          
}
