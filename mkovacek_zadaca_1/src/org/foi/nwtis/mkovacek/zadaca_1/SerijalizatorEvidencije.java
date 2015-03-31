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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija; //mkovacek

/**
 *
 * @author NWTiS_4
 */
public class SerijalizatorEvidencije extends Thread implements Serializable {

    Konfiguracija konfig;
    SimpleDateFormat date;
   // public static Evidencija evidencijaRada = new Evidencija();

    public SerijalizatorEvidencije(Konfiguracija konfig) {
        super(); //za provjeru dal nadklasa radi nešto
        this.konfig = konfig;
        date = new SimpleDateFormat("ddMMyyyy_HHmmss");
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        //TODO zavrsiti za serijalizaciju evidendije, tu sva logika kod pokretanja ide
        //pokreći serijalizaciju u određenim intervalima vidi 
        // this.konfig.dajPostavku("intervalSerijlaizacije");

        long sleepInterval = Long.parseLong(this.konfig.dajPostavku("intervalSerijalizacije"));
        while (true) {
            try {       
                serijalizator(this.konfig.dajPostavku("evidDatoteka")+date.format(new Date())+".bin");
                //serijalizator(naziv);
                sleep(sleepInterval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public static void serijalizator(String datoteka) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(datoteka);
            oos = new ObjectOutputStream(fos);       
            oos.writeObject(Evidencija.getEvidencijaRada());
            System.out.println(Evidencija.getEvidencijaRada().entrySet());
            System.out.println("Serijalizirano");
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
        if(!evidencija.isEmpty()){
            Evidencija.setEvidencijaRada(evidencija);
            System.out.println("Deserijalizirano");
        }else{
            System.out.println("Dohvaćena evidencija je prazna!");
        }
    }    
}
