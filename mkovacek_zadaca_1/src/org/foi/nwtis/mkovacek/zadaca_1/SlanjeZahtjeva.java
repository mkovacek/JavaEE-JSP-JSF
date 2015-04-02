/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;

/**
 * Klasa SlanjeZahtjeva sluzi za slanje korisnikovih zahtjeva serveru.
 *
 * @author Matija Kovacek
 */
public class SlanjeZahtjeva extends Thread {

    private Konfiguracija konfig;
    private String server;
    private int port;
    private String korisnik;
    private int brojCiklusa;
    private int pauza;

    /**
     * Konstruktor
     *
     * @param konfig - konfiguracija
     * @param server - naziv servera
     * @param port - broj porta
     * @param korisnik - naziv korisnika
     * @param brojCiklusa - broj ciklusa izvršavanja
     * @param pauza - broj sekundi pauziranja
     */
    public SlanjeZahtjeva(Konfiguracija konfig, String server, int port, String korisnik, int brojCiklusa, int pauza) {
        this.konfig = konfig;
        this.server = server;
        this.port = port;
        this.korisnik = korisnik;
        this.brojCiklusa = brojCiklusa;
        this.pauza = pauza;
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
     * Overridde-ana thread run() metoda. Metoda šalje korisnikove zahtjeve.
     *
     */
    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        String korisnik = this.getKorisnik();
        int brojCiklusa = this.getBrojCiklusa();
        int cekaj = this.getPauza();
        int pauzaProblema = this.getPauzaProblema();
        int intervalDretve = this.getIntervalDretve();
        int brojPokusajaProblema = this.getBrojPokusajaProblema();
        int brojNeuspjelihPokusaja = 0;
        boolean spavanje = false;
        int trenutniCiklus = 0;
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        while (!ServerSustava.isStop()) {
                long pocetak = System.currentTimeMillis();
                spavanje = false;

                try {
                    socket = new Socket(this.getServer(), this.getPort());
                    if (cekaj != 0) {
                        try {
                            sleep(cekaj);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    os = socket.getOutputStream();
                    is = socket.getInputStream();
                    String komanda = "USER " + korisnik + "; TIME; ";
                    os.write(komanda.getBytes());
                    os.flush();
                    socket.shutdownOutput();

                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        int znak = is.read();
                        if (znak == -1) {
                            break;
                        }
                        sb.append((char) znak);
                    }
                    System.out.println(sb.toString() + " " + getName());
                    brojNeuspjelihPokusaja = 0;
                } catch (IOException ex) {
                    Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    brojNeuspjelihPokusaja++;
                    spavanje = true;
                    System.out.println("ERROR 80; Nema slobodne dretve!");
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (brojNeuspjelihPokusaja > brojPokusajaProblema) {
                    System.out.println("Broj neuspjelih pokušaja je veći od dozvoljenog broja pokušaja!");
                    break;
                }

                if (spavanje) {
                    try {
                       //System.out.println("spavanje zbog neuspješnog pokušaja");
                        sleep(pauzaProblema * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                trenutniCiklus++;
                long kraj = System.currentTimeMillis() - pocetak;
                try {
                    sleep(intervalDretve * 1000 - kraj);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (brojCiklusa != 0) {
                    if (brojCiklusa == trenutniCiklus) {
                        break;
                    }
                } else {
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
     * Metoda za dohvacanje konfiguracije.
     *
     * @return (Konfiguracija) konfig
     */
    public Konfiguracija getKonfig() {
        return konfig;
    }

    /**
     * Metoda za dohvacanje naziva servera.
     *
     * @return (String) naziv servera
     */
    public String getServer() {
        return server;
    }

    /**
     * Metoda za dohvacanje broja porta.
     *
     * @return (int) broj porta
     */
    public int getPort() {
        return port;
    }

    /**
     * Metoda za dohvacanje naziva korisnika.
     *
     * @return (String) naziv korisnika
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     * Metoda za dohvacanje broja ciklusa izvrsavanja.
     *
     * @return (int) broj ciklusa
     */
    public int getBrojCiklusa() {
        return brojCiklusa;
    }

    /**
     * Metoda za dohvacanje trajanja pauze.
     *
     * @return (int) trajanje pauze
     */
    public int getPauza() {
        return pauza;
    }

    /**
     * Metoda za dohvacanje intervala dretve.
     *
     * @return (int) interval dretve
     */
    public int getIntervalDretve() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("intervalDretve"));
    }

    /**
     * Metoda za dohvacanje trajanja pauze problema.
     *
     * @return (int) trajanje pauze problema
     */
    public int getPauzaProblema() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("pauzaProblema"));
    }

    /**
     * Metoda za dohvacanje broja pokusaja problema.
     *
     * @return (int) broj pokusaja problema
     */
    public int getBrojPokusajaProblema() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("brojPokusajaProblema"));
    }
}
