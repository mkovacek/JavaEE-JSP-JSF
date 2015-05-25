/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.mkovacek.ejb.eb.Dnevnik;
import org.foi.nwtis.mkovacek.ejb.sb.DnevnikFacade;

/**
 * JSF ManageBean PregledDnevnika
 *
 * @author mkovacek
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika implements Serializable {
    @EJB
    private DnevnikFacade dnevnikFacade;
    private String korisnik;
    private String ipAdresa;
    private String trajanje;
    private String status;
    private Date dateTimeOd;
    private Date dateTimeDo;
    private List<Dnevnik> dnevnik;
    private boolean nemaRezultata = false;
    private boolean rezultati = false;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    /**
     * Metoda za filtrirano dohvaćanje podataka iz baze podataka. Ukoliko nisu
     * unesni podaci za filtriranje dohvaćaju se svi podaci iz tbl dnevnik, a
     * ako jesu dohvaćaju se podaci (ako takvi postoje) prema unesnim
     * kriterijima.
     *
     */
    public String filtriraj() {
        int pocetak = (int) System.currentTimeMillis();
        if (korisnik.equals("") && ipAdresa.equals("") && trajanje.equals("") && status.equals("") && dateTimeOd == null && dateTimeDo == null) {
            dnevnik = dnevnikFacade.findAll();
            if (!dnevnik.isEmpty()) {
                this.rezultati = true;
                this.nemaRezultata = false;
                this.log((int) System.currentTimeMillis() - pocetak, 200);
            } else {
                this.nemaRezultata = true;
                this.rezultati = false;
                this.log((int) System.currentTimeMillis() - pocetak, 400);
            }
        } else {
            SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String datumOd = "";
            String datumDo = "";
            if (dateTimeOd != null && dateTimeDo != null) {
                datumOd = tmp.format(dateTimeOd);
                datumDo = tmp.format(dateTimeDo);
            }
            dnevnik = dnevnikFacade.filter(korisnik, ipAdresa, trajanje, status, datumOd, datumDo);
            if (!dnevnik.isEmpty()) {
                this.rezultati = true;
                this.nemaRezultata = false;
                this.log((int) System.currentTimeMillis() - pocetak, 200);
            } else {
                this.nemaRezultata = true;
                this.rezultati = false;
                this.log((int) System.currentTimeMillis() - pocetak, 400);
            }
        }
        return null;
    }

    /**
     * Metoda za zapis akcije u bazu podataka (u tbl Dnevnik)
     *
     * @param trajanje akcije
     * @param status akcije
     */
    public void log(int trajanje, int status) {
        HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik("mkovacek");
        dnevnik.setUrl(request.getRequestURI());
        dnevnik.setIpadresa(request.getRemoteAddr());
        dnevnik.setVrijeme(new Date());
        dnevnik.setTrajanje(trajanje);
        dnevnik.setStatus(status);
        dnevnikFacade.create(dnevnik);
    }

    /**
     * Geteri i seteri
     *
     *
     */
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

    public DnevnikFacade getDnevnikFacade() {
        return dnevnikFacade;
    }

    public void setDnevnikFacade(DnevnikFacade dnevnikFacade) {
        this.dnevnikFacade = dnevnikFacade;
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

    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
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

}
