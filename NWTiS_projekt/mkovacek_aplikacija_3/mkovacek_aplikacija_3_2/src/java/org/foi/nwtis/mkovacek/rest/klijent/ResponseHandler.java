/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.rest.klijent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;

/**
 * Klasa za obradu REST responsa u odgovarajući tip objekta
 *
 * @author Matija
 */
public class ResponseHandler {

    public ResponseHandler() {
    }

    /**
     * Metoda za obradu REST s aktivnim korisnicima
     *
     * @author Matija
     */
    public List<Korisnik> listaAktivnihKorisnika(String response) {
        List<Korisnik> aktivniKorisnici = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(response);
            JSONArray ja = jo.getJSONArray("aktivniKorisnici");
            for (int i = 0; i < ja.length(); i++) {
                Korisnik kor = new Korisnik();
                kor.setId(ja.getJSONObject(i).getInt("id"));
                kor.setKorIme(ja.getJSONObject(i).getString("korIme"));
                kor.setIme(ja.getJSONObject(i).getString("ime"));
                kor.setPrezime(ja.getJSONObject(i).getString("prezime"));
                kor.setLozinka(ja.getJSONObject(i).getString("lozinka"));
                kor.setEmail(ja.getJSONObject(i).getString("email"));
                kor.setTip(ja.getJSONObject(i).getInt("vrsta"));
                aktivniKorisnici.add(i, kor);
            }
        } catch (JSONException ex) {
            Logger.getLogger(ResponseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aktivniKorisnici;
    }

    /**
     * Metoda za obradu REST s adresama aktivnog korisnika
     *
     * @author Matija
     */
    public List<Adresa> listaKorisnikovihAdresa(String response) {
        List<Adresa> adrese = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(response);
            JSONArray ja = jo.getJSONArray("adrese");
            for (int i = 0; i < ja.length(); i++) {
                Adresa adr = new Adresa();
                Lokacija l = new Lokacija();
                adr.setIdadresa(ja.getJSONObject(i).getLong("id"));
                adr.setAdresa(ja.getJSONObject(i).getString("adresa"));
                l.setLatitude(ja.getJSONObject(i).getString("latitude"));
                l.setLongitude(ja.getJSONObject(i).getString("longitude"));
                adr.setGeoloc(l);
                adr.setKorisnik(ja.getJSONObject(i).getString("korisnik"));
                adrese.add(i, adr);
            }
        } catch (JSONException ex) {
            Logger.getLogger(ResponseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adrese;
    }
}
