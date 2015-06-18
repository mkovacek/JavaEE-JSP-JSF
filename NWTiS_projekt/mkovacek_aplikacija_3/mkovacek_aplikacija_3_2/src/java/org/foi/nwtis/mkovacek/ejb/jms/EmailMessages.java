/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.jms;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.EmailOM;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.Korisnik;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator2;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.mkovacek.websocket.AdresaEndpoint;

/**
 *
 * @author Matija
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/NWTiS_mkovacek_1"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class EmailMessages implements MessageListener {

    public EmailMessages() {
    }
    /**
     * Metoda osluškuje JMS poruke (email), web socket obavijštava korisnika na
     * ekranu da je zaprimljena nova poruka, slanje zahtjeva socket serveru za
     * provjeru i preuzimanje meteo pdoataka
     *
     */
    @Override
    public void onMessage(Message message) {
        ObjectMessage objMsg = (ObjectMessage) message;
        try {
            EmailOM e = (EmailOM) objMsg.getObject();
            AdresaEndpoint.obavijestiPromjenu("Novi JMS (email)!");
            if (Serijalizator2.getInstance().getListaEmail().isEmpty()) {
                System.out.println("onMessage lista email je prazna");
                Serijalizator2.getInstance().deserijalizatorEmail("C:\\NWTiS_projekt\\mkovacek_aplikacija_3\\mkovacek_aplikacija_3\\dist\\gfdeploy\\mkovacek_aplikacija_3\\mkovacek_aplikacija_3_2_war\\WEB-INF\\NWTiS_evidencijaEmail.bin");
            }
            Serijalizator2.getInstance().getListaEmail().add(e);
            /*System.out.println("Zaprimljena jms poruka email : ");
            System.out.println(e.getVrijemePocetak());
            System.out.println(e.getVrijemeKraj());
            System.out.println(e.getBrojProcitanihPoruka());
            System.out.println(e.getBrojNWTiSporuka());
            System.out.println("Dodani korisnici: ");
            for (Korisnik k : e.getDodaniKorisnici()) {
                System.out.println(k.getKorIme());
            }
            System.out.println("Ostali korisnici: ");
            for (Korisnik k : e.getOstaliKorisnici()) {
                System.out.println(k.getKorIme());
            }*/
        } catch (JMSException ex) {
            Logger.getLogger(EmailMessages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
