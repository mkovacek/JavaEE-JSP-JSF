/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ws.klijenti;

import org.foi.nwtis.mkovacek.ws.server.GeoMeteoWsResponse;

/**
 *
 * @author Matija
 */
public class GeoMeteoWSKlijent {

    public static GeoMeteoWsResponse dajSveAdrese(java.lang.String korisnik, java.lang.String lozinka) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveAdrese(korisnik, lozinka);
    }

    public static GeoMeteoWsResponse dajMeteoPodatkeZaAdresuOdDo(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa, java.lang.String odDatum, java.lang.String doDatum) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajMeteoPodatkeZaAdresuOdDo(korisnik, lozinka, adresa, odDatum, doDatum);
    }

    public static GeoMeteoWsResponse dajMeteoPrognozuZaAdresu(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa, int brojDana) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajMeteoPrognozuZaAdresu(korisnik, lozinka, adresa, brojDana);
    }

    public static GeoMeteoWsResponse dajNzadnjihMeteoPodatakaZaAdresu(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa, int broj) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajNzadnjihMeteoPodatakaZaAdresu(korisnik, lozinka, adresa, broj);
    }

    public static GeoMeteoWsResponse dajRangListuAdresaNajviseMeteoPodataka(java.lang.String korisnik, java.lang.String lozinka, int brojAdresa) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajRangListuAdresaNajviseMeteoPodataka(korisnik, lozinka, brojAdresa);
    }

    public static GeoMeteoWsResponse dajSveKorisnikoveAdrese(java.lang.String korisnik, java.lang.String lozinka) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveKorisnikoveAdrese(korisnik, lozinka);
    }

    public static GeoMeteoWsResponse dajVazeceMeteoPodatkeZaAdresu(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatkeZaAdresu(korisnik, lozinka, adresa);
    }

    public static GeoMeteoWsResponse dajVremenskeStaniceZaOdabranuAdresu(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa, int brojStanica) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVremenskeStaniceZaOdabranuAdresu(korisnik, lozinka, adresa, brojStanica);
    }

    public static GeoMeteoWsResponse dajVremenskuPrognozuZaOdabranuAdresuSatima(java.lang.String korisnik, java.lang.String lozinka, java.lang.String adresa, int brojSati) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVremenskuPrognozuZaOdabranuAdresuSatima(korisnik, lozinka, adresa, brojSati);
    }

    public static GeoMeteoWsResponse dajSveKorisnikoveAdreseZaRest(java.lang.String korisnik) {
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service service = new org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS_Service();
        org.foi.nwtis.mkovacek.ws.server.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveKorisnikoveAdreseZaRest(korisnik);
    }
    
    
}
