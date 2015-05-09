/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Managed bean SlanjePoruke, služi za slanje poruke
 *
 * @author mkovacek
 */
@ManagedBean
@RequestScoped
public class SlanjePoruke {

    private String predmet;
    private String poruka;
    private String prima;
    private String salje;
    private String format;
    private static final Map<String, String> formati;
    private boolean poslano;
    private boolean nijePoslano;

    static {
        formati = new HashMap<>();
        formati.put("text/plain", "text/plain");
        formati.put("text/html", "text/html");
    }

    /**
     * Konstruktor
     */
    public SlanjePoruke() {
    }

    /**
     * Metoda za slanje email poruke
     *
     * @return "OK" kada je uspješno poslana poruka
     * @return "ERROR" kada nije poruka poslana
     */
    public String saljiPoruku() {
        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        String smtpHost = ep.getServer();
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
            if (format.equals("text/html")) {
                message.setContent(poruka, "text/html; charset=utf-8");
            } else {
                message.setText(poruka);
            }
            Transport.send(message);
            poslano = true;
            return "OK";

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        nijePoslano = true;
        return "ERROR";
    }

    /**
     * Metoda vraca predmet poruke
     *
     * @return (String) predmet
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     * Metoda postavlja predmet poruke.
     *
     * @param predmet - predmet
     */
    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    /**
     * Metoda vraca poruku.
     *
     * @return (String) poruka
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     * Metoda postavlja poruku.
     *
     * @param poruka - poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     * Metoda vraca primatelja.
     *
     * @return (String) prima
     */
    public String getPrima() {
        return prima;
    }

    /**
     * Metoda postavlja primatelja.
     *
     * @param prima - primatelj
     */
    public void setPrima(String prima) {
        this.prima = prima;
    }

    /**
     * Metoda vraca pošiljatelj.
     *
     * @return (String) salje
     */
    public String getSalje() {
        return salje;
    }

    /**
     * Metoda postavlja pošiljatelja.
     *
     * @param salje - pošiljatelj
     */
    public void setSalje(String salje) {
        this.salje = salje;
    }

    /**
     * Metoda vraca format poruke.
     *
     * @return (String) format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Metoda postavlja format poruke.
     *
     * @param format - format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Metoda vraca mapu formata poruke.
     *
     * @return (Map<String, String>) formati
     */
    public  Map<String, String> getFormati() {
        return formati;
    }

    /**
     * Metoda vraca da je poslana poruka.
     *
     * @return (boolean) true
     */
    public boolean isPoslano() {
        return poslano;
    }

    /**
     * Metoda postavlja da je poslana poruka.
     *
     * @param poslano - true
     */
    public void setPoslano(boolean poslano) {
        this.poslano = poslano;
    }

    /**
     * Metoda vraca da nije poslana poruka
     *
     * @return (boolean) true
     */
    public boolean isNijePoslano() {
        return nijePoslano;
    }

    /**
     * Metoda postavlja da nije poslana poruka.
     *
     * @param nijePoslano - true
     */
    public void setNijePoslano(boolean nijePoslano) {
        this.nijePoslano = nijePoslano;
    }

}
