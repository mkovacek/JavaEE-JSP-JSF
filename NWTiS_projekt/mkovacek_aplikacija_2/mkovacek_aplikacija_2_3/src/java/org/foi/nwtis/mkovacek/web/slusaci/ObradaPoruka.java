/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.slusaci;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jms.JMSException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.jms.EmailMessages;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.EmailOM;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.Korisnik;
import org.foi.nwtis.mkovacek.ejb.sb.KorisniciFacade;

/**
 * Klasa ObradaPoruka provjerava i obrađuje email poruke.
 *
 * @author mkovacek
 */
public class ObradaPoruka extends Thread implements Serializable {

    KorisniciFacade korisniciFacade = lookupKorisniciFacadeBean();
    private Korisnici user;
    private String sintaksa = "^ADD +([a-zA-Z0-9_-]+);\\s* PASSWD +([a-zA-Z0-9-_#!]+);\\s*$";

    /**
     * Overridde-ana thread run() metoda. Metoda na poslužitelju provjerava
     * email poruke, provjerava predmet i sadržaj poruke, ovisno o sadržaju
     * izvršava određene akcije.Na kraju prebacuje poruku u odgovarajući folder
     * i šalje JMS poruku s statistikom.
     *
     */
    @Override
    public void run() {
        System.out.println("Obrada poruka run");
        String server = SlusacAplikacije.konfig.dajPostavku("emailServer");
        int port = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("emailPort"));

        String korisnik = SlusacAplikacije.konfig.dajPostavku("email");
        String lozinka = SlusacAplikacije.konfig.dajPostavku("password");

        String mapaNWTiSPoruke = SlusacAplikacije.konfig.dajPostavku("ispravnePoruke");
        String mapaOstalePoruke = SlusacAplikacije.konfig.dajPostavku("ostalePoruke");
        String predmet = SlusacAplikacije.konfig.dajPostavku("predmet");
        int brojSekundiZaSpavanje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("sleep"));

        Properties properties = null;
        Session session = null;
        Store store = null;
        Folder osnovnaMapa = null;
        Folder inboxMapa = null;
        Message[] messages = null;
        Matcher matcher = null;

        long pocetak = 0;
        long kraj = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

        while (true) {
            try {
                List<Korisnik> dodaniKorisnici = new ArrayList<>();
                List<Korisnik> ostaliKorisnici = new ArrayList<>();
                int ukupanoPoruka = 0;
                int brNWTiSporuka = 0;
                System.out.println("Obrada email poruka..");
                try {
                    pocetak = System.currentTimeMillis();

                    properties = System.getProperties();
                    properties.put("mail.smtp.host", server);
                    session = Session.getDefaultInstance(properties, null);

                    store = session.getStore("imap");
                    store.connect(server, port, korisnik, lozinka);

                    osnovnaMapa = store.getDefaultFolder();
                    if (!osnovnaMapa.getFolder(mapaNWTiSPoruke).exists()) {
                        osnovnaMapa.getFolder(mapaNWTiSPoruke).create(Folder.HOLDS_MESSAGES);
                    }

                    if (!osnovnaMapa.getFolder(mapaOstalePoruke).exists()) {
                        osnovnaMapa.getFolder(mapaOstalePoruke).create(Folder.HOLDS_MESSAGES);
                    }

                    inboxMapa = store.getFolder("INBOX");
                    inboxMapa.open(Folder.READ_WRITE);
                    messages = inboxMapa.getMessages();

                    for (int i = 0; i < messages.length; ++i) {
                        ukupanoPoruka++;
                        MimeMessage m = (MimeMessage) messages[i];
                        String tip = m.getContentType().toLowerCase();
                        if (tip.startsWith("text/plain")) {
                            if (m.getSubject().equals(predmet)) {
                                brNWTiSporuka++;
                                String sadrzaj = (String) m.getContent();
                                System.out.println("Sadrzaj poruke: " + sadrzaj);
                                if (sadrzaj.startsWith("ADD")) {
                                    String[] s = sadrzaj.split("<br/>");
                                    String noviKorisnik = s[0];
                                    System.out.println("komanda: " + noviKorisnik);
                                    matcher = provjeraSadrzaja(noviKorisnik.trim());
                                    if (matcher != null) {
                                        String korIme = matcher.group(1);
                                        String pass = matcher.group(2);
                                        if (!korisniciFacade.provjeraKorisnika(korIme)) {
                                            Korisnik okKor = new Korisnik(korIme, pass);
                                            dodaniKorisnici.add(okKor);
                                            System.out.println("korisnik ne postoji");
                                            user = new Korisnici();
                                            user.setKorime(korIme);
                                            user.setLozinka(pass);
                                            user.setDatumKreiranja(new Date());
                                            user.setVrsta(0);
                                            user.setEmailAdresa(" ");
                                            user.setIme(" ");
                                            user.setPrezime(" ");
                                            korisniciFacade.create(user);
                                            System.out.println("korisnik je kreiran");
                                            messageTransfer(messages[i], mapaNWTiSPoruke, store);
                                        } else {
                                            System.out.println("korisnik vec postoji");
                                            Korisnik okKor = new Korisnik(korIme, pass);
                                            ostaliKorisnici.add(okKor);
                                            messageTransfer(messages[i], mapaNWTiSPoruke, store);
                                        }
                                    } else {
                                        messageTransfer(messages[i], mapaNWTiSPoruke, store);
                                    }
                                } else {
                                    messageTransfer(messages[i], mapaNWTiSPoruke, store);
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

                String vrijemePocetka = sdf.format(new Date(pocetak));
                String vrijemeKraja = sdf.format(new Date(kraj));
                EmailOM objectMessage = new EmailOM(vrijemePocetka, vrijemeKraja, ukupanoPoruka, brNWTiSporuka, dodaniKorisnici, ostaliKorisnici);
                EmailMessages em = new EmailMessages();
                em.sendJMSMessageToNWTiS_mkovacek_1(objectMessage);

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
            } catch (JMSException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
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

    private KorisniciFacade lookupKorisniciFacadeBean() {
        try {
            Context c = new InitialContext();
            return (KorisniciFacade) c.lookup("java:global/mkovacek_aplikacija_2/mkovacek_aplikacija_2_2/KorisniciFacade!org.foi.nwtis.mkovacek.ejb.sb.KorisniciFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
