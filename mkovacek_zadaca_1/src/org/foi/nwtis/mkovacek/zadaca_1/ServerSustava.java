/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;
import org.foi.nwtis.mkovacek.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mkovacek.konfiguracije.NemaKonfiguracije;

/**
 * Klasa ServerSustava ceka zahtjeve korisnika te ih predaje objektu dretve za
 * izvrsavanje.
 *
 * @author Matija Kovacek
 */
public class ServerSustava {

    private List<ObradaZahtjeva> redDretvi;
    protected String parametri;
    protected Matcher mParametri;
    private static boolean pauza;
    private static boolean stop;
    private SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy_HHmmss");

    /**
     * Konstruktor
     *
     * @param parametri - parametri servera
     */
    public ServerSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("ERROR 90 : Parametri servera ne odgovaraju!");
        }
        redDretvi = new ArrayList<>();
    }

    /**
     * Metoda provjerava ispravnost upisanog argumenta
     *
     * @param p - argument za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-server\\s+-konf +([^\\s]+\\.(?i)(xml|txt))+(\\s+(-load))?$";
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
     * Metoda za pokretanje servera. Ucitava se konfiguracija, po potrebi se
     * ucitava serijalizirana evidencija, pokrece se dretva za serijalizaciju
     * evidencije, kreira se grupa dretvi, otvara se socket i server ceka
     * zahjeve korisnika.
     *
     */
    public void pokreniServer() {
        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije ne postoji!");
            return;
        }
        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            if (this.mParametri.group(3) != null) {
                String datEvid = konfig.dajPostavku("evidDatoteka");
                ucitajSerijaliziranuEvidenciju(datEvid);
            }
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }

        SerijalizatorEvidencije se = new SerijalizatorEvidencije(konfig);
        se.start();

        int brojDretvi = Integer.parseInt(konfig.dajPostavku("brojDretvi"));

        ThreadGroup tg = new ThreadGroup("mkovacek");
        ObradaZahtjeva[] dretve = new ObradaZahtjeva[brojDretvi];
        for (int i = 0; i < brojDretvi; i++) {
            dretve[i] = new ObradaZahtjeva(tg, "mkovacek_" + i);
            dretve[i].setKonfig(konfig);
            dretve[i].setNazivKonfiguracijskeDatoteke(datoteka);
            redDretvi.add(dretve[i]);
        }

        int port = Integer.parseInt(konfig.dajPostavku("port"));
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket socket = ss.accept();
                if (ServerSustava.isStop()) {
                    return;
                }
                ObradaZahtjeva oz = dajSlobodnuDretvu(redDretvi);
                if (oz == null) {
                    String odgovor = "ERROR 80; Nema slobodne dretve!";
                    OutputStream os = socket.getOutputStream();
                    os.write(odgovor.getBytes());
                    os.flush();
                    socket.shutdownOutput();
                } else {
                    oz.setStanje(ObradaZahtjeva.StanjeDretve.Zauzeta);
                    oz.setSocket(socket);
                    oz.setCekaj(false);
                    if (!oz.isAlive()) {
                        oz.start();
                    } else {
                        synchronized (oz) {
                            oz.notify();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda ucitava serijaliziranu evidenciju iz datoeke.
     *
     * @param datEvid - naziv datoteke
     */
    private void ucitajSerijaliziranuEvidenciju(String datEvid) {
        File dat = new File(datEvid);
        if (!dat.exists()) {
            System.out.println("Datoteka serijalizirane evidencije ne postoji!");
            return;
        }
        SerijalizatorEvidencije.deserijalizator(datEvid);
    }

    /**
     * Metoda vraca ukoliko postoji slobodnu dretvu.
     *
     * @param dretve - lista dretvi za obradu zahjteva
     * @return dretva ili null
     */
    private ObradaZahtjeva dajSlobodnuDretvu(List<ObradaZahtjeva> dretve) {
        int indexDretve;
        ObradaZahtjeva[] pomDretva = new ObradaZahtjeva[1];
        for (ObradaZahtjeva dretva : dretve) {
            if (dretva.getStanje().equals(ObradaZahtjeva.StanjeDretve.Slobodna)) {
                System.out.println("Izabrana slobodna dretva: " + dretva.getName());
                indexDretve = dretve.indexOf(dretva);
                pomDretva[0] = dretve.get(indexDretve);
                redDretvi.remove(pomDretva[0]);
                redDretvi.add(pomDretva[0]);
                return dretva;
            }
            System.out.println("Zauzeta dretva: " + dretva.getName());
        }
        return null;
    }

    /**
     * Metoda provjerava da li je server u stanju pauze.
     *
     * @return true ili false
     */
    public static boolean isPauza() {
        return pauza;
    }

    /**
     * Metoda postavlja server u pauzu ili ne.
     *
     * @param pauza - true ili false
     */
    public static void setPauza(boolean pauza) {
        ServerSustava.pauza = pauza;
    }

    /**
     * Metoda provjerava da li je server u stanju stop.
     *
     * @return true ili false
     */
    public static boolean isStop() {
        return stop;
    }

    /**
     * Metoda postavlja server u stanje stop ili ne.
     *
     * @param stop - true ili false
     */
    public static void setStop(boolean stop) {
        ServerSustava.stop = stop;
    }

}
