/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.kontrole;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.rest.klijenti.GMKlijent;
import org.foi.nwtis.mkovacek.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Klasa za obradu korisničkih zahtjeva
 *
 * @author mkovacek
 */
public class Kontroler extends HttpServlet {

    private Adresa adresa;
    private Adresa adresaMeteo;

    /**
     * Obrađuju se zahtjevi za dodavanje adrese, pohranjivanje adrese u bazu
     * podataka, te dohvaćanje meteo podatka za unesenu adresu Processes
     * requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String zahtjev = request.getServletPath();
        String odrediste = null;

        switch (zahtjev) {
            case "/DodajAdresu":
                String unesenaAdresa = request.getParameter("adresa");
                if (unesenaAdresa != null || !unesenaAdresa.equals("")) {
                    System.out.println("Adresa: " + unesenaAdresa);
                    Lokacija lokacija = new Lokacija();
                    GMKlijent gmk = new GMKlijent();
                    lokacija = gmk.getGeoLocation(unesenaAdresa);
                    if (lokacija != null) {
                        adresa = new Adresa(unesenaAdresa, lokacija);
                        adresaMeteo = adresa;
                        request.setAttribute("geoPodaci", adresa);
                        request.setAttribute("geoPrikaz", true);
                    }
                }
                odrediste = "/index.jsp";
                break;

            case "/SpremiAdresu":
                BazaPodataka bp = new BazaPodataka();
                if (adresa != null) {
                    bp.spremiGeoPodatke(adresa);
                    adresaMeteo = adresa;
                }
                adresa = null;
                odrediste = "/index.jsp";
                break;

            case "/PreuzmiMeteoPodatke":
                if (adresaMeteo != null) {
                    String apiKey = SlusacAplikacije.konfig.dajPostavku("owmApiKey");
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    MeteoPodaci mp = owmk.getRealTimeWeather(adresaMeteo.getGeoloc().getLatitude(),
                            adresaMeteo.getGeoloc().getLongitude());
                    request.setAttribute("meteoAdresa", adresaMeteo.getAdresa());
                    request.setAttribute("meteoPodaci", mp);
                    request.setAttribute("meteoPrikaz", true);
                }
                adresaMeteo = null;
                odrediste = "/index.jsp";
                break;
        }
        if (odrediste == null) {
            throw new ServletException("Zahtjev: '" + zahtjev + "' nije poznat!");
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher(odrediste);
        rd.forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
