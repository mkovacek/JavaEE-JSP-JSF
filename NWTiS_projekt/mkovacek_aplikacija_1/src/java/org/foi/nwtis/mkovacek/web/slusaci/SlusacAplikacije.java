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
import org.foi.nwtis.mkovacek.socketServer.ServerSustava;


/**
 * Web application lifecycle listener.
 *
 * @author mkovacek
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private MeteoPodaciPreuzimanje mpp;
    private ServerSustava ss;
    public static BP_Konfiguracija bpk;
    public static Konfiguracija konfig;
    public static Konfiguracija cjenik;

    /**
     * Overridde-ana thread contextInitialized() metoda. Metoda preuzima
     * konfiguraciju, postavlja kontekst,pokrece dretvu za preuzimanje meteo
     * podataka, te pokrece server sustava za obradu zahtjeva.
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String direktorij = sce.getServletContext().
                getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + sce.getServletContext().
                getInitParameter("konfiguracija");
        String datotekaCjenik = direktorij + sce.getServletContext().
                getInitParameter("cjenik");
        bpk = new BP_Konfiguracija(datoteka);
        sce.getServletContext().setAttribute("BP_Konfig", bpk);

        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            sce.getServletContext().setAttribute("Konfig", konfig);
            cjenik=KonfiguracijaApstraktna.preuzmiKonfiguraciju(datotekaCjenik);
            sce.getServletContext().setAttribute("Cjenik", cjenik);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        mpp = new MeteoPodaciPreuzimanje();
        mpp.start();
        
        ss=new ServerSustava();
        ss.start();

    }

    /**
     * Overridde-ana thread contextDestroyed() metoda. Metoda briše kontekst i
     * zaustavlja dretvu za preuzimanje meteo podataka, te zaustavlja servera
     * sustava za obradu zahtjeva
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("BP_Konfig");
        sce.getServletContext().removeAttribute("Konfig");
        sce.getServletContext().removeAttribute("Cjenik");
        if (mpp != null && mpp.isAlive()) {
            mpp.interrupt();
        }
        
        if (ss != null && ss.isAlive()) {
            ServerSustava.setStop(true);
            ss.interrupt();
        }
    }
}
