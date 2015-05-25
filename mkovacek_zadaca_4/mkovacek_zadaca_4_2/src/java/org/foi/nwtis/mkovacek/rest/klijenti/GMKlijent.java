/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.rest.klijenti;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;

/**
 * Klasa za rad s Google maps API
 *
 * @author mkovacek
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;

    /**
     * Konstruktor. Kreira se klijent
     */
    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka za pojedinu adresu putem
     * Google maps API
     *
     * @param adresa adresa za koju dohvaćamo geolokacijske podatke
     * @return objekt tipa lokacija koji sadrži latitude i longitude unesene
     * adrese
     */
    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JSONObject jo = new JSONObject(odgovor);
            JSONObject obj = jo.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            Lokacija loc = new Lokacija(obj.getString("lat"), obj.getString("lng"));

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
