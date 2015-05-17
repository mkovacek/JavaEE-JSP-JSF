/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.rest.serveri;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;


/**
 * REST Web Service
 *
 * @author mkovacek
 */
public class MeteoRESTResource {

    private String id;

    /**
     * Creates a new instance of MeteoRESTResource
     */
    private MeteoRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the MeteoRESTResource
     */
    public static MeteoRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MeteoRESTResource class.
        return new MeteoRESTResource(id);
    }

    /**
     * Metoda vraća podatke o pojedinoj adresi, na temljelju putanje {id}
     * 
     * Retrieves representation of an instance of
     * org.foi.nwtis.mkovacek.rest.serveri.MeteoRESTResource
     *
     * @return an instance of java.lang.String 
     */
    @GET
    @Produces("application/json; charset=UTF-8")
    public String getJson() {
        BazaPodataka bp = new BazaPodataka();
        Adresa adresa = new Adresa();
        adresa = bp.dohvatiGeoPodatkeZaPojedinuAdresu(Long.valueOf(id));
        JSONObject rezultat = new JSONObject();
        if (adresa != null) {
            try {
                rezultat.put("id", Long.toString(adresa.getIdadresa()));
                rezultat.put("adresa", adresa.getAdresa());
                rezultat.put("latitude", adresa.getGeoloc().getLatitude());
                rezultat.put("longitude", adresa.getGeoloc().getLongitude());
            } catch (JSONException ex) {
                Logger.getLogger(MeteoRESTResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rezultat.toString();
    }

    /**
     * PUT method for updating or creating an instance of MeteoRESTResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource MeteoRESTResource
     */
    @DELETE
    public void delete() {
    }
}
