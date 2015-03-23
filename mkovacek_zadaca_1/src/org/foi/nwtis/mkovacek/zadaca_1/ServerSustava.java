/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
public class ServerSustava {

    protected String parametri;
    protected Matcher mParametri;

    public ServerSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("Parametri servera ne odgovaraju!");
        }
    }

    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-server -konf +([^\\s]+(.xml|.txt))( +-load)?$";
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

    public void pokreniServer() {
        String datoteka=mParametri.group(1);
        File dat=new File(datoteka);
        if(!dat.exists()){
            System.out.println("Datoteka konfiguracije ne postoji!");
            return;
        }
        Konfiguracija konfig=null;
        try {
            konfig=KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            if(this.mParametri.group(2)!=null){
                String datEvid=konfig.dajPostavku("evidDatoteka");
                ucitajSerijaliziranuEvidenciju(datEvid);
            }
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SerijalizatorEvidencije se=new SerijalizatorEvidencije(konfig);
        se.start();
        
        //popravi
        int brojDretvi=Integer.parseInt(konfig.dajPostavku("brojDretvi"));
        //int brojDretvi=5;
        ThreadGroup tg=new ThreadGroup("mkovacek");
        ObradaZahtjeva[] dretve=new ObradaZahtjeva[brojDretvi];
        
        for (int i = 0; i < brojDretvi; i++) {
            dretve[i]=new ObradaZahtjeva(tg, "mkovacek_"+i);
            dretve[i].setKonfig(konfig);
        }
        //popravi
        int port=Integer.parseInt(konfig.dajPostavku("port"));
        //int port=8000;
        
        try {
            ServerSocket ss =new ServerSocket(port);
            while(true){
                Socket socket=ss.accept();//server čeka ulaz(zahtjev), kad dobi input šalje ga 
                ObradaZahtjeva oz=dajSlobodnuDretvu(dretve);
                if(oz==null){
                    //TODO dovrsi sam jer nema slobodne dretve
                    System.out.println("ERROR 80; Nema slobodne dretve!");
                } //else stavi
                oz.setStanje(ObradaZahtjeva.StanjeDretve.Zauzeta);
                oz.setSocket(socket);
                oz.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ucitajSerijaliziranuEvidenciju(String datEvid) {
        //TODO:napravi sam!
        File dat=new File(datEvid);
        if(!dat.exists()){
            System.out.println("Datoteka serijalizirane evidencije ne postoji!");
            return;
        }
        SerijalizatorEvidencije.deserijalizator(datEvid);
        System.out.println("Evidencija: "+SerijalizatorEvidencije.evidencija);
        //inace ucitaj serijaliziranu evidenciju iz datoteke...
    }

    private ObradaZahtjeva dajSlobodnuDretvu(ObradaZahtjeva[] dretve) {
        //TODO dovrsiti: odabire slobodnu dretvu
        //prvojeri kak ide to kružno dodjeljivanje!
        for (int i = 0; i < dretve.length; i++) {
            if(dretve[i].getStanje().equals(ObradaZahtjeva.StanjeDretve.Slobodna)){
                System.out.println("Izabrana slobodna dretva: "+dretve[i].getName());
                return dretve[i];
            }
            else
                System.out.println("Zauzeta dretva: "+dretve[i].getName());
        }
        return null;
        //return dretve[0]; //samo za sad dok ne implementiram
    }

}
