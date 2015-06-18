/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.rest.serveri;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ws.klijenti.GeoMeteoWSKlijent;
import org.foi.nwtis.mkovacek.ws.server.Adresa;
import org.foi.nwtis.mkovacek.ws.server.GeoMeteoWsResponse;

/**
 * REST Web Service
 *
 * @author Matija
 */
public class AktivniKorisniciResource {

    private String korisnik;

    /**
     * Creates a new instance of AktivniKorisniciResource
     */
    private AktivniKorisniciResource(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     * Get instance of the AktivniKorisniciResource
     */
    public static AktivniKorisniciResource getInstance(String korisnik) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of AktivniKorisniciResource class.
        return new AktivniKorisniciResource(korisnik);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.mkovacek.rest.serveri.AktivniKorisniciResource
     *
     * Vraća listu adresa za aktivnog korisnika
     * 
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        List<Adresa> preuzeteAdrese = new ArrayList<>();
        JSONObject rezultat = new JSONObject();
        GeoMeteoWsResponse wsResponse = new GeoMeteoWsResponse();
        wsResponse = GeoMeteoWSKlijent.dajSveKorisnikoveAdreseZaRest(korisnik);
        String poruka = wsResponse.getPoruka();
        if (poruka.equals("OK")) {
            preuzeteAdrese = wsResponse.getAdrese();
            if (preuzeteAdrese != null) {
                JSONArray jaAdrese = new JSONArray();
                int i = 0;
                for (Adresa a : preuzeteAdrese) {
                    try {
                        JSONObject joAdresa = new JSONObject();
                        joAdresa.put("id", Long.toString(a.getIdadresa()));
                        joAdresa.put("adresa", a.getAdresa());
                        joAdresa.put("latitude", a.getGeoloc().getLatitude());
                        joAdresa.put("longitude", a.getGeoloc().getLongitude());
                        joAdresa.put("korisnik", a.getKorisnik());
                        jaAdrese.put(i, joAdresa);
                    } catch (JSONException ex) {
                        Logger.getLogger(AktivniKorisniciResource.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i++;
                }
                try {
                    rezultat.put("adrese", jaAdrese);
                } catch (JSONException ex) {
                    Logger.getLogger(AktivniKorisniciResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return rezultat.toString();
    }

    /**
     * PUT method for updating or creating an instance of
     * AktivniKorisniciResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource AktivniKorisniciResource
     */
    @DELETE
    public void delete() {
    }
}
