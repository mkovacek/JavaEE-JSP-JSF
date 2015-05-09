/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.slusaci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;

/**
 * Klasa ObradaPoruka provjerava i obrađuje email poruke.Vise u opisu run metode
 *
 * @author mkovacek
 */
public class ObradaPoruka extends Thread {

    private BP_Konfiguracija konfigDB;
    private Konfiguracija konfig;
    private String direktorij;
    private String sintaksa = "^USER +([a-zA-Z0-9]+); PASSWORD +([a-zA-Z0-9]+); KEY +([a-zA-Z0-9]+); VALUE +([a-zA-Z0-9]+); (ADD|UPDATE|REMOVE);$";

    /**
     * Overridde-ana thread run() metoda. Metoda na poslužitelju provjerava
     * email poruke, provjerava predmet i sadržaj poruke, ovisno o sadržaju
     * izvršava određene akcije nad properies datotekama.Na kraju prebacuje
     * poruku u odgovarajući folder i šalje email poruku s statistikom.
     *
     */
    @Override
    public void run() {

        String server = konfig.dajPostavku("server");
        int port = Integer.parseInt(konfig.dajPostavku("port"));
        String korisnik = konfig.dajPostavku("user");
        String lozinka = konfig.dajPostavku("password");
        String mapaNWTiSPoruke = konfig.dajPostavku("ispravnePoruke");
        String mapaOstalePoruke = konfig.dajPostavku("ostalePoruke");
        String mapaPoslano = konfig.dajPostavku("direktorij");
        String predmet = konfig.dajPostavku("subject");
        int brojSekundiZaSpavanje = Integer.parseInt(konfig.dajPostavku("interval"));
        
        Properties properties = null;
        Session session = null;
        Store store = null;
        Folder osnovnaMapa = null;
        Folder inboxMapa = null;
        Message[] messages = null;
        Matcher matcher = null;

        long pocetak = 0;
        long kraj = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.zzz");
        int ukupanBrPoruka = 0;
        int brPoruka = 0;
        int brDodanih = 0;
        int brAzuriranih = 0;
        int brObrisanih = 0;
        int brPogresaka = 0;
        int brojPoruke = 0;
        List<String> listaDatoteka = new ArrayList();
        List<String> listaDirektorija = new ArrayList();

        while (true) {
            try {
                pocetak = System.currentTimeMillis();
                
                properties = System.getProperties();
                properties.put("mail.smtp.host", server);
                session = Session.getInstance(properties, null);

                store = session.getStore("imap");
                store.connect(server, port, korisnik, lozinka);

                osnovnaMapa = store.getDefaultFolder();
                if (!osnovnaMapa.getFolder(mapaNWTiSPoruke).exists()) {
                    osnovnaMapa.getFolder(mapaNWTiSPoruke).create(Folder.HOLDS_MESSAGES);
                }

                if (!osnovnaMapa.getFolder(mapaOstalePoruke).exists()) {
                    osnovnaMapa.getFolder(mapaOstalePoruke).create(Folder.HOLDS_MESSAGES);
                }

                if (!osnovnaMapa.getFolder(mapaPoslano).exists()) {
                    osnovnaMapa.getFolder(mapaPoslano).create(Folder.HOLDS_MESSAGES);
                }

                inboxMapa = store.getFolder("INBOX");
                inboxMapa.open(Folder.READ_WRITE); 
                messages = inboxMapa.getMessages();
                
                for (int i = 0; i < messages.length; ++i) {
                    ukupanBrPoruka++;
                    brPoruka++;
                    MimeMessage m = (MimeMessage) messages[i];
                    String tip = m.getContentType().toLowerCase();
                    if (tip.startsWith("text/plain")) {
                        if (m.getSubject().equals(predmet)) {
                            String sadrzaj = (String) m.getContent();
                            matcher = provjeraSadrzaja(sadrzaj.trim());
                            if (matcher != null) {
                                String user = matcher.group(1);
                                String pass = matcher.group(2);

                                if (provjeraKorisnika(user, pass)) {
                                    String key = matcher.group(3);
                                    String value = matcher.group(4);
                                    String action = matcher.group(5);

                                    File propDatDir = new File(getDirektorij() + File.separator + getKonfig().dajPostavku("mapa") + File.separator + user + File.separator);
                                    if (!propDatDir.exists()) {
                                        propDatDir.mkdirs();
                                        listaDirektorija.add(getKonfig().dajPostavku("mapa") + File.separator + user);
                                    }
                                    File propDat = new File(propDatDir, getKonfig().dajPostavku("datoteka"));
                                    Properties prop = new Properties();
                                    FileOutputStream output = null;
                                    FileInputStream input = null;
                                    try {
                                        if (propDat.exists()) {
                                            input = new FileInputStream(propDat);
                                            prop.load(input);
                                            input.close();
                                        }
                                        if (action.equals("UPDATE")) {
                                            if (prop.containsKey(key)) {
                                                String oldValue = prop.get(key).toString();
                                                if (prop.replace(key, oldValue, value)) {
                                                    output = new FileOutputStream(propDat);
                                                    prop.store(output, null);
                                                    brAzuriranih++;
                                                } else {
                                                    brPogresaka++;
                                                }
                                            } else {
                                                brPogresaka++;
                                            }
                                        } else if (action.equals("REMOVE")) {
                                            if (prop.containsKey(key)) {
                                                prop.remove(key);
                                                output = new FileOutputStream(propDat);
                                                prop.store(output, null);
                                                brObrisanih++;
                                            } else {
                                                brPogresaka++;
                                            }
                                        } else {
                                            if (!prop.containsKey(key)) {
                                                if (!propDat.exists()) {
                                                    listaDatoteka.add(getKonfig().dajPostavku("mapa") + File.separator + user + File.separator + getKonfig().dajPostavku("datoteka"));
                                                }
                                                prop.setProperty(key, value);
                                                output = new FileOutputStream(propDat);
                                                prop.store(output, null);
                                                brDodanih++;
                                            } else {
                                                brPogresaka++;
                                            }
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if (output != null) {
                                        try {
                                            output.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    messageTransfer(messages[i], mapaNWTiSPoruke, store);
                                } else {
                                    messageTransfer(messages[i], mapaNWTiSPoruke, store);
                                }
                            } else {
                                messageTransfer(messages[i], mapaOstalePoruke, store);
                            }
                        } else {
                            messageTransfer(messages[i], mapaOstalePoruke, store);
                        }
                    } else {
                        messageTransfer(messages[i], mapaOstalePoruke, store);
                    }
                }
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }

            kraj = System.currentTimeMillis();
            long trajanjeObrade = kraj - pocetak;

            String statistika = "Obrada zapocela u: " + sdf.format(new Date(pocetak)) + "<br/>Obrada zavrsila u: " + sdf.format(new Date(kraj))
                    + "<br/>Trajanje obrade u ms: " + trajanjeObrade + "<br/>Ukupan broj poruka: " + ukupanBrPoruka
                    + "<br/>Broj poruka: " + brPoruka + "<br/>Broj dodanih poruka: " + brDodanih + "<br/>Broj azuriranih poruka: " + brAzuriranih
                    + "<br/>Broj obrisanih poruka: " + brObrisanih + "<br/>Broj pogresaka kod podataka: " + brPogresaka
                    + "<br/>Kreirani direktoriji: " + listaDirektorija + "<br/>Kreirane datoteke: " + listaDatoteka;

            brPoruka = 0;
            brDodanih = 0;
            brAzuriranih = 0;
            brObrisanih = 0;
            brPogresaka = 0;
            listaDatoteka.clear();
            listaDirektorija.clear();
            brojPoruke++;
            saljiPoruku(statistika, String.valueOf(brojPoruke), session, store);
            
            if (inboxMapa.isOpen()) {
                try {
                    inboxMapa.close(true);
                } catch (MessagingException ex) {
                    Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException ex) {
                    Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                long spavanje = brojSekundiZaSpavanje * 1000;
                if (trajanjeObrade < spavanje) {
                    sleep(spavanje - trajanjeObrade);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    /**
     * Metoda provjerava ispravnost sadržaja poruke
     *
     * @param sadrzaj - sadržaj za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraSadrzaja(String sadrzaj) {
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(sadrzaj);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            return null;
        }
    }

    /**
     * Metoda za autentifikaciju korisnika prema bazi podataka
     *
     * @param user - korisničko ime
     * @param pass - lozinka
     * @return true/false
     */
    private boolean provjeraKorisnika(String user, String pass) {
        String server = getKonfigDB().getServer_database();
        String baza = server + getKonfigDB().getUser_database();
        String korisnik = getKonfigDB().getUser_username();
        String lozinka = getKonfigDB().getUser_password();
        String sql = "select KOR_IME, LOZINKA from POLAZNICI where KOR_IME=? and LOZINKA=?";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda prebacuje email poruku u određenu mapu i briše poruke iz postojeće
     * mape
     *
     * @param messages - poruka
     * @param folder - folder
     * @param store - store
     */
    private void messageTransfer(Message messages, String folder, Store store) {
        Folder f;
        try {
            f = store.getFolder(folder);
            f.appendMessages(new Message[]{messages});
            messages.setFlag(Flags.Flag.DELETED, true);
            if (f.isOpen()) {
                f.close(true);
            }
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda šalje email poruku
     *
     * @param tekstPoruke - tekst poruke
     * @param brojPoruke - broj poslane poruke
     * @param session - session
     * @param store - store
     */
    public void saljiPoruku(String tekstPoruke, String brojPoruke, Session session, Store store) {
        try {
            MimeMessage message = new MimeMessage(session);
            Address fromAddress = new InternetAddress(getKonfig().dajPostavku("user"));
            message.setFrom(fromAddress);
            Address[] toAddresses = InternetAddress.parse(getKonfig().dajPostavku("email"));
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(getKonfig().dajPostavku("predmet") + "." + brojPoruke);
            message.setContent(tekstPoruke, "text/html; charset=utf-8");
            Transport.send(message);
            String folder = getKonfig().dajPostavku("direktorij");
            messageTransfer(message, folder, store);
        } catch (AddressException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda vraca direktorij.
     *
     * @return (String) direktorij
     */
    public String getDirektorij() {
        return direktorij;
    }

    /**
     * Metoda postavlja direktorij.
     *
     * @param direktorij - direktorij
     */
    public void setDirektorij(String direktorij) {
        this.direktorij = direktorij;
    }

    /**
     * Metoda vraca konfiguraciju DB.
     *
     * @return (BP_Konfiguracija) konfigDB
     */
    public BP_Konfiguracija getKonfigDB() {
        return konfigDB;
    }

    /**
     * Metoda postavlja konfiguraciju DB.
     *
     * @param konfigDB - konfiguracija
     */
    public void setKonfigDB(BP_Konfiguracija konfigDB) {
        this.konfigDB = konfigDB;
    }

    /**
     * Metoda vraca konfiguraciju.
     *
     * @return (Konfiguracija) konfig
     */
    public Konfiguracija getKonfig() {
        return konfig;
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
     * Overridde-ana thread interrupt() metoda.
     *
     */
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Overridde-ana thread start() metoda.
     *
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
