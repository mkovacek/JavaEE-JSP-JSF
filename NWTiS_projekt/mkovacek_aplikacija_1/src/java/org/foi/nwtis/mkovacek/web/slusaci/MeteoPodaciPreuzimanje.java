/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.slusaci;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mkovacek.socketServer.ServerSustava;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;

/**
 * Klasa MeteoPodaciPreuzimanje preuzima meteo podatke pomoću openweather
 * servisa za adrese pohranjene u bazi podataka. Proširuje klasu Thread
 *
 * @author mkovacek
 */
public class MeteoPodaciPreuzimanje extends Thread {

    /**
     * Overridde-ana thread run() metoda. Metoda preuzima listu pohranjenih
     * adresa iz baze podataka te preuzima meteo podatke za njih i pohranjuje u
     * bazu podataka
     *
     */
    @Override
    public void run() {
        long pocetak = 0;
        long kraj = 0;
        int pauza = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("sleep"));
        while (!ServerSustava.isStop()) {
            pocetak = System.currentTimeMillis();
            if (!ServerSustava.isPauza()) {
                System.out.println("preuzimanje meteo podataka");
                BazaPodataka bp = new BazaPodataka();
                List<Adresa> adrese = new ArrayList<Adresa>();
                adrese = bp.dohvatiAdrese();
                String apiKey = SlusacAplikacije.konfig.dajPostavku("apiKey");
                OWMKlijent owmk = new OWMKlijent(apiKey);
                if (adrese != null) {
                    for (Adresa a : adrese) {
                        //System.out.println("adr: " + a.getAdresa());
                        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
                        bp.spremiMeteoPodatke(a.getAdresa(), mp);
                        //System.out.println("Status: " + a.getStatusPreuzimanja());
                        if (a.getStatusPreuzimanja() == 0) {
                           // System.out.println("update statusa!");
                            bp.statusPreuzimanjaPodataka(a.getAdresa());
                        }
                    }
                }
            }
            kraj = System.currentTimeMillis();
            long trajanjeObrade = kraj - pocetak;
            try {
                long spavanje = pauza * 1000;
                if (trajanjeObrade < spavanje) {
                    sleep(spavanje - trajanjeObrade);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MeteoPodaciPreuzimanje.class.getName()).log(Level.SEVERE, null, ex);
                break;
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
     * Overridde-ana thread interrupt() metoda.
     *
     */
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

}
