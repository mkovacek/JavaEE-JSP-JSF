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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija; //mkovacek

/**
 *
 * @author NWTiS_4
 */
public class SerijalizatorEvidencije extends Thread implements Serializable{

    Konfiguracija konfig;
    public static List<Evidencija> evidencija = new ArrayList<>();
    SimpleDateFormat date;

    public SerijalizatorEvidencije(Konfiguracija konfig) {
        super(); //za provjeru dal nadklasa radi nešto
        this.konfig = konfig;
        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
        
        long sleepInterval= Long.parseLong(this.konfig.dajPostavku("intervalSerijalizacije"));
        while(true){
            try {
                sleep(sleepInterval);
               // serijalizator("evidencijaDat_"+date.format(new Date()).toString()+".bin");
                serijalizator(konfig.dajPostavku("evidDatoteka"));
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
            oos.writeObject(evidencija);
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
        System.out.println("Serijalizirano");
    }

    public static void deserijalizator(String datoteka) {
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        
        try {
            fis=new FileInputStream(datoteka);
            ois=new ObjectInputStream(fis);
            evidencija=(List<Evidencija>)ois.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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
        System.out.println("Deserijalizirano");
        
    }

}
