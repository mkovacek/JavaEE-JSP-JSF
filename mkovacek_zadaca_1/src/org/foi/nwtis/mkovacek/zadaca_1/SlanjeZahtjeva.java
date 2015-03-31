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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija; //mkovacek

/**
 *
 * @author NWTiS_4
 */
public class SlanjeZahtjeva extends Thread {

    private Konfiguracija konfig;
    private String server;
    private int port;
    private String korisnik;
    private int brojCiklusa;
    private int pauza;

    public SlanjeZahtjeva(Konfiguracija konfig, String server, int port, String korisnik, int brojCiklusa, int pauza) {
        this.konfig = konfig;
        this.server = server;
        this.port = port;
        this.korisnik = korisnik;
        this.brojCiklusa = brojCiklusa;
        this.pauza = pauza;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

        String korisnik = this.getKorisnik();
        int brojCiklusa = this.getBrojCiklusa();
        //int brojCiklusa = 2;
        int cekaj = this.getPauza();
        int pauzaProblema = this.getPauzaProblema();
        int intervalDretve = this.getIntervalDretve();
        int brojPokusajaProblema = this.getBrojPokusajaProblema();
        int brojNeuspjelihPokusaja=0;
        boolean spavanje = false;
        int trenutniCiklus = 0;
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String datum;

       /* while (true) {
            System.out.println("1.while");
            brojNeuspjelihPokusaja = 0;
            System.out.println(brojNeuspjelihPokusaja);*/
            //while za broj ciklusa..
            while (true) {
                //datum=date.format(new Date());
                long pocetak = System.currentTimeMillis();
                System.out.println("2.while");
                spavanje = false;
                try {
                    System.out.println("try");
                    socket = new Socket(this.getServer(), this.getPort());

                    if (cekaj != 0) {
                        try {
                            System.out.println("cekaj");
                            sleep(cekaj);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    os = socket.getOutputStream();
                    is = socket.getInputStream();

                    String komanda = "USER " + korisnik + "; TIME; ";
                    //String komanda = "USER " + korisnik + ";"+" nesto "+"; TIME; ";
                    //String komanda = "USER " + korisnik;
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
                    System.out.println(sb.toString() + " " + getName()); //ili od obrade zahtjeva ime dretve
                    brojNeuspjelihPokusaja=0;
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
                        System.out.println("spavanje zbog neuspješnog pokušaja");
                        sleep(pauzaProblema * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                trenutniCiklus++;
                System.out.println("Broj ciklusa: "+trenutniCiklus+" dretva: "+getName());
                long kraj = System.currentTimeMillis() - pocetak;
                try {
                    System.out.println("sleep interval dretve");
                    sleep(intervalDretve * 1000 - kraj);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (brojCiklusa != 0) {
                    System.out.println("broj ciklusa " +getName());
                    if (brojCiklusa == trenutniCiklus) {
                        break;
                    }
                } else {
                    break;
                }
            }
           /* break;
        }*/
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public Konfiguracija getKonfig() {
        return konfig;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public int getBrojCiklusa() {
        return brojCiklusa;
    }

    public int getPauza() {
        return pauza;
    }

    public int getIntervalDretve() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("intervalDretve"));
    }

    public int getPauzaProblema() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("pauzaProblema"));
    }

    public int getBrojPokusajaProblema() {
        Konfiguracija konfig = this.getKonfig();
        return Integer.parseInt(konfig.dajPostavku("brojPokusajaProblema"));
    }
}
