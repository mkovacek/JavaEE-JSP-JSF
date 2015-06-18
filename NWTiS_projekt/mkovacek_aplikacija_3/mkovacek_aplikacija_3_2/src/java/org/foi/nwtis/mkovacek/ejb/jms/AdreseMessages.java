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
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.AdreseOM;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator2;
import org.foi.nwtis.mkovacek.socket.klijent.SlanjeZahtjeva;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.mkovacek.websocket.AdresaEndpoint;

/**
 *
 * @author Matija
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/NWTiS_mkovacek_2"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class AdreseMessages implements MessageListener {

    public AdreseMessages() {
    }

    @Override

    /**
     * Metoda osluškuje JMS poruke (adrese), web socket obavijštava korisnika na
     * ekranu da je zaprimljena nova poruka, slanje zahtjeva socket serveru za
     * provjeru i preuzimanje meteo pdoataka
     *
     */
    public void onMessage(Message message) {
        ObjectMessage objMsg = (ObjectMessage) message;
        try {
            AdreseOM a = (AdreseOM) objMsg.getObject();
            AdresaEndpoint.obavijestiPromjenu("Novi JMS (adresa)!");
            Serijalizator2.getInstance().getListaAdresa().add(a);
            String zahtjev = "USER " + a.getKorisnik() + "; PASSWD " + a.getLozinka() + "; TEST " + a.getAdresa() + ";";
            SlanjeZahtjeva sz = new SlanjeZahtjeva(zahtjev);
            String odgovor = sz.posaljiZahtjev();
            if (odgovor.equals("ERR 42;")) {
                System.out.println("Adresa nije u evidenciji preuzimanja");
                String zahtjevAdd = "USER " + a.getKorisnik() + "; PASSWD " + a.getLozinka() + "; ADD " + a.getAdresa() + ";";
                sz.setZahtjev(zahtjevAdd);
                String odgovorAdd = sz.posaljiZahtjev();
                if (odgovorAdd.equals("OK 10;")) {
                    System.out.println("Adresa je stavljena u evidenciju preuzimanja");
                } else if (odgovorAdd.equals("ERR 40;")) {
                    System.out.println("Korisnik nema dovoljno sredstava za ADD adresa");
                } else if (odgovorAdd.equals("ERR 41;")) {
                    System.out.println("Adresa vec je u evidenciji preuzimanja");
                }
            } else if (odgovor.equals("ERR 40;")) {
                System.out.println("Korisnik nema dovoljno sredstava za TEST adresa");
            } else if (odgovor.equals("OK 10;")) {
                System.out.println("Adresa je u evidenciji preuzimanja");

            }

        } catch (JMSException ex) {
            Logger.getLogger(AdreseMessages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
