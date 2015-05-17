/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.slusaci;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author mkovacek
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private MeteoPodaciPreuzimanje mpp;
    private ServletContextEvent kontekst;
    public static BP_Konfiguracija bpk;
    public static Konfiguracija konfig;

    /**
     * Overridde-ana thread contextInitialized() metoda. Metoda preuzima
     * konfiguraciju, postavlja kontekst i pokrece dretvu za preuzimanje meteo
     * podataka
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        kontekst = sce;
        String direktorij = sce.getServletContext().
                getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + sce.getServletContext().
                getInitParameter("konfiguracija");
        bpk = new BP_Konfiguracija(datoteka);
        sce.getServletContext().setAttribute("BP_Konfig", bpk);

        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList ak = new ArrayList();
        sce.getServletContext().setAttribute("AktivniKorisnici", ak);

        mpp = new MeteoPodaciPreuzimanje();
        mpp.start();
    }

    /**
     * Overridde-ana thread contextDestroyed() metoda. Metoda briše kontekst i
     * zaustavlja dretvu za preuzimanje meteo podataka
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("BP_Konfig");
        sce.getServletContext().removeAttribute("AktivniKorisnici");

        if (mpp != null && !mpp.isInterrupted()) {
            mpp.interrupt();
        }
    }
    /**
     * Getter za dohvaćanje konteksta
     *
     */
    public ServletContextEvent getKontekst() {
        return kontekst;
    }

}
