/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;

/**
 * Klasa SerijalizatorEvidencije extends Thread. Sluzi za za serijalizaciju
 * evidencije rada dretvi u određenim intervalima
 *
 * @author Matija Kovacek
 */
public class SerijalizatorEvidencije extends Thread {

    private Konfiguracija konfig;
    private SimpleDateFormat date;

    /**
     * Konstruktor
     *
     * @param konfig - konfiguracija
     */
    public SerijalizatorEvidencije(Konfiguracija konfig) {
        super();
        this.konfig = konfig;
        date = new SimpleDateFormat("ddMMyyyy_HHmmss");
    }

    /**
     * Overridde-ana thread interrupt() metoda.
     *
     */
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Overridde-ana thread run() metoda. Metoda u intervalima serijallizira
     * evidenciju rada dretvi.
     *
     */
    @Override
    public void run() {
        long sleepInterval = Long.parseLong(this.konfig.dajPostavku("intervalSerijalizacije"));
        String nazivDatoteke = this.konfig.dajPostavku("evidDatoteka");
        String[] datoteka = nazivDatoteke.split("\\.");
        while (!ServerSustava.isStop()) {
            try {
                serijalizator(datoteka[0] + date.format(new Date()) + "." + datoteka[1]);
                sleep(sleepInterval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Overridde-ana thread start() metoda.
     *
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metoda sluzi serijalizaciju zapisa u datoteku.
     *
     * @param datoteka - naziv datoteke
     */
    public static void serijalizator(String datoteka) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(datoteka);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Evidencija.getEvidencijaRada());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (oos != null) {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda sluzi deserijalizaciju zapisa iz datoteke.
     *
     * @param datoteka - naziv datoteke
     */
    public static void deserijalizator(String datoteka) {
        HashMap<String, EvidencijaModel> evidencija = new HashMap<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(datoteka);
            ois = new ObjectInputStream(fis);
            evidencija = (HashMap<String, EvidencijaModel>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (fis != null) {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (ois != null) {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!evidencija.isEmpty()) {
            Evidencija.setEvidencijaRada(evidencija);
        } else {
            System.out.println("Dohvaćena evidencija je prazna!");
        }
    }
}
