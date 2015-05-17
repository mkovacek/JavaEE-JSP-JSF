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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;

/**
 * REST Web Service
 *
 * @author NWTiS_4
 */
@Path("/meteoREST")
public class MeteoRESTResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MeteoRESTResourceContainer
     */
    public MeteoRESTResourceContainer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.matnovak.rest.serveri.MeteoRESTResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json; charset=UTF-8")
    public String getJson() {
        List<Adresa> adrese = new ArrayList<Adresa>();
        BazaPodataka bp = new BazaPodataka();
        adrese = bp.dohvatiGeoPodatke();

        JSONObject rezultat = new JSONObject();
        JSONArray jaAdrese = new JSONArray();
        int i = 0;
        if (adrese != null) {
            for (Adresa a : adrese) {
                try {
                    JSONObject joAdresa = new JSONObject();
                    joAdresa.put("id", Long.toString(a.getIdadresa()));
                    joAdresa.put("adresa", a.getAdresa());
                    joAdresa.put("latitude", a.getGeoloc().getLatitude());
                    joAdresa.put("longitude", a.getGeoloc().getLongitude());
                    jaAdrese.put(i, joAdresa);
                } catch (JSONException ex) {
                    Logger.getLogger(MeteoRESTResourceContainer.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }

            try {
                rezultat.put("adrese", jaAdrese);
            } catch (JSONException ex) {
                Logger.getLogger(MeteoRESTResourceContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rezultat.toString();
    }

    /**
     * POST method for creating an instance of MeteoRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response postJson(String content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public MeteoRESTResource getMeteoRESTResource(@PathParam("id") String id) {
        return MeteoRESTResource.getInstance(id);
    }
}
