/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.jms.AdreseMessages;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.AdreseOM;
import org.foi.nwtis.mkovacek.ws.server.Adresa;
import org.foi.nwtis.mkovacek.ws.klijenti.GeoMeteoWSKlijent;
import org.foi.nwtis.mkovacek.ws.server.GeoMeteoWsResponse;
import org.foi.nwtis.mkovacek.ws.server.MeteoPodaci;
import org.foi.nwtis.mkovacek.ws.server.MeteoPrognoza;
import org.foi.nwtis.mkovacek.ws.server.VremenskeStanice;

/**
 * ManagedBean za prikaz adresa i meteo podataka
 *
 * @author Matija
 */
@ManagedBean
@SessionScoped
public class MeteoAdrese implements Serializable {

    private HttpSession session;
    private FacesContext context;
    public static String response;
    private String novaAdresa;
    private boolean dodanaAdresa = false;
    private boolean nedovoljnoSredstava = false;
    private boolean adrPostoji = false;
    private boolean nemaAdresa = false;
    private boolean nedovoljnoSredstavaMeteo = false;
    private boolean nemaAdresaMeteo = false;
    private List<Adresa> sveAdrese;
    private boolean prikazSvihAdresa = false;
    private String odabranaAdresa;
    private List<Adresa> sveKorisnikoveAdrese;
    private boolean prikazKorisnikovihAdresa = false;
    private String odabranaKorisnikovaAdresa;
    private boolean nijeOdabranaAdresa = false;
    private boolean prikazVazecihMeteoPodataka = false;
    private boolean prikazVazecihMeteoPodatakaKor = false;
    private MeteoPodaci mp;
    private MeteoPodaci mpKor;
    private int brojAdresaRangListe;
    private boolean prikazRangListeAdresa = false;
    private List<Adresa> rangListaAdresa;
    private int brojMeteoPodataka;
    private List<MeteoPodaci> nMeteoPodataka;
    private boolean prikaznMeteoPodataka = false;
    private Date dateTimeOd;
    private Date dateTimeDo;
    private List<MeteoPodaci> meteoPodataciOdDo;
    private boolean prikazMeteoPodatakaOdDo = false;
    private boolean nemaMeteoPodataka = false;
    private int brojDanaMeteoPrognoze;
    private List<MeteoPrognoza> meteoPrognoza;
    private boolean prikazMeteoPrognoze = false;
    private int brojSatiPrognoze;
    private List<MeteoPodaci> meteoPrognozaSati;
    private boolean prikazMeteoPrognozeSati = false;
    private int brojStanica;
    private List<VremenskeStanice> meteoStanice;
    private boolean prikazMeteoStanica = false;
    private String markerData;

    /**
     * Creates a new instance of MeteoAdrese
     */
    public MeteoAdrese() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        session.setAttribute("url", servletRequest.getRequestURI());
        //System.out.println("url: "+(String)session.getAttribute("url"));
    }

    /**
     * Dodavanje nove adrese, slanje JMS
     *
     */
    public String dodajAdresu() {
        dodanaAdresa = false;
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");

        if (!novaAdresa.trim().equals("")) {
            AdreseMessages am = new AdreseMessages();
            AdreseOM adresa = new AdreseOM(korisnik.getKorime(), korisnik.getLozinka(), novaAdresa);
            try {
                am.sendJMSMessageToNWTiS_mkovacek_2(adresa);
            } catch (JMSException ex) {
                Logger.getLogger(MeteoAdrese.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(MeteoAdrese.class.getName()).log(Level.SEVERE, null, ex);
            }
            dodanaAdresa = true;
        }
        return null;
    }

    /**
     * Prikaz svih adresa
     *
     */
    public String dajSveAdrese() {
        this.preuzmiSveAdrese();
        return null;
    }

    /**
     * Prikaz korisnikove adrese
     *
     */
    public String dajKorisnikoveAdrese() {
        this.preuzmiKorisnikoveAdrese();
        return null;
    }

    /**
     * Prikaz Zadnji meto podaci za adresu
     *
     */
    public String dajZadnjeMeteoPodatke() {
        if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
            return null;
        } else {
            this.preuzmiZadnjeMeteoPodatke(odabranaAdresa, "sve");
        }
        return null;
    }

    /**
     * Prikaz Zadnji meto podaci za korisnikovu adresu
     *
     */
    public String dajZadnjeMeteoPodatke2() {
        if (odabranaKorisnikovaAdresa == null) {
            nijeOdabranaAdresa = true;
            return null;
        } else {
            this.preuzmiZadnjeMeteoPodatke(odabranaKorisnikovaAdresa, "kor");
        }
        return null;
    }

    /**
     * Prikaz Rang lista adresa
     *
     */
    public String dajRangListuAdresa() {
        if (brojAdresaRangListe != 0) {
            this.preuzmiRangListuAdresa();
        }
        return null;
    }

    /**
     * Prikaz Zadnji N meto podataka
     *
     */
    public String dajNMeteoPodatka() {
        if (brojMeteoPodataka != 0 && odabranaAdresa != null) {
            this.preuzmiNMeteoPodataka();
        } else if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
        }
        return null;
    }

    /**
     * Prikaz Meteo podaci u vremenskom intervalu
     *
     */
    public String dajMeteoPodatkaOdDo() {
        if (dateTimeDo != null && dateTimeOd != null && odabranaAdresa != null) {
            this.preuzmiMeteoPodatkeOdDo();
        } else if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
        }
        return null;
    }

    /**
     * Prikaz Meteo prognoza
     *
     */
    public String dajMeteoPrognozu() {
        if (odabranaAdresa != null && brojDanaMeteoPrognoze != 0) {
            this.preuzmiMeteoPrognozu();
        } else if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
        }
        return null;
    }

    /**
     * Prikaz Meteo prognoza u satima
     *
     */
    public String dajMeteoPrognozuSati() {
        if (odabranaAdresa != null && brojSatiPrognoze != 0) {
            this.preuzmiMeteoPrognozuSati();
        } else if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
        }
        return null;
    }

    /**
     * Prikaz Meteo stanice
     *
     */
    public String dajMeteoStanice() {
        if (odabranaAdresa != null && brojStanica != 0) {
            this.preuzmiMeteoStanice();
        } else if (odabranaAdresa == null) {
            nijeOdabranaAdresa = true;
        }
        return null;
    }

    /**
     * Preuzimanje svih adresa
     *
     */
    public void preuzmiSveAdrese() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajSveAdrese(korisnik.getKorime(), korisnik.getLozinka());
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<Adresa> preuzeteAdrese = wsResponse.getAdrese();
            prikazSvihAdresa = true;
            this.setSveAdrese(preuzeteAdrese);
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nema")) {
            nemaAdresa = true;
        }

    }

    /**
     * Preuzimanje korisnikovih adresa
     *
     */
    public void preuzmiKorisnikoveAdrese() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajSveKorisnikoveAdrese(korisnik.getKorime(), korisnik.getLozinka());
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<Adresa> preuzeteAdrese = wsResponse.getAdrese();
            prikazKorisnikovihAdresa = true;
            this.setSveKorisnikoveAdrese(preuzeteAdrese);
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nema")) {
            nemaAdresa = true;
        }

    }

    /**
     * Preuzimanje zadnjih meteo podataka
     *
     */
    public void preuzmiZadnjeMeteoPodatke(String adresa, String render) {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajVazeceMeteoPodatkeZaAdresu(korisnik.getKorime(), korisnik.getLozinka(), adresa);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            MeteoPodaci mpod = wsResponse.getMp();
            if (render.equals("sve")) {
                this.setMp(mpod);
                prikazVazecihMeteoPodataka = true;
            } else {
                this.setMpKor(mpod);
                prikazVazecihMeteoPodatakaKor = true;
            }
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nepostojeća")) {
            nemaAdresa = true;
        }
    }

    /**
     * Preuzimanje rang liste adresa
     *
     */
    public void preuzmiRangListuAdresa() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajRangListuAdresaNajviseMeteoPodataka(korisnik.getKorime(), korisnik.getLozinka(), brojAdresaRangListe);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<Adresa> adrese = new ArrayList<>();
            adrese = wsResponse.getAdrese();
            this.setRangListaAdresa(adrese);
            prikazRangListeAdresa = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        }
    }

    /**
     * Preuzimanje N meteo podataka
     *
     */
    public void preuzmiNMeteoPodataka() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajNzadnjihMeteoPodatakaZaAdresu(korisnik.getKorime(), korisnik.getLozinka(), odabranaAdresa, brojMeteoPodataka);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<MeteoPodaci> mp = new ArrayList<>();
            mp = wsResponse.getMpList();
            this.setnMeteoPodataka(mp);
            System.out.println("ok");
            prikaznMeteoPodataka = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        }
    }

    /**
     * Preuzimanje meteo podataka u vremenskom intervalu
     *
     */
    public void preuzmiMeteoPodatkeOdDo() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;
        nemaMeteoPodataka = false;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datumO = df.format(dateTimeOd);
        String datumD = df.format(dateTimeDo);

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajMeteoPodatkeZaAdresuOdDo(korisnik.getKorime(), korisnik.getLozinka(), odabranaAdresa, datumO, datumD);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<MeteoPodaci> mp = new ArrayList<>();
            mp = wsResponse.getMpList();
            this.setMeteoPodataciOdDo(mp);
            prikazMeteoPodatakaOdDo = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nepostojeća")) {
            nemaMeteoPodataka = true;
        }
    }

    /**
     * Preuzimanje meteo prognoze
     *
     */
    public void preuzmiMeteoPrognozu() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;
        nemaMeteoPodataka = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajMeteoPrognozuZaAdresu(korisnik.getKorime(), korisnik.getLozinka(), odabranaAdresa, brojDanaMeteoPrognoze);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<MeteoPrognoza> mp = new ArrayList<>();
            mp = wsResponse.getMprog();
            this.setMeteoPrognoza(mp);
            prikazMeteoPrognoze = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nepostojeća")) {
            nemaAdresa = true;
        }
    }

    /**
     * Preuzimanje meteo prognoze u satima
     *
     */
    public void preuzmiMeteoPrognozuSati() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;
        nemaMeteoPodataka = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajVremenskuPrognozuZaOdabranuAdresuSatima(korisnik.getKorime(), korisnik.getLozinka(), odabranaAdresa, brojSatiPrognoze);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<MeteoPodaci> mp = new ArrayList<>();
            mp = wsResponse.getMpList();
            this.setMeteoPrognozaSati(mp);
            prikazMeteoPrognozeSati = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nepostojeća")) {
            nemaMeteoPodataka = true;
        }
    }

    /**
     * Preuzimanje meteo stanica
     *
     */
    public void preuzmiMeteoStanice() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
        Korisnici korisnik = (Korisnici) session.getAttribute("korisnik");
        nedovoljnoSredstava = false;
        nemaAdresa = false;
        nijeOdabranaAdresa = false;
        nemaMeteoPodataka = false;

        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajVremenskeStaniceZaOdabranuAdresu(korisnik.getKorime(), korisnik.getLozinka(), odabranaAdresa, brojStanica);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            List<VremenskeStanice> vs = new ArrayList<>();
            vs = wsResponse.getVs();
            this.setMeteoStanice(vs);
            prikazMeteoStanica = true;
        } else if (poruka.startsWith("Nedovoljno")) {
            nedovoljnoSredstava = true;
        } else if (poruka.startsWith("Nepostojeća")) {
            nemaMeteoPodataka = true;
        }
    }

    /**
     * zatvaranje dijelova pogleda
     *
     */
    public String zatvoriPregledSvihAdresa() {
        prikazSvihAdresa = false;
        return null;
    }

    public String zatvoriPregledKorisnikovihAdresa() {
        prikazKorisnikovihAdresa = false;
        return null;
    }

    public String zatvoriPregledMeteoPodaci() {
        prikazVazecihMeteoPodataka = false;
        return null;
    }

    public String zatvoriPregledMeteoPodaciKor() {
        prikazVazecihMeteoPodatakaKor = false;
        return null;
    }

    public String zatvoriRangListuAdresa() {
        prikazRangListeAdresa = false;
        return null;
    }

    public String zatvorinMeteoPodataka() {
        prikaznMeteoPodataka = false;
        return null;
    }

    public String zatvoriMeteoPodatakeOdDo() {
        prikazMeteoPodatakaOdDo = false;
        return null;
    }

    public String zatvoriMeteoPrognozu() {
        prikazMeteoPrognoze = false;
        return null;
    }

    public String zatvoriMeteoPrognozuSati() {
        prikazMeteoPrognozeSati = false;
        return null;
    }

    public String zatvoriMeteoStanice() {
        prikazMeteoStanica = false;
        return null;
    }

    /**
     * Geteri i setteri
     *
     */
    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        MeteoAdrese.response = response;
    }

    public String getNovaAdresa() {
        return novaAdresa;
    }

    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresa = novaAdresa;
    }

    public boolean isNedovoljnoSredstava() {
        return nedovoljnoSredstava;
    }

    public void setNedovoljnoSredstava(boolean nedovoljnoSredstava) {
        this.nedovoljnoSredstava = nedovoljnoSredstava;
    }

    public boolean isAdrPostoji() {
        return adrPostoji;
    }

    public void setAdrPostoji(boolean adrPostoji) {
        this.adrPostoji = adrPostoji;
    }

    public List<Adresa> getSveAdrese() {
        return sveAdrese;
    }

    public void setSveAdrese(List<Adresa> sveAdrese) {
        this.sveAdrese = sveAdrese;
    }

    public String getOdabranaAdresa() {
        return odabranaAdresa;
    }

    public void setOdabranaAdresa(String odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    public boolean isNemaAdresa() {
        return nemaAdresa;
    }

    public void setNemaAdresa(boolean nemaAdresa) {
        this.nemaAdresa = nemaAdresa;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public boolean isPrikazSvihAdresa() {
        return prikazSvihAdresa;
    }

    public void setPrikazSvihAdresa(boolean prikazSvihAdresa) {
        this.prikazSvihAdresa = prikazSvihAdresa;
    }

    public List<Adresa> getSveKorisnikoveAdrese() {
        return sveKorisnikoveAdrese;
    }

    public void setSveKorisnikoveAdrese(List<Adresa> sveKorisnikoveAdrese) {
        this.sveKorisnikoveAdrese = sveKorisnikoveAdrese;
    }

    public boolean isPrikazKorisnikovihAdresa() {
        return prikazKorisnikovihAdresa;
    }

    public void setPrikazKorisnikovihAdresa(boolean prikazKorisnikovihAdresa) {
        this.prikazKorisnikovihAdresa = prikazKorisnikovihAdresa;
    }

    public String getOdabranaKorisnikovaAdresa() {
        return odabranaKorisnikovaAdresa;
    }

    public void setOdabranaKorisnikovaAdresa(String odabranaKorisnikovaAdresa) {
        this.odabranaKorisnikovaAdresa = odabranaKorisnikovaAdresa;
    }

    public boolean isNijeOdabranaAdresa() {
        return nijeOdabranaAdresa;
    }

    public void setNijeOdabranaAdresa(boolean nijeOdabranaAdresa) {
        this.nijeOdabranaAdresa = nijeOdabranaAdresa;
    }

    public boolean isPrikazVazecihMeteoPodataka() {
        return prikazVazecihMeteoPodataka;
    }

    public void setPrikazVazecihMeteoPodataka(boolean prikazVazecihMeteoPodataka) {
        this.prikazVazecihMeteoPodataka = prikazVazecihMeteoPodataka;
    }

    public MeteoPodaci getMpKor() {
        return mpKor;
    }

    public void setMpKor(MeteoPodaci mpKor) {
        this.mpKor = mpKor;
    }

    public MeteoPodaci getMp() {
        return mp;
    }

    public void setMp(MeteoPodaci mp) {
        this.mp = mp;
    }

    public boolean isNedovoljnoSredstavaMeteo() {
        return nedovoljnoSredstavaMeteo;
    }

    public void setNedovoljnoSredstavaMeteo(boolean nedovoljnoSredstavaMeteo) {
        this.nedovoljnoSredstavaMeteo = nedovoljnoSredstavaMeteo;
    }

    public boolean isNemaAdresaMeteo() {
        return nemaAdresaMeteo;
    }

    public void setNemaAdresaMeteo(boolean nemaAdresaMeteo) {
        this.nemaAdresaMeteo = nemaAdresaMeteo;
    }

    public boolean isPrikazVazecihMeteoPodatakaKor() {
        return prikazVazecihMeteoPodatakaKor;
    }

    public void setPrikazVazecihMeteoPodatakaKor(boolean prikazVazecihMeteoPodatakaKor) {
        this.prikazVazecihMeteoPodatakaKor = prikazVazecihMeteoPodatakaKor;
    }

    public boolean isDodanaAdresa() {
        return dodanaAdresa;
    }

    public void setDodanaAdresa(boolean dodanaAdresa) {
        this.dodanaAdresa = dodanaAdresa;
    }

    public int getBrojAdresaRangListe() {
        return brojAdresaRangListe;
    }

    public void setBrojAdresaRangListe(int brojAdresaRangListe) {
        this.brojAdresaRangListe = brojAdresaRangListe;
    }

    public boolean isPrikazRangListeAdresa() {
        return prikazRangListeAdresa;
    }

    public void setPrikazRangListeAdresa(boolean prikazRangListeAdresa) {
        this.prikazRangListeAdresa = prikazRangListeAdresa;
    }

    public List<Adresa> getRangListaAdresa() {
        return rangListaAdresa;
    }

    public void setRangListaAdresa(List<Adresa> rangListaAdresa) {
        this.rangListaAdresa = rangListaAdresa;
    }

    public int getBrojMeteoPodataka() {
        return brojMeteoPodataka;
    }

    public void setBrojMeteoPodataka(int brojMeteoPodataka) {
        this.brojMeteoPodataka = brojMeteoPodataka;
    }

    public List<MeteoPodaci> getnMeteoPodataka() {
        return nMeteoPodataka;
    }

    public void setnMeteoPodataka(List<MeteoPodaci> nMeteoPodataka) {
        this.nMeteoPodataka = nMeteoPodataka;
    }

    public boolean isPrikaznMeteoPodataka() {
        return prikaznMeteoPodataka;
    }

    public void setPrikaznMeteoPodataka(boolean prikaznMeteoPodataka) {
        this.prikaznMeteoPodataka = prikaznMeteoPodataka;
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

    public List<MeteoPodaci> getMeteoPodataciOdDo() {
        return meteoPodataciOdDo;
    }

    public void setMeteoPodataciOdDo(List<MeteoPodaci> meteoPodataciOdDo) {
        this.meteoPodataciOdDo = meteoPodataciOdDo;
    }

    public boolean isPrikazMeteoPodatakaOdDo() {
        return prikazMeteoPodatakaOdDo;
    }

    public void setPrikazMeteoPodatakaOdDo(boolean prikazMeteoPodatakaOdDo) {
        this.prikazMeteoPodatakaOdDo = prikazMeteoPodatakaOdDo;
    }

    public boolean isNemaMeteoPodataka() {
        return nemaMeteoPodataka;
    }

    public void setNemaMeteoPodataka(boolean nemaMeteoPodataka) {
        this.nemaMeteoPodataka = nemaMeteoPodataka;
    }

    public int getBrojDanaMeteoPrognoze() {
        return brojDanaMeteoPrognoze;
    }

    public void setBrojDanaMeteoPrognoze(int brojDanaMeteoPrognoze) {
        this.brojDanaMeteoPrognoze = brojDanaMeteoPrognoze;
    }

    public List<MeteoPrognoza> getMeteoPrognoza() {
        return meteoPrognoza;
    }

    public void setMeteoPrognoza(List<MeteoPrognoza> meteoPrognoza) {
        this.meteoPrognoza = meteoPrognoza;
    }

    public boolean isPrikazMeteoPrognoze() {
        return prikazMeteoPrognoze;
    }

    public void setPrikazMeteoPrognoze(boolean prikazMeteoPrognoze) {
        this.prikazMeteoPrognoze = prikazMeteoPrognoze;
    }

    public int getBrojSatiPrognoze() {
        return brojSatiPrognoze;
    }

    public void setBrojSatiPrognoze(int brojSatiPrognoze) {
        this.brojSatiPrognoze = brojSatiPrognoze;
    }

    public List<MeteoPodaci> getMeteoPrognozaSati() {
        return meteoPrognozaSati;
    }

    public void setMeteoPrognozaSati(List<MeteoPodaci> meteoPrognozaSati) {
        this.meteoPrognozaSati = meteoPrognozaSati;
    }

    public boolean isPrikazMeteoPrognozeSati() {
        return prikazMeteoPrognozeSati;
    }

    public void setPrikazMeteoPrognozeSati(boolean prikazMeteoPrognozeSati) {
        this.prikazMeteoPrognozeSati = prikazMeteoPrognozeSati;
    }

    public int getBrojStanica() {
        return brojStanica;
    }

    public void setBrojStanica(int brojStanica) {
        this.brojStanica = brojStanica;
    }

    public List<VremenskeStanice> getMeteoStanice() {
        return meteoStanice;
    }

    public void setMeteoStanice(List<VremenskeStanice> meteoStanice) {
        this.meteoStanice = meteoStanice;
    }

    public boolean isPrikazMeteoStanica() {
        return prikazMeteoStanica;
    }

    public void setPrikazMeteoStanica(boolean prikazMeteoStanica) {
        this.prikazMeteoStanica = prikazMeteoStanica;
    }

    public String getMarkerData() {
        markerData = odabranaAdresa + "<br>" + mp.getTemperatureValue() + " °C <br>" + mp.getPressureValue() + " hPa <br>" + mp.getWeatherValue();
        return markerData;
    }

    public void setMarkerData(String markerData) {
        this.markerData = markerData;
    }

}
