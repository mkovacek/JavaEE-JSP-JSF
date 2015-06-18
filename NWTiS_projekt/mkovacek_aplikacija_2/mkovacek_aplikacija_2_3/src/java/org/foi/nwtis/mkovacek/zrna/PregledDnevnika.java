/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Dnevnik;
import org.foi.nwtis.mkovacek.ejb.sb.DnevnikFacade;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * ManagedBean za dnevnika
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    private HttpSession session;
    private FacesContext context;
    private String korisnik;
    private String zahtjev;
    private String ipAdresa;
    private String trajanje;
    private String status;
    private Date dateTimeOd;
    private Date dateTimeDo;
    private List<Dnevnik> dnevnik;
    private boolean nemaRezultata = false;
    private boolean rezultati = false;
    private int stranicenje = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranicenje"));

    /**
     * Creates a new instance of Dnevnik
     */
    public PregledDnevnika() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
    }

    /**
     * Metoda za filtrirano dohvaćanje podataka iz baze podataka. Ukoliko nisu
     * unesni podaci za filtriranje dohvaćaju se svi podaci iz tbl dnevnik, a
     * ako jesu dohvaćaju se podaci (ako takvi postoje) prema unesnim
     * kriterijima.
     *
     */
    public String filtriraj() {
        if (korisnik.equals("") && ipAdresa.equals("") && zahtjev.equals("") && trajanje.equals("") && status.equals("") && dateTimeOd == null && dateTimeDo == null) {
            dnevnik = dnevnikFacade.findAll();
            Collections.reverse(dnevnik);
            if (!dnevnik.isEmpty()) {
                this.rezultati = true;
                this.nemaRezultata = false;
            } else {
                this.nemaRezultata = true;
                this.rezultati = false;
            }
        } else {
            SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String datumOd = "";
            String datumDo = "";
            if (dateTimeOd != null && dateTimeDo != null) {
                datumOd = tmp.format(dateTimeOd);
                datumDo = tmp.format(dateTimeDo);
            }
            dnevnik = dnevnikFacade.filter(korisnik, ipAdresa, trajanje, status, datumOd, datumDo, zahtjev);
            if (!dnevnik.isEmpty()) {
                this.rezultati = true;
                this.nemaRezultata = false;
            } else {
                this.nemaRezultata = true;
                this.rezultati = false;
            }
        }
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public DnevnikFacade getDnevnikFacade() {
        return dnevnikFacade;
    }

    public void setDnevnikFacade(DnevnikFacade dnevnikFacade) {
        this.dnevnikFacade = dnevnikFacade;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateTimeOd() {
        return dateTimeOd;
    }

    public void setDateTimeOd(Date dateTimeOd) {
        this.dateTimeOd = dateTimeOd;
    }

    public Date getDateTimeDo() {
        return dateTimeDo;
    }

    public void setDateTimeDo(Date dateTimeDo) {
        this.dateTimeDo = dateTimeDo;
    }

    public boolean isNemaRezultata() {
        return nemaRezultata;
    }

    public void setNemaRezultata(boolean nemaRezultata) {
        this.nemaRezultata = nemaRezultata;
    }

    public boolean isRezultati() {
        return rezultati;
    }

    public void setRezultati(boolean rezultati) {
        this.rezultati = rezultati;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }

    public int getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(int stranicenje) {
        this.stranicenje = stranicenje;
    }

}
