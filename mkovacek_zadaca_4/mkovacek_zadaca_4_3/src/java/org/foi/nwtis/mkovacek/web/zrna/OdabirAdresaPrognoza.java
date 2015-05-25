/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.zrna;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mkovacek.ejb.eb.Adrese;
import org.foi.nwtis.mkovacek.ejb.eb.Dnevnik;
import org.foi.nwtis.mkovacek.ejb.sb.AdreseFacade;
import org.foi.nwtis.mkovacek.ejb.sb.DnevnikFacade;
import org.foi.nwtis.mkovacek.ejb.sb.MeteoAdresniKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPrognoza;

/**
 * JSF ManageBean OdabirAdresaPrognoza
 *
 * @author mkovacek
 */
@ManagedBean
@SessionScoped
public class OdabirAdresaPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    @EJB
    private MeteoAdresniKlijent meteoAdresniKlijent;
    @EJB
    private AdreseFacade adreseFacade;

    private String novaAdresa;
    private String odabranaAdresaZaDodati;
    private String odabranaAdresaZaMaknuti;
    private String azuriranaAdresa;
    private String originalnaAdresa;
    private Map<String, Object> aktivneAdrese;
    private Map<String, Object> odabraneAdrese;
    private List<MeteoPrognoza> prognoza;
    private int brojDana;
    private boolean prikaziAzuriranje = false;
    private boolean prikazGreske = false;
    private boolean prikazGreskeKodAzuriranja = false;
    private boolean prikazPrognoza = false;
    private boolean prikazGreskePrognoza = false;
    public static Konfiguracija konfig;

    /**
     * Creates a new instance of OdabirAdresaPrognoza
     */
    public OdabirAdresaPrognoza() {
    }

    /**
     * Metoda za dohvaćanje svih adresa iz baze podataka i postavljanje u popis
     * aktivnih adresa.
     *
     */
    public void preuzmiAdrese() {
        List<Adrese> preuzeteAdrese = adreseFacade.findAll();
        Map<String, Object> adr = new HashMap<>();
        for (Adrese a : preuzeteAdrese) {
            adr.put(a.getAdresa(), a.getAdresa());
        }
        this.setAktivneAdrese(adr);
    }

    /**
     * Metoda za zapis akcije u bazu podataka (u tbl Dnevnik)
     *
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
     * Metoda za zapis nove, unesene adrese u popis aktivnih adresa te novu
     * adresu pohranjuje u tbl Adrese u bazi podataka
     *
     *
     */
    public Object upisiAdresu() {
        int pocetak = (int) System.currentTimeMillis();
        Adrese adresa = new Adrese();
        adresa.setAdresa(novaAdresa);
        Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
        adresa.setLatitude(l.getLatitude());
        adresa.setLongitude(l.getLongitude());
        adreseFacade.create(adresa);
        aktivneAdrese.put(novaAdresa, novaAdresa);
        this.log((int) System.currentTimeMillis() - pocetak, 200);
        return null;
    }

    /**
     * Metoda za prikazivanje dijela ekrana za ažuriranje adrese.Ukoliko nije
     * odabrana adresa prikazuje se poruka greške.
     *
     */
    public Object otvoriAzuriranjeAdrese() {
        int pocetak = (int) System.currentTimeMillis();
        if (odabranaAdresaZaDodati != null) {
            this.originalnaAdresa = odabranaAdresaZaDodati;
            azuriranaAdresa = odabranaAdresaZaDodati;
            originalnaAdresa = odabranaAdresaZaDodati;
            this.prikazGreske = false;
            this.prikaziAzuriranje = true;
            this.log((int) System.currentTimeMillis() - pocetak, 200);
        } else {
            this.prikazGreske = true;
            this.log((int) System.currentTimeMillis() - pocetak, 400);
        }
        return null;
    }

    /**
     * Metoda za ažuriranje adrese u bazi podataka te u popisu aktivnih adresa.
     *
     */
    public Object azurirajAdresu() {
        int pocetak = (int) System.currentTimeMillis();
        if (azuriranaAdresa != null) {
            List<Adrese> dohvacenaAdresaZaAzuriranje = new ArrayList<>();
            dohvacenaAdresaZaAzuriranje = adreseFacade.findByAdresa(originalnaAdresa);
            Adrese adresa = dohvacenaAdresaZaAzuriranje.get(0);
            adresa.setAdresa(azuriranaAdresa);
            Lokacija l = meteoAdresniKlijent.dajLokaciju(azuriranaAdresa);
            adresa.setLatitude(l.getLatitude());
            adresa.setLongitude(l.getLongitude());
            adreseFacade.edit(adresa);
            aktivneAdrese.remove(odabranaAdresaZaDodati);
            aktivneAdrese.put(azuriranaAdresa, azuriranaAdresa);
            this.prikaziAzuriranje = false;
            this.log((int) System.currentTimeMillis() - pocetak, 200);
        } else {
            this.log((int) System.currentTimeMillis() - pocetak, 400);
        }

        return null;
    }

    /**
     * Metoda za prebacivanje adresa iz popisa aktivnih adresa u popis odabranih
     * adresa. Prilikom prebacivanja odabrana adresa se briše iz popisa aktivnih
     * adresa.
     *
     */
    public String prebaciAdresu() {
        int pocetak = (int) System.currentTimeMillis();
        if (odabranaAdresaZaDodati != null) {
            if (odabraneAdrese == null) {
                odabraneAdrese = new HashMap<>();
                odabraneAdrese.put(odabranaAdresaZaDodati, odabranaAdresaZaDodati);
            } else {
                odabraneAdrese.put(odabranaAdresaZaDodati, odabranaAdresaZaDodati);
            }
            aktivneAdrese.remove(odabranaAdresaZaDodati);
            this.log((int) System.currentTimeMillis() - pocetak, 200);
        } else {
            this.log((int) System.currentTimeMillis() - pocetak, 400);
        }
        return null;
    }

    /**
     * Metoda za prebacivanje adresa iz popisa odabranih adresa u popis aktivnih
     * adresa. Prilikom prebacivanja odabrana adresa se briše iz popisa
     * odabranih adresa.
     *
     */
    public String makniAdresu() {
        int pocetak = (int) System.currentTimeMillis();
        if (odabranaAdresaZaMaknuti != null) {
            if (aktivneAdrese == null) {
                aktivneAdrese = new HashMap<>();
                aktivneAdrese.put(odabranaAdresaZaMaknuti, odabranaAdresaZaMaknuti);
            } else {
                aktivneAdrese.put(odabranaAdresaZaMaknuti, odabranaAdresaZaMaknuti);
            }
            odabraneAdrese.remove(odabranaAdresaZaMaknuti);
            this.log((int) System.currentTimeMillis() - pocetak, 200);
        } else {
            this.log((int) System.currentTimeMillis() - pocetak, 400);
        }
        return null;

    }

    /**
     * Metoda za dohvaćanje i prikazivanje meteo prognoze (za određeni broj
     * dana) za adrese koje se nalaze u popisu odabranih adresa.
     *
     */
    public String pregledPrognoza() {
        int pocetak = (int) System.currentTimeMillis();
        if (odabraneAdrese == null) {
            odabraneAdrese = new HashMap<>();
        }
        if (!odabraneAdrese.isEmpty()) {
            this.kontekst();
            prognoza = new ArrayList<>();
            meteoAdresniKlijent.postaviKorisnickePodatke(konfig.dajPostavku("apiKey"));
            for (String adr : odabraneAdrese.keySet()) {
                MeteoPrognoza[] mprognoza = new MeteoPrognoza[brojDana];
                mprognoza = meteoAdresniKlijent.dajMeteoPrognoze(adr, brojDana);
                for (MeteoPrognoza mprog : mprognoza) {
                    prognoza.add(mprog);
                }
            }
            this.prikazPrognoza = true;
            this.prikazGreskePrognoza = false;
            this.log((int) System.currentTimeMillis() - pocetak, 200);
        } else {
            this.prikazGreskePrognoza = true;
            this.log((int) System.currentTimeMillis() - pocetak, 400);
        }
        return null;

    }

    /**
     * Metoda za zatvarnje/sakrivanje pregleda meteo pronoze.
     *
     */
    public String zatvoriPregledPrognoza() {
        int pocetak = (int) System.currentTimeMillis();
        this.log((int) System.currentTimeMillis() - pocetak, 200);
        this.prikazPrognoza = false;
        return null;

    }

    /**
     * Metoda preuzima konfiguraciju.
     *
     */
    public void kontekst() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String direktorij = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + servletContext.getInitParameter("konfiguracija");
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(OdabirAdresaPrognoza.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Geteri i seteri
     */
    public String getNovaAdresa() {
        return novaAdresa;
    }

    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresa = novaAdresa;
    }

    public String getOdabranaAdresaZaDodati() {
        return odabranaAdresaZaDodati;
    }

    public void setOdabranaAdresaZaDodati(String odabranaAdresaZaDodati) {
        this.odabranaAdresaZaDodati = odabranaAdresaZaDodati;
    }

    public String getOdabranaAdresaZaMaknuti() {
        return odabranaAdresaZaMaknuti;
    }

    public void setOdabranaAdresaZaMaknuti(String odabranaAdresaZaMaknuti) {
        this.odabranaAdresaZaMaknuti = odabranaAdresaZaMaknuti;
    }

    public String getAzuriranaAdresa() {
        return azuriranaAdresa;
    }

    public void setAzuriranaAdresa(String azuriranaAdresa) {
        this.azuriranaAdresa = azuriranaAdresa;
    }

    public Map<String, Object> getOdabraneAdrese() {
        return odabraneAdrese;
    }

    public void setOdabraneAdrese(Map<String, Object> odabraneAdrese) {
        this.odabraneAdrese = odabraneAdrese;
    }

    public boolean isPrikaziAzuriranje() {
        return prikaziAzuriranje;
    }

    public void setPrikaziAzuriranje(boolean prikaziAzuriranje) {
        this.prikaziAzuriranje = prikaziAzuriranje;
    }

    public MeteoAdresniKlijent getMeteoAdresniKlijent() {
        return meteoAdresniKlijent;
    }

    public void setMeteoAdresniKlijent(MeteoAdresniKlijent meteoAdresniKlijent) {
        this.meteoAdresniKlijent = meteoAdresniKlijent;
    }

    public AdreseFacade getAdreseFacade() {
        return adreseFacade;
    }

    public void setAdreseFacade(AdreseFacade adreseFacade) {
        this.adreseFacade = adreseFacade;
    }

    public String getOriginalnaAdresa() {
        return originalnaAdresa;
    }

    public void setOriginalnaAdresa(String originalnaAdresa) {
        this.originalnaAdresa = originalnaAdresa;
    }

    public boolean isPrikazGreske() {
        return prikazGreske;
    }

    public void setPrikazGreske(boolean prikazGreske) {
        this.prikazGreske = prikazGreske;
    }

    public boolean isPrikazGreskeKodAzuriranja() {
        return prikazGreskeKodAzuriranja;
    }

    public void setPrikazGreskeKodAzuriranja(boolean prikazGreskeKodAzuriranja) {
        this.prikazGreskeKodAzuriranja = prikazGreskeKodAzuriranja;
    }

    public int getBrojDana() {
        return brojDana;
    }

    public void setBrojDana(int brojDana) {
        this.brojDana = brojDana;
    }

    public boolean isPrikazPrognoza() {
        return prikazPrognoza;
    }

    public void setPrikazPrognoza(boolean prikazPrognoza) {
        this.prikazPrognoza = prikazPrognoza;
    }

    public List<MeteoPrognoza> getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(List<MeteoPrognoza> prognoza) {
        this.prognoza = prognoza;
    }

    public boolean isPrikazGreskePrognoza() {
        return prikazGreskePrognoza;
    }

    public void setPrikazGreskePrognoza(boolean prikazGreskePrognoza) {
        this.prikazGreskePrognoza = prikazGreskePrognoza;
    }

    public Map<String, Object> getAktivneAdrese() {
        if (aktivneAdrese == null) {
            preuzmiAdrese();
        }
        return aktivneAdrese;
    }

    public void setAktivneAdrese(Map<String, Object> aktivneAdrese) {
        this.aktivneAdrese = aktivneAdrese;
    }

}
