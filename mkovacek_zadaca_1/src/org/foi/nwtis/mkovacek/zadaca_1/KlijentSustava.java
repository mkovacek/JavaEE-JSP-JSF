/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija; //mkovacek
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna; //mkovacek
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije; //mkovacek

/**
 *
 * @author NWTiS_4
 */
public class KlijentSustava {

    protected Konfiguracija konfig;
    protected String parametri;
    protected Matcher mParametri;

    public KlijentSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("Parametri usera ne odgovaraju!");
        }
    }

    public Matcher provjeraParametara(String p) {
       //  String sintaksa = "^-user -s ([^\\s]+) -port (\\d{4}) -konf +([^\\s]+\\.(?i)(xml|txt)) (-cekaj)?( -multi)?( -ponavljaj)? $"; 
        String sintaksa = "^-user -s ([^\\s]+) -port (\\d{4}) -konf +([^\\s]+\\.(?i)(xml|txt))$";

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

    public void pokreniKlijenta() {

        String datoteka = mParametri.group(3);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije ne postoji!");
            return;
        }
        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            if (this.mParametri.group(2) != null) {
                //TODO: ovo nam treba za neobavezne parametre
            }

            String server = this.mParametri.group(1);
            int port = Integer.parseInt(this.mParametri.group(2));

            //TODO: traži se više dretvi koje se mogu izvršavati, trenutno se samo jedna izvršava
            SlanjeZahtjeva sz = new SlanjeZahtjeva();
            sz.setServer(server);
            sz.setPort(port);
            sz.start();
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
