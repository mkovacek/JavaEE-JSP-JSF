/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
 * Klasa ObradaZahtjeva obrađuje korisnikove zahtjeve. Preuzima ulazne i izlazne
 * podatke prema korisniku.
 *
 * @author Matija Kovacek
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
    public static String nazivKonfiguracijskeDatoteke;
    private String userSintaksa = "^USER +([a-zA-Z0-9_-]+); TIME; $";
    private String adminSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+); (PAUSE;|START;|STOP;|CLEAN;|STAT;|UPLOAD +([^\\s]+)([\\r\\n|\\r|\\n]+)(.+)|DOWNLOAD;)$";
    public static HashMap<String, EvidencijaModel> evidencijaRada = new HashMap<>();

    /**
     * Konstruktor
     *
     * @param group - grupa dretvi
     * @param name - naziv grupe dretvi
     */
    public ObradaZahtjeva(ThreadGroup group, String name) {
        super(group, name);
        this.stanje = StanjeDretve.Slobodna;
    }

    /**
     * Overridde-ana thread run() metoda. Metoda obrađuje korisnikove zahtjeve.
     *
     */
    @Override
    public void run() {
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
        SimpleDateFormat dateSerijalizacija = new SimpleDateFormat("ddMMyyyy_HHmmss");
        SimpleDateFormat dateSetSadrzaj = new SimpleDateFormat("ddMMyyyy_HHmmss");

        while (!ServerSustava.isStop()) {
            synchronized (this) {
                while (this.isCekaj()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
                    System.out.println(sb.toString() + date.format(new Date()));

                    if (provjeraZahtjeva(zahtjev).equals("true;user")) {
                        if (ServerSustava.isPauza()) {
                            odgovor = "ERROR 01: Server je u stanju pauze!";
                        } else {
                            odgovor = "OK; " + date.format(new Date());
                        }
                    } else if (provjeraZahtjeva(zahtjev).equals("true;admin")) {
                        this.mParametri = adminZahtjev(zahtjev);
                        String korisnik = this.mParametri.group(1);
                        String lozinka = this.mParametri.group(2);
                        String komanda = this.mParametri.group(3);
                        if (komanda.equals("PAUSE;")) {
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
                        } else if (komanda.equals("START;")) {
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
                        } else if (komanda.equals("STOP;")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                if (!ServerSustava.isStop()) {
                                    ServerSustava.setStop(true);
                                    String nazivDatoteke = getKonfig().dajPostavku("adminDatoteka");
                                    String[] datoteka = nazivDatoteke.split("\\.");
                                    SerijalizatorEvidencije.serijalizator(datoteka[0] + dateSerijalizacija.format(new Date()) + "." + datoteka[1]);
                                    odgovor = "OK";
                                } else {
                                    odgovor = "ERROR 03: Server stopiran!";
                                }
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else if (komanda.equals("CLEAN;")) {
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
                        } else if (komanda.equals("STAT;")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                odgovor = "OK;\r\n";
                                if (!Evidencija.getEvidencijaRada().isEmpty()) {
                                    for (Map.Entry<String, EvidencijaModel> entrySet : Evidencija.getEvidencijaRada().entrySet()) {
                                        odgovor += "Oznaka: " + entrySet.getValue().getOznaka() + "\tPrviZahtjev: " + entrySet.getValue().getPrvizahtjev()
                                                + "\tZadnji zahtjev: " + entrySet.getValue().getZadnjiZahtjev() + "\tUkupan br. zahtjeva: " + entrySet.getValue().getUkupanBrojZahtjeva()
                                                + "\tNeuspjesan br. zahtjeva: " + entrySet.getValue().getNeuspjesanBrojZahtjeva() + "\tUkupno vrijeme rada: " + entrySet.getValue().getUkupnoVrijemeRada() + "\n";
                                    }
                                } else {
                                    System.out.println("ERROR 05; Evidencija rad je prazna!");
                                }
                            } else {
                                odgovor = "ERROR 01: Server je u stanju pauze!";
                            }
                        } else if (komanda.startsWith("UPLOAD")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                String sadrzajDatoteke = this.mParametri.group(6);
                                String nazivDatoteke = getKonfig().dajPostavku("adminDatoteka");
                                String[] datoteka = nazivDatoteke.split("\\.");
                                String konacniNaziv = datoteka[0] + dateSetSadrzaj.format(new Date()) + "." + datoteka[1];
                                if (!this.provjeraDatoteke(konacniNaziv)) {
                                    if (this.setSadrzajDatoteke(konacniNaziv, sadrzajDatoteke)) {
                                        String[] velicina = this.mParametri.group(4).split(";");
                                        if (Integer.toString(sadrzajDatoteke.length()).equals(velicina[0])) {
                                            odgovor = "OK";
                                        } else {
                                            if (this.obrisiDatoteku(konacniNaziv)) {
                                                odgovor = "ERROR 07; Velicina sadrzaja ne odgovara, datoteka je obrisana!";
                                            } else {
                                                System.out.println("Problem kod brisanja datoteke");
                                            }
                                        }
                                    } else {
                                        System.out.println("Problem kod zapisivanja sadrzaja u datoteku!");
                                    }
                                } else {
                                    odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                                }
                            } else {
                                odgovor = "ERROR 01: Server je u stanju pauze!";
                            }
                        } else if (komanda.startsWith("DOWNLOAD")) {
                            if (postojiKorisnikLozinka(korisnik, lozinka)) {
                                String sadrzajKonfiguracije = this.getSadrzajDatoteke(ObradaZahtjeva.getNazivKonfiguracijskeDatoteke());
                                String velicina = Integer.toString(sadrzajKonfiguracije.length());
                                odgovor = "DATA " + velicina + ";\r\n" + sadrzajKonfiguracije;
                            } else {
                                odgovor = "ERROR 00; Korisnik nije admin ili lozinka ne odgovara!";
                            }
                        } else {
                            odgovor = "ERROR 01: Server je u stanju pauze!";
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
        }
    }

    /**
     * Metoda provjerava ispravnost zahtjeva administratora.
     *
     * @param komanda - zahtjev administratora
     * @return matcher ili null
     */
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

    /**
     * Metoda provjerava ispravnost zahtjeva.Ukoliko je ispravan vraca
     * informaciju o cijem zahtjevu se radi.
     *
     * @param komanda - zahtjev administratora ili korisnika
     * @return "true;user" -zahtjev korisnika ili "true;admin" -zahtjev admina
     * ili "false;false" - neispravan zahtjev
     */
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

    /**
     * Metoda provjerava postoji li korisnik i njemu pridruzena lozinka u
     * datoteci administratora sustava ciji je naziv određen postavkom
     * adminDatoteka.
     *
     * @param korisnik - naziv korisnika
     * @param lozinka - lozinka korisnika
     * @return true ili false
     */
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

    /**
     * Metoda provjerava da li postoji datoteka.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return true ili false
     */
    public boolean provjeraDatoteke(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        if (!dat.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Metoda za brisanje datoteke.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return true ili false
     */
    public boolean obrisiDatoteku(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        if (dat.exists()) {
            System.gc();
            return dat.delete();
        }
        return false;
    }

    /**
     * Metoda upisivanje sadrzaja u datoteku.
     *
     * @param nazivDatoteke - naziv datoteke
     * @param sadrzaj - sadrzaj
     * @return true ili false
     */
    public boolean setSadrzajDatoteke(String nazivDatoteke, String sadrzaj) {
        File dat = new File(nazivDatoteke);
        try {
            FileOutputStream fos = new FileOutputStream(dat);
            fos.write(sadrzaj.getBytes());
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda za dohvacanje sadrzaja iz datoteke.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return (string) sadrzaj
     */
    public String getSadrzajDatoteke(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = new FileInputStream(dat);
            sb = new StringBuilder();
            while (true) {
                int znak = fis.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
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

    /**
     * Metoda postavlja konfiguraciju.
     *
     * @param konfig - konfiguracija
     */
    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
    }

    /**
     * Metoda dohvaca konfiguraciju.
     *
     * @return konfig
     */
    public Konfiguracija getKonfig() {
        return konfig;
    }

    /**
     * Metoda postavlja socket.
     *
     * @param socket - socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Metoda postavlja stanje dretve.
     *
     * @param stanje - stanje dretve
     */
    public void setStanje(StanjeDretve stanje) {
        this.stanje = stanje;
    }

    /**
     * Metoda dohvaca stanje dretve.
     *
     * @return slobodna ili zauzeta
     */
    public StanjeDretve getStanje() {
        return stanje;
    }

    /**
     * Metoda dohvaca da li dretva ceka.
     *
     * @return true ili false
     */
    public boolean isCekaj() {
        return cekaj;
    }

    /**
     * Metoda postavlja dretvu u cekanje ili ne cekanje.
     *
     * @param cekaj - true ili false
     */
    public void setCekaj(boolean cekaj) {
        this.cekaj = cekaj;
    }

    /**
     * Metoda dohvaca naziv konfiguracijske datoteke.
     *
     * @return (String) naziv konfiguracijske datoteke
     */
    public static String getNazivKonfiguracijskeDatoteke() {
        return nazivKonfiguracijskeDatoteke;
    }

    /**
     * Metoda postavlja naziv konfiguracijske datoteke.
     *
     * @param nazivKonfiguracijskeDatoteke - naziv konfiguracijske datoteke
     */
    public void setNazivKonfiguracijskeDatoteke(String nazivKonfiguracijskeDatoteke) {
        ObradaZahtjeva.nazivKonfiguracijskeDatoteke = nazivKonfiguracijskeDatoteke;
    }

}
