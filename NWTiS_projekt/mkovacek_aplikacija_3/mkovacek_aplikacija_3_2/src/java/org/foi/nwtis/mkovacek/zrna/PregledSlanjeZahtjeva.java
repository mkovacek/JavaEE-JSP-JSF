/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;

/**
 * ManagedBean za slanje zahtjeva socket serveru
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledSlanjeZahtjeva implements Serializable {

    private String zahtjev;
    private String odgovor;

    private boolean prikaz10 = false;
    private boolean prikaz10T = false;
    private boolean prikaz11 = false;
    private boolean prikaz20 = false;
    private boolean prikaz21 = false;
    private boolean prikaz30 = false;
    private boolean prikaz31 = false;
    private boolean prikaz32 = false;
    private boolean prikaz33 = false;
    private boolean prikaz34 = false;
    private boolean prikaz35 = false;
    private boolean prikaz36 = false;
    private boolean prikaz37 = false;
    private boolean prikaz40 = false;
    private boolean prikaz41 = false;
    private boolean prikaz42 = false;
    private boolean prikaz43 = false;
    private boolean prikaz50 = false;
    private boolean error = false;
    private SlanjeZahtjeva sz;

    /**
     * Creates a new instance of PregledSlanjeZahtjeva
     */
    public PregledSlanjeZahtjeva() {
    }

    /**
     * Slanje zahtjeva i prikaz odgovora
     */
    public String slanjezahtjeva() {
        prikaz10 = false;
        prikaz11 = false;
        prikaz20 = false;
        prikaz21 = false;
        prikaz30 = false;
        prikaz31 = false;
        prikaz32 = false;
        prikaz33 = false;
        prikaz34 = false;
        prikaz35 = false;
        prikaz36 = false;
        prikaz37 = false;
        prikaz40 = false;
        prikaz41 = false;
        prikaz42 = false;
        prikaz43 = false;
        prikaz50 = false;
        error = false;

        if (!zahtjev.trim().equals("") && zahtjev != null) {
            sz = new SlanjeZahtjeva(zahtjev);
            odgovor = sz.posaljiZahtjev();
            if (odgovor.startsWith("OK 10;")) {
                prikaz10 = true;
            } else if (odgovor.startsWith("OK 11;")) {
                prikaz11 = true;
            } else if (odgovor.startsWith("ERR 20;")) {
                prikaz20 = true;
            } else if (odgovor.startsWith("ERR 21;")) {
                prikaz21 = true;
            } else if (odgovor.startsWith("ERR 30;")) {
                prikaz30 = true;
            } else if (odgovor.startsWith("ERR 31;")) {
                prikaz31 = true;
            } else if (odgovor.startsWith("ERR 32;")) {
                prikaz32 = true;
            } else if (odgovor.startsWith("ERR 33;")) {
                prikaz33 = true;
            } else if (odgovor.startsWith("ERR 34;")) {
                prikaz34 = true;
            } else if (odgovor.startsWith("ERR 35;")) {
                prikaz35 = true;
            } else if (odgovor.startsWith("ERR 36;")) {
                prikaz36 = true;
            } else if (odgovor.startsWith("ERR 37;")) {
                prikaz37 = true;
            } else if (odgovor.startsWith("ERR 40;")) {
                prikaz40 = true;
            } else if (odgovor.startsWith("ERR 41;")) {
                prikaz41 = true;
            } else if (odgovor.startsWith("ERR 42;")) {
                prikaz42 = true;
            } else if (odgovor.startsWith("ERR 43;")) {
                prikaz43 = true;
            } else if (odgovor.startsWith("ERR 50;")) {
                prikaz50 = true;
            }else{
                error=true;
            }
        }
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public SlanjeZahtjeva getSz() {
        return sz;
    }

    public void setSz(SlanjeZahtjeva sz) {
        this.sz = sz;
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

    public boolean isPrikaz10() {
        return prikaz10;
    }

    public void setPrikaz10(boolean prikaz10) {
        this.prikaz10 = prikaz10;
    }

    public boolean isPrikaz10T() {
        return prikaz10T;
    }

    public void setPrikaz10T(boolean prikaz10T) {
        this.prikaz10T = prikaz10T;
    }

    public boolean isPrikaz11() {
        return prikaz11;
    }

    public void setPrikaz11(boolean prikaz11) {
        this.prikaz11 = prikaz11;
    }

    public boolean isPrikaz20() {
        return prikaz20;
    }

    public void setPrikaz20(boolean prikaz20) {
        this.prikaz20 = prikaz20;
    }

    public boolean isPrikaz21() {
        return prikaz21;
    }

    public void setPrikaz21(boolean prikaz21) {
        this.prikaz21 = prikaz21;
    }

    public boolean isPrikaz30() {
        return prikaz30;
    }

    public void setPrikaz30(boolean prikaz30) {
        this.prikaz30 = prikaz30;
    }

    public boolean isPrikaz31() {
        return prikaz31;
    }

    public void setPrikaz31(boolean prikaz31) {
        this.prikaz31 = prikaz31;
    }

    public boolean isPrikaz32() {
        return prikaz32;
    }

    public void setPrikaz32(boolean prikaz32) {
        this.prikaz32 = prikaz32;
    }

    public boolean isPrikaz33() {
        return prikaz33;
    }

    public void setPrikaz33(boolean prikaz33) {
        this.prikaz33 = prikaz33;
    }

    public boolean isPrikaz34() {
        return prikaz34;
    }

    public void setPrikaz34(boolean prikaz34) {
        this.prikaz34 = prikaz34;
    }

    public boolean isPrikaz35() {
        return prikaz35;
    }

    public void setPrikaz35(boolean prikaz35) {
        this.prikaz35 = prikaz35;
    }

    public boolean isPrikaz36() {
        return prikaz36;
    }

    public void setPrikaz36(boolean prikaz36) {
        this.prikaz36 = prikaz36;
    }

    public boolean isPrikaz37() {
        return prikaz37;
    }

    public void setPrikaz37(boolean prikaz37) {
        this.prikaz37 = prikaz37;
    }

    public boolean isPrikaz40() {
        return prikaz40;
    }

    public void setPrikaz40(boolean prikaz40) {
        this.prikaz40 = prikaz40;
    }

    public boolean isPrikaz41() {
        return prikaz41;
    }

    public void setPrikaz41(boolean prikaz41) {
        this.prikaz41 = prikaz41;
    }

    public boolean isPrikaz42() {
        return prikaz42;
    }

    public void setPrikaz42(boolean prikaz42) {
        this.prikaz42 = prikaz42;
    }

    public boolean isPrikaz43() {
        return prikaz43;
    }

    public void setPrikaz43(boolean prikaz43) {
        this.prikaz43 = prikaz43;
    }

    public boolean isPrikaz50() {
        return prikaz50;
    }

    public void setPrikaz50(boolean prikaz50) {
        this.prikaz50 = prikaz50;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
