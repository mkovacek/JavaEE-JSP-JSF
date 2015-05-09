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

/**
 * Web application lifecycle listener.
 *
 * @author mkovacek
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ObradaPoruka op;
    public static Konfiguracija konfig;

    /**
     * Overridde-ana thread contextInitialized() metoda. Metoda preuzima
     * konfiguraciju, postavlja kontekst i pokrece dretvu za obradu poruka
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String direktorij = sce.getServletContext().getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + sce.getServletContext().getInitParameter("konfiguracija");
        BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);
        sce.getServletContext().setAttribute("BP_Konfig", bpk);
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            sce.getServletContext().setAttribute("Konfig", konfig);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        op = new ObradaPoruka();
        op.setKonfigDB(bpk);
        op.setKonfig(konfig);
        op.setDirektorij(direktorij);
        op.start();

    }

    /**
     * Overridde-ana thread contextDestroyed() metoda. Metoda briše kontekst i
     * zaustavlja dretvu obradu poruka
     * @param sce
     */
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("BP_Konfig");
        sce.getServletContext().removeAttribute("Konfig");

        if (op != null && !op.isInterrupted()) {
            op.interrupt();
        }
    }
}
