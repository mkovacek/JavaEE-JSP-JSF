/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;
import org.foi.nwtis.mkovacek.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mkovacek.konfiguracije.NemaKonfiguracije;

/**
 * Klasa KlijentSustava provjerava upisane opcije i kreira dretv(e)u klase
 * SlanjeZahtjeva
 *
 * @author Matija Kovacek
 */
public class KlijentSustava {

    protected Konfiguracija konfig;
    protected String parametri;
    protected Matcher mParametri;

    /**
     * Konstruktor
     *
     * @param parametri - parametri klijenta
     */
    public KlijentSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("ERROR 90: Parametri usera ne odgovaraju!");
        }
    }

    /**
     * Metoda provjerava ispravnost upisanog argumenta
     *
     * @param p - argument za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-user\\s+-s +([^\\s]+)\\s+-port +(\\d{4})\\s+-u +([a-zA-Z0-9_-]+)\\s+-konf +([^\\s]+\\.(?i)(xml|txt))+(\\s+(-cekaj\\s+([1-9][0-9]?|^100)))?+(\\s+(-multi))?+(\\s+(-ponavljaj\\s+([2-9][0-9]?|[0-9]{2}|^100)))?$";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.out.println("Ne odgovara!");
            return null;
        }
    }

    /**
     * Metoda za pokretanje klijenta. Ucitava se konfiguracija i kreira
     * dretv(e)u klase SlanjeZahtjeva
     *
     */
    public void pokreniKlijenta() {

        String datoteka = mParametri.group(4);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije ne postoji!");
            return;
        }
        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            String server = this.mParametri.group(1);
            int port = Integer.parseInt(this.mParametri.group(2));
            String korisnik = this.mParametri.group(3);
            int pauza = 0;
            int brojCiklusa = 0;

            if (this.mParametri.group(8) != null) {
                pauza = Integer.parseInt(this.mParametri.group(8));
            }
            if (this.mParametri.group(13) != null) {
                brojCiklusa = Integer.parseInt(this.mParametri.group(13));
            }

            if (this.mParametri.group(10) != null) {
                Random random = new Random();
                int maxDretvi = Integer.parseInt(konfig.dajPostavku("brojDretvi"));
                int brojDretvi = 1 + random.nextInt(maxDretvi);
                int maxRazmakDretvi = Integer.parseInt(konfig.dajPostavku("razmakDretvi"));
                int razmakDretvi = 1 + random.nextInt(maxRazmakDretvi - 1);

                SlanjeZahtjeva[] sz = new SlanjeZahtjeva[brojDretvi];
                for (int i = 0; i < brojDretvi; i++) {
                    sz[i] = new SlanjeZahtjeva(konfig, server, port, korisnik, brojCiklusa, pauza);
                    sz[i].start();
                    try {
                        SlanjeZahtjeva.sleep(razmakDretvi * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                SlanjeZahtjeva sz = new SlanjeZahtjeva(konfig, server, port, korisnik, brojCiklusa, pauza);
                sz.start();
            }
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
