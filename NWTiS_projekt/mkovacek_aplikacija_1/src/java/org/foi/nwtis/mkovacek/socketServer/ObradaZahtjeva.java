/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.socketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.rest.klijenti.GMKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Klasa za obradu zahtjeva prema socket serveru
 *
 * @author Matija
 */
public class ObradaZahtjeva extends Thread {

    private Matcher mParametri;
    private Socket socket;
    private String addUserSintaksa = "^ADD +([a-zA-Z0-9_-]+);\\s* PASSWD +([a-zA-Z0-9-_#!]+);\\s*$";
    private String userSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+);\\s*(ADD +([a-zA-Z0-9_, -]+);|TEST +([a-zA-Z0-9_, -]+);|GET +([a-zA-Z0-9_, -]+);|TYPE;)*$";
    private String adminSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+);\\s*(PAUSE;|START;|STOP;|ADMIN +([a-zA-Z0-9-_#!]+);|NOADMIN +([a-zA-Z0-9-_#!]+);|UPLOAD +([^\\\\s]+)(.+)|DOWNLOAD;)*$";
// crlf private String adminSintaksa = "^USER +([a-zA-Z0-9_-]+); PASSWD +([a-zA-Z0-9-_#!]+);\\s*(PAUSE;|START;|STOP;|ADMIN +([a-zA-Z0-9-_#!]+);|NOADMIN +([a-zA-Z0-9-_#!]+);|UPLOAD +([^\\s]+)([\\r\\n|\\r|\\n]+)(.+)|DOWNLOAD;)*$";

    private String smtpHost = SlusacAplikacije.konfig.dajPostavku("emailServer");
    private String salje = SlusacAplikacije.konfig.dajPostavku("user");
    private String prima = SlusacAplikacije.konfig.dajPostavku("email");
    private String predmet = SlusacAplikacije.konfig.dajPostavku("predmet");

    /**
     * Metoda prima i obrađuje socket server zahtjeve
     *
     */
    @Override
    public void run() {
        if (!ServerSustava.isStop()) {
            System.out.println("Obrada zahtjeva!!!");
            InputStream is = null;
            OutputStream os = null;
            String zahtjev = null;
            String odgovor = null;
            String korisnik = "";
            String lozinka = "";
            Date zahtjevi;
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String ipAdresa = "";
            BazaPodataka bp = new BazaPodataka();
            Korisnik user = new Korisnik();

            try {
                zahtjevi = new Date();

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
                System.out.println(zahtjev);

                ProvjeraZahtjeva pz = new ProvjeraZahtjeva();
                pz = provjeraZahtjeva(zahtjev);
                System.out.println("status zahtjeva: " + pz.getStatus());

                if (pz.getStatus().equals("newUser")) {
                    mParametri = pz.getM();
                    korisnik = this.mParametri.group(1);
                    lozinka = this.mParametri.group(2);
                    user = bp.provjeraKorisnika(korisnik);
                    if (user == null) {
                        bp.spremiKorisnika(korisnik, lozinka);

                        String brojKorisnika = Integer.toString(bp.trenutniBrojKorisnika());

                        String poruka = "ADD " + korisnik + "; PASSWD " + lozinka + ";"
                                + "<br/> Vrijeme primanja zahtjeva: " + date.format(zahtjevi)
                                + "<br/> Trenutni broj korisnika: " + brojKorisnika;
                        this.slanjeEmailPoruke(poruka);
                        odgovor = "OK 10;";
                    } else {
                        String brojKorisnika = Integer.toString(bp.trenutniBrojKorisnika());
                        String poruka = "ADD " + korisnik + "; PASSWD " + lozinka + ";"
                                + "<br/> Vrijeme primanja zahtjeva: " + date.format(zahtjevi)
                                + "<br/> Trenutni broj korisnika: " + brojKorisnika;
                        this.slanjeEmailPoruke(poruka);

                        odgovor = "ERR 50;";
                    }
                } else if (pz.getStatus().equals("user")) {
                    mParametri = pz.getM();
                    korisnik = this.mParametri.group(1);
                    lozinka = this.mParametri.group(2);
                    String komanda = "";
                    komanda = this.mParametri.group(3) != null ? this.mParametri.group(3) : "autentifikacija";

                    if (komanda.equals("autentifikacija")) {
                        user = bp.autentifikacija(korisnik, lozinka);
                        odgovor = user != null ? "OK 10;" : "ERR 20;";
                    } else {
                        user = bp.autentifikacija(korisnik, lozinka);
                        if (user != null) {
                            if (komanda.startsWith("ADD")) {
                                double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("ADD"));
                                if (user.getRacun() >= iznos) {
                                    double saldo = user.getRacun() - iznos;
                                    String adresa = this.mParametri.group(4);
                                    System.out.println("ADD adresa: " + adresa);
                                    if (!bp.provjeraAdrese(adresa)) {
                                        Lokacija lokacija = new Lokacija();
                                        GMKlijent gmk = new GMKlijent();
                                        lokacija = gmk.getGeoLocation(adresa);
                                        Adresa adr = new Adresa(adresa, lokacija, korisnik);
                                        bp.spremiAdresu(adr);
                                        bp.azurirajSredstva(user.getKorIme(), saldo);
                                        odgovor = "OK 10;";
                                    } else {
                                        odgovor = "ERR 41;";
                                    }
                                } else {
                                    odgovor = "ERR 40;";
                                }

                            } else if (komanda.startsWith("TEST")) {
                                double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("TEST"));
                                if (user.getRacun() >= iznos) {
                                    double saldo = user.getRacun() - iznos;
                                    String adresa = this.mParametri.group(5);
                                    System.out.println("TEST adresa: " + adresa);
                                    odgovor = bp.provjeraAdrese(adresa) ? "OK 10;" : "ERR 42;";
                                } else {
                                    odgovor = "ERR 40;";
                                }
                            } else if (komanda.startsWith("GET")) {
                                double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("GET"));
                                if (user.getRacun() >= iznos) {
                                    double saldo = user.getRacun() - iznos;
                                    String adresa = this.mParametri.group(6);
                                    if (bp.provjeraAdrese(adresa)) {
                                        MeteoPodaci mp = bp.dajZadnjeMeteoPodatkeZaAdresu(adresa);
                                        odgovor = "OK 10; DATUM " + mp.getLastUpdate()
                                                + " VRIJEME " + mp.getWeatherValue() + " TEMP " + mp.getTemperatureValue()
                                                + " TLAK " + mp.getPressureValue();
                                    } else {
                                        odgovor = "ERR 43;";
                                    }
                                } else {
                                    odgovor = "ERR 40;";
                                }
                            } else if (komanda.startsWith("TYPE")) {
                                if (user.getTip() == 0) {
                                    odgovor = "OK 10;";
                                } else if (user.getTip() == 1) {
                                    odgovor = "OK 11;";
                                }
                            }
                        } else {
                            odgovor = "ERR 20;";
                        }
                    }
                } else if (pz.getStatus().equals("admin")) {
                    mParametri = pz.getM();
                    korisnik = this.mParametri.group(1);
                    lozinka = this.mParametri.group(2);
                    String komanda = "";

                    komanda = this.mParametri.group(3) != null ? this.mParametri.group(3) : "autentifikacija";
                    if (komanda.equals("autentifikacija")) {
                        user = bp.autentifikacija(korisnik, lozinka);
                        odgovor = user != null ? "OK 10;" : "ERR 20;";
                    } else {
                        user = bp.autentifikacija(korisnik, lozinka);
                        if (user != null) {
                            if (user.getTip() != 1) {
                                odgovor = "ERR 21;";
                            } else {
                                if (komanda.equals("PAUSE;")) {
                                    if (!ServerSustava.isPauza()) {
                                        ServerSustava.setPauza(true);
                                        odgovor = "OK 10;";
                                    } else {
                                        odgovor = "ERR 30;";
                                    }
                                } else if (komanda.equals("START;")) {
                                    if (ServerSustava.isPauza()) {
                                        ServerSustava.setPauza(false);
                                        odgovor = "OK 10;";
                                    } else {
                                        odgovor = "ERR 31;";
                                    }
                                } else if (komanda.equals("STOP;")) {
                                    if (!ServerSustava.isStop()) {
                                        ServerSustava.setStop(true);
                                        odgovor = "OK 10;";
                                    } else {
                                        odgovor = "ERR 32;";
                                    }
                                } else if (komanda.startsWith("ADMIN")) {
                                    Korisnik kor = new Korisnik();
                                    String korIme = this.mParametri.group(4);
                                    kor = bp.provjeraKorisnika(korIme);
                                    if (kor == null) {
                                        odgovor = "ERR 33;";
                                    }
                                    if (kor.getTip() == 0) {
                                        bp.postaviRolu(korIme, 1);
                                        odgovor = "OK 10;";
                                    }
                                    if (kor.getTip() == 1) {
                                        odgovor = "ERR 34;";
                                    }
                                } else if (komanda.startsWith("NOADMIN")) {
                                    Korisnik kor = new Korisnik();
                                    String korIme = this.mParametri.group(5);
                                    kor = bp.provjeraKorisnika(korIme);

                                    if (kor == null) {
                                        odgovor = "ERR 33;";
                                    }
                                    if (kor.getTip() == 1) {
                                        bp.postaviRolu(korIme, 0);
                                        odgovor = "OK 10;";
                                    }
                                    if (kor.getTip() == 0) {
                                        odgovor = "ERR 35;";
                                    }
                                } else if (komanda.equals("DOWNLOAD;")) {
                                    //TODO
                                    odgovor = "Krivi zahtjev!";
                                } else if (komanda.startsWith("UPLOAD")) {
                                    //TODO
                                    odgovor = "Krivi zahtjev!";
                                }
                            }
                        } else {
                            odgovor = "ERR 20;";
                        }
                    }
                } else {
                    odgovor = "Krivi zahtjev!";
                }
                ipAdresa = socket.getRemoteSocketAddress().toString();
                os.write(odgovor.getBytes());
                os.flush();
                socket.shutdownOutput();
                System.out.println("user: " + korisnik);
                System.out.println("ipadresa: " + ipAdresa);
                System.out.println("zahtjev " + zahtjev);
                bp.log(korisnik,zahtjev,ipAdresa);
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
        }
    }

    /**
     * Metoda provjerava ispravnost zahtjeva.Ukoliko je ispravan vraca
     * informaciju o cijem zahtjevu se radi.
     *
     * @param komanda - zahtjev
     */
    public ProvjeraZahtjeva provjeraZahtjeva(String komanda) {
        Pattern pattern = Pattern.compile(addUserSintaksa);
        Matcher m = pattern.matcher(komanda);
        boolean status = m.matches();
        ProvjeraZahtjeva pz = new ProvjeraZahtjeva();
        if (status) {
            pz.setStatus("newUser");
            pz.setM(m);
            return pz;
        } else {
            Pattern pattern2 = Pattern.compile(userSintaksa);
            Matcher m2 = pattern2.matcher(komanda);
            boolean status2 = m2.matches();
            if (status2) {
                pz.setStatus("user");
                pz.setM(m2);
                return pz;
            } else {
                Pattern pattern3 = Pattern.compile(adminSintaksa);
                Matcher m3 = pattern3.matcher(komanda);
                boolean status3 = m3.matches();
                if (status3) {
                    pz.setStatus("admin");
                    pz.setM(m3);
                    return pz;
                } else {
                    pz.setStatus("error");
                    pz.setM(m3);
                    return pz;
                }
            }
        }
    }

    /**
     * Metoda za slanje email poruka
     *
     * @param poruka - sadržaj poruke
     */
    public void slanjeEmailPoruke(String poruka) {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", smtpHost);
            Session session = Session.getInstance(properties, null);
            MimeMessage message = new MimeMessage(session);
            Address fromAddress = new InternetAddress(salje);
            message.setFrom(fromAddress);
            Address[] toAddresses = InternetAddress.parse(prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(predmet);
            message.setText(poruka);
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
