/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;
import org.foi.nwtis.mkovacek.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mkovacek.konfiguracije.NemaKonfiguracije;

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
    private Matcher mParametri;

    private String userSintaksa = "^USER +([a-zA-Z0-9_-]+); TIME; $";      //admin promijeni UPLOAD i DOWNLOAD
    private String adminSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+); (PAUSE|START|STOP|CLEAN|STAT|UPLOAD|DOWNLOAD);$";

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
                // while (this.isCekaj() && !ServerSustava.isStop()) {
                while (this.isCekaj()) {
                    try {
                        //System.out.println("wait");
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //if (!ServerSustava.isStop()) {
                // System.out.println("nije stopiran");
                try {
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
                        //ako nije pauziran i stopiran server...
                        if (ServerSustava.isPauza()) {
                            odgovor = "Server: pauza/stop!";
                        } else {
                            odgovor = "OK " + date.format(new Date());
                        }
                    } else if (provjeraZahtjeva(zahtjev).equals("true;admin")) {
                        this.mParametri = adminZahtjev(zahtjev);
                        String korisnik = this.mParametri.group(1);
                        String lozinka = this.mParametri.group(2);
                        String komanda = this.mParametri.group(3);
                        if (komanda.equals("PAUSE")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                if (!ServerSustava.isPauza()) {
                                    ServerSustava.setPauza(true);
                                    odgovor = "OK";
                                } else {
                                    odgovor = "ERROR 01: Server je u stanju pauze!";
                                }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("START")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                if (ServerSustava.isPauza()) {
                                    ServerSustava.setPauza(false);
                                    odgovor = "OK";
                                } else {
                                    odgovor = "ERROR 02: Server nije u stanju pauze!";
                                }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("STOP")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                if (!ServerSustava.isStop()) {
                                    ServerSustava.setStop(true);
                                    odgovor = "OK";
                                    //System.out.println("interrupt obrada");
                                    this.interrupt();
                                    //System.out.println("poslije");
                                } else {
                                    odgovor = "ERROR 03: Server stopiran!";
                                }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("CLEAN")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                try {
                                    HashMap<String, EvidencijaModel> evidencija = new HashMap<>();
                                    Evidencija.setEvidencijaRada(evidencija);
                                    odgovor = "OK";
                                } catch (Exception e) {
                                    odgovor = "ERROR 04; Greška kod brisanja evidencije iz memorije!";
                                }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("STAT")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                    odgovor = "OK";
                                    if (!Evidencija.getEvidencijaRada().isEmpty()) {
                                        for (Map.Entry<String, EvidencijaModel> entrySet : Evidencija.getEvidencijaRada().entrySet()) {
                                            System.out.println("Oznaka dretve: " + entrySet.getValue().getOznaka() + "\nPrviZahtjev: " + entrySet.getValue().getPrvizahtjev()
                                                    + "\nZadnji zahtjev: " + entrySet.getValue().getZadnjiZahtjev() + "\nUkupan broj zahtjeva: " + entrySet.getValue().getUkupanBrojZahtjeva()
                                                    + "\nNeuspjesan br. zahtjeva: " + entrySet.getValue().getNeuspjesanBrojZahtjeva() + "\nUkupno vrijeme rada: " + entrySet.getValue().getUkupnoVrijemeRada()
                                                    + "\nKorisnikovi zahtjevi: ");
                                            if (!entrySet.getValue().getZahtjevi().isEmpty()) {
                                                for (EvidencijaModel.ZahtjevKorisnika item : entrySet.getValue().getZahtjevi()) {
                                                    System.out.println("\tIP adresa: " + item.getIpAdresa()
                                                            + "\n\tVrijeme: " + item.getVrijeme()
                                                            + "\n\tZahtjev: " + item.getZahtjev()
                                                            + "\n\tOdgovor: " + item.getOdgovor() + "\n");
                                                }
                                            }
                                            System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°\n");
                                        }
                                    } else {
                                        System.out.println("ERROR 05; Evidencija rad je prazna!");
                                    }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("UPLOAD")) {

                        } else if (komanda.equals("DOWNLOAD")) {

                        } else {
                            odgovor = "ERROR 90; Pogresna sintaksa komande!";
                        }
                    } else {
                        odgovor = "ERROR 90; Pogresna sintaksa komande!";
                    }
                    os.write(odgovor.getBytes());
                    os.flush();
                    socket.shutdownOutput();

                    kraj = System.currentTimeMillis();
                    ukupnoVrijeme = kraj - pocetak;
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
                evidencijaModel.setUkupnoVrijemeRada(evidencijaModel.getUkupnoVrijemeRada() + ukupnoVrijeme);

                evidencijaRada = Evidencija.getEvidencijaRada();
                evidencijaRada.put(this.getName(), evidencijaModel);
                Evidencija.setEvidencijaRada(evidencijaRada);

                this.setStanje(StanjeDretve.Slobodna);
                this.setCekaj(true);
            }
            // }
            /*System.out.println("prije break;");
             break;*/
        }
        //System.out.println("kraj run metode");
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
    }

    public Konfiguracija getKonfig() {
        return konfig;
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

    public boolean postojiKorisnikLozinka(String korisnik, String lozinka) {
        String datoteka = getKonfig().dajPostavku("adminDatoteka");
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Admin datoteka ne postoji!");
            return false;
        }

        Konfiguracija konfiguracija = null;
        String password = "";
        try {
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            password = konfiguracija.dajPostavku(korisnik);
            if (password.equals(lozinka)) {
                return true;
            }
            return false;
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
