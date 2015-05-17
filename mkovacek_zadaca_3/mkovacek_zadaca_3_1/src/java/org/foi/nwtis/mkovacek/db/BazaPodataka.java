/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.web.kontrole.Kontroler;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Klasa za rad s bazom podataka
 *
 * @author mkovacek
 */
public class BazaPodataka {

    private String server;
    private String baza;
    private String korisnik;
    private String lozinka;
    private String driver;

    /**
     * Konstruktor. Postavljaju se postavke za povezivanje na bazu podataka koje
     * se čitaju iz datoteke
     */
    public BazaPodataka() {
        server = SlusacAplikacije.bpk.getServer_database();
        baza = server + SlusacAplikacije.bpk.getUser_database();
        korisnik = SlusacAplikacije.bpk.getUser_username();
        lozinka = SlusacAplikacije.bpk.getUser_password();
        driver = SlusacAplikacije.bpk.getDriver_database(server);
    }

    /**
     * Metoda za pohranjivanje geolokacijskih podataka za pojedinu adresu u bazu
     * podataka.
     *
     * @param adresa - objekt tipa adresa koji sadrži, naziv adrese, te latitude
     * i longitude
     */
    public void spremiGeoPodatke(Adresa adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO adrese (adresa,latitude,longitude) VALUES(?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, adresa.getAdresa());
                pstmt.setString(2, adresa.getGeoloc().getLatitude());
                pstmt.setString(3, adresa.getGeoloc().getLongitude());
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka za pojedinu adresu iz baze
     * podataka.
     *
     * @param idAdrese - id adrese za koju se dohvaćaju podaci.
     * @return objekt tipa adresa, sa svim geolokacijskim podacima adrese
     */
    public Adresa dohvatiGeoPodatkeZaPojedinuAdresu(long idAdrese) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM adrese where idadresa=?";
        Adresa adresa = null;
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setLong(1, idAdrese);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                adresa = new Adresa(Long.parseLong(odg.getString(1)), odg.getString(2), new Lokacija(odg.getString(3), odg.getString(4)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adresa;
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka svih adresa iz baze
     * podataka.
     *
     * @return lista adresa sa svim geolokacijskim podacima adresa
     */
    public List<Adresa> dohvatiGeoPodatke() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM adrese";
        List<Adresa> adrese = new ArrayList<Adresa>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Adresa adresa = new Adresa(Long.parseLong(odg.getString(1)), odg.getString(2), new Lokacija(odg.getString(3), odg.getString(4)));
                adrese.add(index, adresa);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adrese;
    }

    /**
     * Metoda za spremanje meteoroloških podataka za pojedinu adresu u bazu
     * podataka.
     *
     * @param idAdrese id adrese za koju pohranjujemo u bazu
     * @param mp objekt meteopodaci koji sadrži meteorološke podatke za adresu
     */
    public void spremiMeteoPodatke(long idAdrese, MeteoPodaci mp) {

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_meteo (idadresa,izlazaksunca,zalazaksunca,temperatura,naoblaka,tlakzraka) VALUES(?,?,?,?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setLong(1, idAdrese);
                pstmt.setTimestamp(2, new Timestamp(mp.getSunRise().getTime()));
                pstmt.setTimestamp(3, new Timestamp(mp.getSunSet().getTime()));
                pstmt.setString(4, mp.getTemperatureValue().toString());
                pstmt.setString(5, mp.getCloudsName());
                pstmt.setString(6, mp.getPressureValue().toString());
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za dohvaćanje zadnjih meteoroloških podataka za pojedinu adresu iz
     * baze podataka.
     *
     * @param idAdresa - id adrese za koju se dohvaćaju posljednji meteorološki
     * podaci.
     * @return objekt tipa meteopodaci, sa svim zadnjim meteorološkim podacima
     * adrese
     */
    public MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(long idAdresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_meteo where idadresa=? ORDER BY id DESC FETCH FIRST ROW ONLY";
        MeteoPodaci mp = null;
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setLong(1, idAdresa);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                mp = new MeteoPodaci(odg.getTimestamp(3), odg.getTimestamp(4), odg.getFloat(5), odg.getFloat(7), odg.getString(6));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mp;
    }

    /**
     * Metoda za dohvaćanje svih meteoroloških podataka za pojedinu adresu iz
     * baze podataka.
     *
     * @param idAdresa - id adrese za koju se dohvaćaju svi meteorološki podaci.
     * @return lista tipa meteopodaci, sa svim meteorološkim podacima
     * adrese
     */
    public List<MeteoPodaci> dajSveMeteoPodatkeZaAdresu(long idAdresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_meteo where idadresa=? ORDER BY id DESC";
        List<MeteoPodaci> meteoPodaci = new ArrayList<MeteoPodaci>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setLong(1, idAdresa);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                MeteoPodaci mp = new MeteoPodaci(odg.getTimestamp(3), odg.getTimestamp(4), odg.getFloat(5), odg.getFloat(7), odg.getString(6));
                meteoPodaci.add(index, mp);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meteoPodaci;

    }

}
