/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

import java.util.List;

/**
 *
 * @author Matija
 */
public class GeoMeteoWsResponse {
    private String poruka;
    private MeteoPodaci mp;
    private List<MeteoPodaci> mpList;
    private List<Adresa> adrese;
    private MeteoPrognoza[] mprog;
    private List<VremenskeStanice> vs;

    public GeoMeteoWsResponse() {
    }

    
    
    public GeoMeteoWsResponse(String poruka) {
        this.poruka = poruka;
    }

    public GeoMeteoWsResponse(String poruka, MeteoPodaci mp) {
        this.poruka = poruka;
        this.mp = mp;
    }

    public GeoMeteoWsResponse(String poruka, List<MeteoPodaci> mpList) {
        this.poruka = poruka;
        this.mpList = mpList;
    }

    public GeoMeteoWsResponse(String poruka, MeteoPrognoza[] mprog) {
        this.poruka = poruka;
        this.mprog = mprog;
    }
    
    
    public List<VremenskeStanice> getVs() {
        return vs;
    }

    public void setVs(List<VremenskeStanice> vs) {
        this.vs = vs;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public MeteoPodaci getMp() {
        return mp;
    }

    public void setMp(MeteoPodaci mp) {
        this.mp = mp;
    }

    public List<MeteoPodaci> getMpList() {
        return mpList;
    }

    public void setMpList(List<MeteoPodaci> mpList) {
        this.mpList = mpList;
    }

    public List<Adresa> getAdrese() {
        return adrese;
    }

    public void setAdrese(List<Adresa> adrese) {
        this.adrese = adrese;
    }

    public MeteoPrognoza[] getMprog() {
        return mprog;
    }

    public void setMprog(MeteoPrognoza[] mprog) {
        this.mprog = mprog;
    }
    
    
    
}
