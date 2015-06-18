/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator;
import org.foi.nwtis.mkovacek.serijalizator.Serijalizator2;

/**
 * Web application lifecycle listener.
 *
 * @author mkovacek
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    public static BP_Konfiguracija bpk;
    public static Konfiguracija konfig;
    public static String direktorij;

    /**
     * Overridde-ana thread contextInitialized() metoda. Metoda preuzima
     * konfiguraciju, postavlja kontekst,pokrece deserijalizaciju podataka
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        direktorij = sce.getServletContext().
                getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + sce.getServletContext().
                getInitParameter("konfiguracija");
        bpk = new BP_Konfiguracija(datoteka);
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*System.out.println("test: " + direktorij + konfig.dajPostavku("evidDatotekaEmail"));
        System.out.println("test2: " + direktorij + konfig.dajPostavku("evidDatotekaAdrese"));*/

        if (Serijalizator2.getInstance().getListaEmail().isEmpty()) {
            System.out.println("kontekst lista email je prazna");
            Serijalizator.deserijalizatorEmail(direktorij + konfig.dajPostavku("evidDatotekaEmail"));
        }
        if (Serijalizator2.getInstance().getListaAdresa().isEmpty()) {
            System.out.println("kontekst lista adresa je prazna");
            Serijalizator.deserijalizatorAdresa(direktorij + konfig.dajPostavku("evidDatotekaAdrese"));
        }

    }

    /**
     * Overridde-ana thread contextDestroyed() metoda. Metoda serijalizira
     * podatke
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Serijalizator2.getInstance().serijalizatorEmail(direktorij + konfig.dajPostavku("evidDatotekaEmail"), Serijalizator2.getInstance().getListaEmail());
        Serijalizator2.getInstance().serijalizatorAdresa(direktorij + konfig.dajPostavku("evidDatotekaAdrese"), Serijalizator2.getInstance().getListaAdresa());
        System.out.println("context destroyed!");
    }

}
