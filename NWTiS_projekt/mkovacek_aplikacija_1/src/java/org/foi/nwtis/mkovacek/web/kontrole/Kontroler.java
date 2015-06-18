/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.kontrole;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.web.podaci.Dnevnik;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.podaci.Transakcije;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Matija
 */
public class Kontroler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        String zahtjev = request.getServletPath();
        String odrediste = null;
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = new Korisnik();

        switch (zahtjev) {
            case "/Kontroler":
                odrediste = "/jsp/index.jsp";
                break;
            case "/PrijavaKorisnika":
                odrediste = "/jsp/login.jsp";
                break;
            case "/OdjavaKorisnika":
                HttpSession hsess = request.getSession(false);
                if (hsess != null) {
                    hsess.removeAttribute("user");
                    hsess.invalidate();
                }
                odrediste = "/jsp/index.jsp";
                break;
            case "/ProvjeraKorisnika":
                String korisnik = request.getParameter("korisnik");
                String lozinka = request.getParameter("lozinka");
                if (korisnik == null || lozinka == null || korisnik.trim().equals("") || lozinka.trim().equals("")) {
                    odrediste = "/jsp/login.jsp";
                } else {
                    user = bp.autentifikacija(korisnik, lozinka);
                    if (user != null) {
                        HttpSession hs = request.getSession(true);
                        hs.setAttribute("user", user);
                        odrediste = "/privatno/home.jsp";
                    } else {
                        odrediste = "/jsp/login.jsp";
                    }
                }
                break;
            case "/Home":
                odrediste = "/privatno/home.jsp";
                break;
            case "/PregledFinancija":
                HttpSession sesija = request.getSession();
                user = (Korisnik) sesija.getAttribute("user");
                if (user != null) {
                    user = bp.dajFinancije(user.getKorIme());
                    request.setAttribute("racun", user);
                }
                odrediste = "/privatno/financije.jsp";
                break;
            case "/DodavanjeSredstava":
                HttpSession s1 = request.getSession();
                user = (Korisnik) s1.getAttribute("user");
                String kor = user.getKorIme();
                String iznos = request.getParameter("iznos");
                if (iznos != null || !iznos.equals("")) {
                    user = bp.dajFinancije(kor);
                    double izn = Double.parseDouble(iznos);
                    bp.spremiTranskaciju(kor, "Financije - dodavanje sredstava", user.getRacun(), 0, user.getRacun() + izn, "OK");
                    bp.azurirajSredstva(kor, user.getRacun() + izn);
                    odrediste = "/PregledFinancija";
                } else {
                    odrediste = "/PregledFinancija";
                }
                break;
            case "/AzuriranjeSredstava":
                HttpSession s2 = request.getSession();
                user = (Korisnik) s2.getAttribute("user");
                String korIme = user.getKorIme();
                String noviIznos = request.getParameter("noviIznos");
                if (noviIznos != null || !noviIznos.equals("")) {
                    user = bp.dajFinancije(korIme);
                    double izn = Double.parseDouble(noviIznos);
                    bp.spremiTranskaciju(korIme, "Financije - azuriranje sredstava", user.getRacun(), 0, izn, "OK");
                    bp.azurirajSredstva(korIme, izn);
                    odrediste = "/PregledFinancija";
                } else {
                    odrediste = "/PregledFinancija";
                }
                break;
            case "/PregledTransakcija":
                HttpSession s3 = request.getSession();
                user = (Korisnik) s3.getAttribute("user");
                if (user != null) {
                    List<Transakcije> trans = bp.dohvatiKorisnikoveTransakcije(user.getKorIme());
                    s3.setAttribute("transakcije", trans);
                    String stranicenje = SlusacAplikacije.konfig.dajPostavku("stranicenje");
                    s3.setAttribute("stranicenje", stranicenje);
                }
                odrediste = "/privatno/transakcije.jsp";
                break;
            case "/PregledDnevnika":
                HttpSession s4 = request.getSession();
                user = (Korisnik) s4.getAttribute("user");
                if (user != null) {
                    List<Dnevnik> dnevnik = bp.dohvatiDnevnik();
                    s4.setAttribute("dnevnik", dnevnik);
                    String stranicenje = SlusacAplikacije.konfig.dajPostavku("stranicenje");
                    s4.setAttribute("stranicenje", stranicenje);
                }
                odrediste = "/privatno/dnevnik.jsp";
                break;
            case "/FiltriranjeDnevnika":
                HttpSession s5 = request.getSession();
                user = (Korisnik) s5.getAttribute("user");
                if (user != null) {
                    String zht = request.getParameter("zahtjev");
                    String odDatum = request.getParameter("odDatum");
                    String doDatum = request.getParameter("doDatum");
                    String ipAdresa = request.getParameter("ipAdresa");
                    if (zht.equals("") && odDatum.equals("") && doDatum.equals("") && ipAdresa.equals("")) {
                        odrediste = "/PregledDnevnika";
                    } else {
                        if (!odDatum.equals("") && !doDatum.equals("")) {
                            SimpleDateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat dfIn = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
                            try {
                                odDatum = dfOut.format(dfIn.parse(odDatum));
                                doDatum = dfOut.format(dfIn.parse(doDatum));
                            } catch (ParseException ex) {
                                Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        List<Dnevnik> dnevnik = bp.filtrirajDnevnik(zht, ipAdresa, odDatum, doDatum);
                        s5.setAttribute("dnevnik", dnevnik);
                        String stranicenje = SlusacAplikacije.konfig.dajPostavku("stranicenje");
                        s5.setAttribute("stranicenje", stranicenje);
                        odrediste = "/privatno/dnevnik.jsp";
                    }
                }
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
