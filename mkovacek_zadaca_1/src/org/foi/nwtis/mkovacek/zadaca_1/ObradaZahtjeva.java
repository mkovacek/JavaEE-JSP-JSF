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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;

/**
 *
 * @author NWTiS_4
 */
public class ObradaZahtjeva extends Thread {

    public enum StanjeDretve {

        Slobodna, Zauzeta
    };
    private boolean cekaj;
    private boolean pokrenuta;
    private Konfiguracija konfig;
    private Socket socket;
    private StanjeDretve stanje;

    private String userSintaksa = "^USER +([a-zA-Z0-9_-]+); TIME; $";      //admin promijeni UPLOAD i DOWNLOAD
    private String adminSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+); (PAUSE|START|STOP|CLEAN|UPLOAD|DOWNLOAD); $";

    public static HashMap<String, EvidencijaModel> evidencijaRada = new HashMap<>();

    public ObradaZahtjeva(ThreadGroup group, String name) {
        super(group, name);
        this.stanje = StanjeDretve.Slobodna;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        System.out.println("Run()");
        EvidencijaModel evidencijaModel = new EvidencijaModel(this.getName());
        InputStream is = null;
        OutputStream os = null;
        String zahtjev = null;
        String odgovor = null;
        Date zahtjevi;
        String vrijeme = "";
        String ipAdresa = "";
        long pocetak = 0;
        long kraj = 0;
        long ukupnoVrijeme = 0;
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        while (true) {
            synchronized (this) {
                while (this.isCekaj()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    System.out.println("Try obrada zahtjeva");
                    pocetak = System.currentTimeMillis();
                    zahtjevi = new Date();
                    vrijeme = date.format(zahtjevi);
                    if (evidencijaModel.getPrvizahtjev() == null) {
                        evidencijaModel.setPrvizahtjev(zahtjevi);
                    }
                    evidencijaModel.setZadnjiZahtjev(zahtjevi);
                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        int znak = is.read();
                        if (znak == -1) {
                            break;
                        }
                        sb.append((char) znak);
                        zahtjev = sb.toString();
                    }
                    System.out.println("Obrada zahtjeva: " + sb.toString() + date.format(new Date())); //makni obrada zahtjeva
                    
                    if (provjeraZahtjeva(zahtjev).equals("true;user")) {
                        odgovor = "OK " + date.format(new Date());
                    } else if (provjeraZahtjeva(zahtjev).equals("true;admin")) {
                        //nova provjera gdje tražim da li je upload, start....
                        //Matcher m=adminKomanda(zahtjev);
                        //if(m!=null){m.group() bla bla}
                        odgovor = "OK " + date.format(new Date());
                    } else {
                        odgovor = "ERROR 90; Pogresna sintaksa komande!";
                    }
                    os.write(odgovor.getBytes());
                    os.flush();
                    socket.shutdownOutput();
                    
                    kraj = System.currentTimeMillis();
                    ukupnoVrijeme=kraj-pocetak;
                } catch (IOException ex) {
                    Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
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

                
                ipAdresa = socket.getRemoteSocketAddress().toString();

                EvidencijaModel.ZahtjevKorisnika zahtjevKorisnika = evidencijaModel.new ZahtjevKorisnika(vrijeme, ipAdresa, zahtjev, odgovor);
                evidencijaModel.dodajZahtjev(zahtjevKorisnika);
                evidencijaModel.setUkupnoVrijemeRada(evidencijaModel.getUkupnoVrijemeRada()+ukupnoVrijeme);

                evidencijaRada = Evidencija.getEvidencijaRada();
                evidencijaRada.put(this.getName(), evidencijaModel);
                Evidencija.setEvidencijaRada(evidencijaRada);

                this.setStanje(StanjeDretve.Slobodna);
                this.setCekaj(true);
            }

        }

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
    }

    
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setStanje(StanjeDretve stanje) {
        this.stanje = stanje;
    }

    public StanjeDretve getStanje() {
        return stanje;
    }

    public void setPokrenuta(boolean pokrenuta) {
        this.pokrenuta = pokrenuta;
    }

    public boolean isPokrenuta() {
        return pokrenuta;
    }

    public boolean isCekaj() {
        return cekaj;
    }

    public void setCekaj(boolean cekaj) {
        this.cekaj = cekaj;
    }

    public Matcher adminZahtjev(String komanda) {
        Pattern pattern = Pattern.compile(adminSintaksa);
        Matcher m = pattern.matcher(komanda);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            return null;
        }
    }

    public String provjeraZahtjeva(String komanda) {
        Pattern pattern = Pattern.compile(userSintaksa);
        Matcher m = pattern.matcher(komanda);
        boolean status = m.matches();
        if (status) {
            return "true;user";
        } else {
            Pattern pattern2 = Pattern.compile(adminSintaksa);
            Matcher m2 = pattern2.matcher(komanda);
            boolean status2 = m2.matches();
            if (status2) {
                return "true;admin";
            } else {
                return "false;false";
            }
        }
    }

}
