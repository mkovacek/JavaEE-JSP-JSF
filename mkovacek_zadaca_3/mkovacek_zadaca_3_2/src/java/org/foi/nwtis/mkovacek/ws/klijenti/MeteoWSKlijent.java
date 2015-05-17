/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ws.klijenti;

import org.foi.nwtis.mkovacek.ws.serveri.MeteoPodaci;

/**
 * Klijent web servisa GeoMeteoWS iz mkovacek_zadaca_3_1
 *
 * @author mkovacek
 */
public class MeteoWSKlijent {

    /**
     * Poziv operacije dajSveAdrese na web servisu GeoMeteoWS
     *
     * @author mkovacek
     */
    public static java.util.List<org.foi.nwtis.mkovacek.ws.serveri.Adresa> dajSveAdrese() {
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveAdrese();
    }

    /**
     * Poziv operacije dajSveMeteoPodatkeZaAdresu na web servisu GeoMeteoWS
     *
     * @author mkovacek
     */
    public static java.util.List<org.foi.nwtis.mkovacek.ws.serveri.MeteoPodaci> dajSveMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaAdresu(adresa);
    }

    /**
     * Poziv operacije dajVazeceMeteoPodatkeZaAdresu na web servisu GeoMeteoWS
     *
     * @author mkovacek
     */
    public static MeteoPodaci dajVazeceMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatkeZaAdresu(adresa);
    }

    /**
     * Poziv operacije dajZadnjeMeteoPodatkeZaAdresu na web servisu GeoMeteoWS
     *
     * @author mkovacek
     */
    public static MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeMeteoPodatkeZaAdresu(adresa);
    }

}
