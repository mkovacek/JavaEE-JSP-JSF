/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ws.serveri;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * SOAP web servis za meteorološke podatke
 *
 * @author mkovacek
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation dajSveAdrese
     *
     * @return listu adresa iz baze podataka
     */
    @WebMethod(operationName = "dajSveAdrese")
    public java.util.List<Adresa> dajSveAdrese() {
        BazaPodataka bp = new BazaPodataka();
        List<Adresa> adrese = new ArrayList<Adresa>();
        adrese = bp.dohvatiGeoPodatke();
        return adrese;
    }

    /**
     * Web service operation dajVazeceMeteoPodatkeZaAdresu
     *
     * @param adresa - id adrese
     * @return vraća važeće meteo podatke za adresu pomoću owm servisa, u
     * objektu tipa MeteoPodaci
     */
    @WebMethod(operationName = "dajVazeceMeteoPodatkeZaAdresu")
    public MeteoPodaci dajVazeceMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        BazaPodataka bp = new BazaPodataka();
        Adresa a = bp.dohvatiGeoPodatkeZaPojedinuAdresu(Long.parseLong(adresa));
        String apiKey = SlusacAplikacije.konfig.dajPostavku("owmApiKey");
        OWMKlijent owmk = new OWMKlijent(apiKey);
        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    /**
     * Web service operation dajZadnjeMeteoPodatkeZaAdresu
     *
     * @param adresa - id adrese
     * @return vraća važeće zadnje meteo podatke za adresu u objektu tipa
     * MeteoPodaci iz baze podataka
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatkeZaAdresu")
    public MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        MeteoPodaci mp = new MeteoPodaci();
        BazaPodataka bp = new BazaPodataka();
        mp = bp.dajZadnjeMeteoPodatkeZaAdresu(Long.parseLong(adresa));
        return mp;

    }

    /**
     * Web service operation dajSveMeteoPodatkeZaAdresu
     *
     * @param adresa - id adrese
     * @return vraća sve meto podatke adrese iz baze podataka u listi tipa MeteoPodaci
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaAdresu")
    public java.util.List<MeteoPodaci> dajSveMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        List<MeteoPodaci> mp = new ArrayList<MeteoPodaci>();
        BazaPodataka bp = new BazaPodataka();
        mp = bp.dajSveMeteoPodatkeZaAdresu(Long.parseLong(adresa));
        return mp;
    }

}
