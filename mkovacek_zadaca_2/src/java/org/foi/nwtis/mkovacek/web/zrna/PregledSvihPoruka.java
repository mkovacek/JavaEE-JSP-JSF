/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.mkovacek.web.kontrole.Poruka;
import org.foi.nwtis.mkovacek.web.slusaci.ObradaPoruka;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Managed bean PregledSvihPoruka, služi za pregled svih poruka
 *
 * @author mkovacek
 */
@ManagedBean
@ViewScoped
public class PregledSvihPoruka {

    private String server;
    private String korisnik;
    private String lozinka;
    private List<Poruka> poruke;
    private Poruka poruka;
    private Map<String, String> mape;
    private String odabranaMapa = "INBOX";
    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));
    private int prva = 0;
    private int zadnja = 0;
    private boolean prethodna;
    private boolean sljedeca;

    /**
     * Konstruktor preuzima iz beana EmailPovezivanje odg. podatke, pokreće
     * metodu za preuzimanje svih folder i pokreće metodu za preuzimanje poruka
     * za odabranu mapu
     */
    public PregledSvihPoruka() {
        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        server = ep.getServer();
        korisnik = ep.getKorisnik();
        lozinka = ep.getLozinka();
        prva = stranicenje - (stranicenje - 1);
        zadnja = stranicenje;
        folders();
        poruke = this.messages(prva, zadnja);
    }

    /**
     * Metoda pretražuje mape korisnika i pohranjuje ih u HashMap mape
     *
     */
    public void folders() {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);
            Folder osnovnaMapa = store.getDefaultFolder();
            Folder[] podMape = osnovnaMapa.list();
            mape = new HashMap<>();
            for (Folder f : podMape) {
                mape.put(f.getName(), f.getName());
            }
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda preuzima određeni raspon poruka za određenog korisnika iz odabrane
     * mape.Implementirano stranicenje.
     *
     * @param prva
     * @param zadnja
     * @return poruke
     */
    public List<Poruka> messages(int prva, int zadnja) {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);

            int brojPoruka = folder.getMessageCount();
            poruke = new ArrayList<>();
            Message[] messages = null;

            if (brojPoruka == 0) {
                sljedeca = false;
                prethodna = false;
                store.close();
                return poruke;
            } else if (brojPoruka < stranicenje) {
                messages = folder.getMessages();
                sljedeca = false;
                prethodna = false;
            } else if (brojPoruka < zadnja) {
                int last = brojPoruka;
                messages = folder.getMessages(prva, last);
                sljedeca = false;
                prethodna = true;
            } else if (prva == 1) {
                messages = folder.getMessages(prva, zadnja);
                sljedeca = true;
                prethodna = false;
            } else if (brojPoruka > prva && brojPoruka > zadnja) {
                messages = folder.getMessages(prva, zadnja);
                sljedeca = true;
                prethodna = true;
            } else {
                messages = folder.getMessages(prva, zadnja);
                sljedeca = true;
                prethodna = false;
            }
            for (int i = messages.length - 1; i >= 0; i--) {
                MimeMessage m = (MimeMessage) messages[i];
                String sadrzaj = (String) messages[i].getContent();
                String tip = m.getContentType().toLowerCase();
                Poruka p = new Poruka(m.getHeader("Message-ID")[0], m.getReceivedDate(), m.getFrom()[0].toString(), m.getSubject(), tip, m.getSize(), 0, m.getFlags(), null, false, true, sadrzaj);
                poruke.add(p);
            }
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return poruke;
    }

    /**
     * Metoda preuzima parametar odabrane poruke koji sadrži njezin ID, traži
     * poruka s tim ID-om, i postavlja tu poruku s tim ID-om u varijablu poruka
     * klase PregledPoruke Ovisno o returnu, ostaje se na ovoj stranici ili se
     * prelazi na stranicu za pregled sadržaja te poruke
     *
     * @return "OK"/"NOT_OK"/"ERROR"
     */
    public String pregledPoruke() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String porukaID = params.get("porukaID");
        for (Poruka p : poruke) {
            if (p.getId() != null) {
                if (p.getId().equals(porukaID)) {
                    PregledPoruke.poruka = p;
                    return "OK";
                }
            } else {
                return "NOT_OK";
            }
        }
        return "ERROR";
    }

    /**
     * Metoda azurira raspon dohvacanja poruka, te dohvaca opseg poruka. Raspon
     * se smanjuje za broj stranicenja.
     *
     * @return ""
     */
    public String prethodna() {
        prva = prva - stranicenje;
        zadnja = zadnja - stranicenje;
        poruke = this.messages(prva, zadnja);
        return "";
    }

    /**
     * Metoda azurira raspon dohvacanja poruka, te dohvaca opseg poruka. Raspon
     * se povećava za broj stranicenja.
     *
     * @return ""
     */
    public String sljedeca() {
        prva = prva + stranicenje;
        zadnja = zadnja + stranicenje;
        poruke = this.messages(prva, zadnja);
        return "";
    }

    /**
     * Metoda vraca poruku.
     *
     * @return (Poruka) poruka
     */
    public Poruka getPoruka(String porukaID) {
        return poruka;
    }

    /**
     * Metoda postavlja predmet poruke.
     *
     * @param predmet - predmet
     */
    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    /**
     * Metoda vraca listu poruka.
     *
     * @return (List<Poruka>) poruke
     */
    public List<Poruka> getPoruke() {
        return poruke;
    }

    /**
     * Metoda postavlja listu poruka.
     *
     * @param poruke - lista poruka
     */
    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    /**
     * Metoda sve mape.
     *
     * @return (Map<String, String>) mape
     */
    public Map<String, String> getMape() {
        return mape;
    }

    /**
     * Metoda postavlja sve mape.
     *
     * @param mape - mape
     */
    public void setMape(Map<String, String> mape) {
        this.mape = mape;
    }

    /**
     * Metoda vraca odabranu mapu.
     *
     * @return (Poruka) poruka
     */
    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    /**
     * Metoda postavlja odabranu mapu.
     *
     * @param odabranaMapa - odabrana mapa
     */
    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    /**
     * Metoda dohavća poruke za odabranu mapu i refresha ekran
     *
     * @return ""
     */
    public String odaberiMapu() {
        poruke = this.messages(prva, zadnja);
        return "";
    }

    /**
     * Metoda vraca true ili false za renderiranje gumba "Prethodne"
     *
     * @return (boolean) prethodna
     */
    public boolean isPrethodna() {
        return prethodna;
    }

    /**
     * Metoda postavlja true ili false za renderiranje gumba "Prethodne"
     *
     * @param prethodna - true/false
     */
    public void setPrethodna(boolean prethodna) {
        this.prethodna = prethodna;
    }

    /**
     * Metoda vraca true ili false za renderiranje gumba "Sljedeca"
     *
     * @return (boolean) sljedeca
     */
    public boolean isSljedeca() {
        return sljedeca;
    }

    /**
     * Metoda postavlja true ili false za renderiranje gumba "Sljedeca"
     *
     * @param sljedeca - true/false
     */
    public void setSljedeca(boolean sljedeca) {
        this.sljedeca = sljedeca;
    }

}
