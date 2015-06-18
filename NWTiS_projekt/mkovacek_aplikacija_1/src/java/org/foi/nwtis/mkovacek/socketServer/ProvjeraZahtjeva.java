/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.socketServer;

import java.util.regex.Matcher;

/**
 * Pomoćna klasa za provjeru valjanosti zahtjeva prema socket serveru
 * @author Matija
 */
public class ProvjeraZahtjeva {
    private String status;
    private Matcher m;

    public ProvjeraZahtjeva() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Matcher getM() {
        return m;
    }

    public void setM(Matcher m) {
        this.m = m;
    }
       
}
