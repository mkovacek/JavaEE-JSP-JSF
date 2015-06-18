/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.web.podaci.Poruka;
import org.foi.nwtis.mkovacek.web.slusaci.ObradaPoruka;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * ManagedBean za pregled email poruka
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledPoruka {

    private HttpSession session;
    private FacesContext context;
    private String server = SlusacAplikacije.konfig.dajPostavku("emailServer");
    private String korisnik = SlusacAplikacije.konfig.dajPostavku("email");
    private String lozinka = SlusacAplikacije.konfig.dajPostavku("password");
    private List<Poruka> poruke;
    private Poruka poruka;
    private Poruka odabranaPoruka;
    private Map<String, String> mape;
    private String odabranaMapa = "NWTiS_IspravnePoruke";
    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));
    private boolean prikazForme = false;
    private boolean obrisano = false;

    /**
     * Creates a new instance of PregledPoruka
     */
    public PregledPoruka() {
        this.folders();
        poruke = this.messages();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Metoda dohvaća poruke za odabranu mapu i refresha ekran
     *
     */
    public String odaberiMapu() {
        poruke = this.messages();
        return null;
    }

    /**
     * Metoda za brisanje email poruke
     *
     */
    public void obrisiPoruku() {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);

            Message[] messages = null;
            messages = folder.getMessages();

            for (int i = messages.length - 1; i >= 0; i--) {
                MimeMessage m = (MimeMessage) messages[i];
                if (m.getHeader("Message-ID")[0].equals(odabranaPoruka.getId())) {
                    m.setFlag(Flags.Flag.DELETED, true);
                }
            }
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda pretražuje mape korisnika i pohranjuje ih u HashMap mape
     *
     */
    public void folders() {
        try {
            java.util.Properties properties = System.getProperties();
            System.out.println("server: " + server);
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
     * Metoda preuzima email poruka za određenog korisnika iz odabrane mape.
     *
     * @return poruke
     */
    public List<Poruka> messages() {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);

            int brojPoruka = folder.getMessageCount();
            if (brojPoruka == 0) {
                poruke = new ArrayList<>();
                return poruke;
            }
            poruke = new ArrayList<>();
            Message[] messages = null;
            messages = folder.getMessages();

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
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return poruke;
    }

    public String pregled() {
        if (odabranaPoruka != null) {
            poruka = odabranaPoruka;
            prikazForme = true;
        }
        return null;
    }

    public String brisanje() {
        obrisano = false;
        if (odabranaPoruka != null) {
            this.obrisiPoruku();
            obrisano = true;
        }
        return null;
    }

    public String zatvoriPregled() {
        prikazForme = false;
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public List<Poruka> getPoruke() {
        poruke = this.messages();
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public Map<String, String> getMape() {
        return mape;
    }

    public void setMape(Map<String, String> mape) {
        this.mape = mape;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public int getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(int stranicenje) {
        this.stranicenje = stranicenje;
    }

    public Poruka getOdabranaPoruka() {
        return odabranaPoruka;
    }

    public void setOdabranaPoruka(Poruka odabranaPoruka) {
        this.odabranaPoruka = odabranaPoruka;
    }

    public boolean isPrikazForme() {
        return prikazForme;
    }

    public void setPrikazForme(boolean prikazForme) {
        this.prikazForme = prikazForme;
    }

    public boolean isObrisano() {
        return obrisano;
    }

    public void setObrisano(boolean obrisano) {
        this.obrisano = obrisano;
    }

}
