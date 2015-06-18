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
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.zrna.AktivniKorisnici;

/**
 * REST Web Service
 *
 * @author Matija
 */
@Path("/aktivniKorisnici")
public class AktivniKorisnicisResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AktivniKorisnicisResource
     */
    public AktivniKorisnicisResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.mkovacek.rest.serveri.AktivniKorisnicisResource
     *
     * Vraća listu aktivnih korisnika
     * 
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        List<Korisnici> listaAktivnihKorisnika = new ArrayList<>();
        listaAktivnihKorisnika = AktivniKorisnici.aktivniKorisnici;

        JSONObject rezultat = new JSONObject();
        JSONArray jaKorisnik = new JSONArray();
        int i = 0;
        if (listaAktivnihKorisnika != null) {
            for (Korisnici kor : listaAktivnihKorisnika) {
                try {
                    JSONObject joKor = new JSONObject();
                    joKor.put("id", Integer.toString(kor.getId()));
                    joKor.put("korIme", kor.getKorime());
                    joKor.put("ime", kor.getIme());
                    joKor.put("prezime", kor.getPrezime());
                    joKor.put("lozinka", kor.getLozinka());
                    joKor.put("email", kor.getEmailAdresa());
                    joKor.put("vrsta", Integer.toString(kor.getVrsta()));
                    jaKorisnik.put(i, joKor);
                } catch (JSONException ex) {
                    Logger.getLogger(AktivniKorisnicisResource.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }

            try {
                rezultat.put("aktivniKorisnici", jaKorisnik);
            } catch (JSONException ex) {
                Logger.getLogger(AktivniKorisnicisResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rezultat.toString();
    }

    /**
     * POST method for creating an instance of AktivniKorisniciResource
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
     * Sub-resource locator method for {korisnik}
     */
    @Path("{korisnik}")
    public AktivniKorisniciResource getAktivniKorisniciResource(@PathParam("korisnik") String korisnik) {
        return AktivniKorisniciResource.getInstance(korisnik);
    }
}
