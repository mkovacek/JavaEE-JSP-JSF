/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.rest.klijenti;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;

/**
 * Klasa za rad s openweather map servisom
 *
 * @author mkovacek
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    /**
     * Konstruktor. Setira se apiKey za rad s servisom.Kreira se klijent i
     * pomoćna klasa za rad s servisom
     */
    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    /**
     * Metoda za dohvaćanje meteoroloških podataka za pojedinu adresu putem
     * openweather map servisa
     *
     * @param latitude adrese za koju dohvaćamo meteo podatke
     * @param longitude adrese za koju dohvaćamo meteo podatke
     * @return objekt tipa MeteoPodaci koji sadrži meteo podatke za unesene
     * latitude i longitude
     */
    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JSONObject jo = new JSONObject(odgovor);
            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJSONObject("sys").getLong("sunrise") * 1000));
            mp.setSunSet(new Date(jo.getJSONObject("sys").getLong("sunset") * 1000));

            mp.setTemperatureValue(Float.parseFloat(jo.getJSONObject("main").getString("temp")));
            mp.setTemperatureMin(Float.parseFloat(jo.getJSONObject("main").getString("temp_min")));
            mp.setTemperatureMax(Float.parseFloat(jo.getJSONObject("main").getString("temp_max")));
            mp.setTemperatureUnit("celsius");

            mp.setPressureValue(Float.parseFloat(jo.getJSONObject("main").getString("pressure")));
            mp.setPressureUnit("hPa");

            mp.setHumidityValue(Float.parseFloat(jo.getJSONObject("main").getString("humidity")));
            mp.setHumidityUnit("%");

            mp.setWindSpeedValue(Float.parseFloat(jo.getJSONObject("wind").getString("speed")));
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(Float.parseFloat(jo.getJSONObject("wind").getString("deg")));
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJSONObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJSONArray("weather").getJSONObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJSONArray("weather").getJSONObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getLong("dt") * 1000));

            return mp;

        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
