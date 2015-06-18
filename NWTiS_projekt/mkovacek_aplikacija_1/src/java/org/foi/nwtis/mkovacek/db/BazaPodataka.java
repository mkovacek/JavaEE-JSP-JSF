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
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Dnevnik;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.podaci.Lokacija;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.podaci.Transakcije;
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
    public void spremiAdresu(Adresa adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_adrese (adresa,latitude,longitude,korisnik) VALUES(?,?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, adresa.getAdresa());
                pstmt.setString(2, adresa.getGeoloc().getLatitude());
                pstmt.setString(3, adresa.getGeoloc().getLongitude());
                pstmt.setString(4, adresa.getKorisnik());
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za provjeru da li postoji adresa
     *
     * @param adresa
     * @return true/false
     */
    public boolean provjeraAdrese(String adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_adrese where adresa=?";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, adresa);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                System.out.println("postoji adresa: " + adresa);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ne postoji adresa: " + adresa);
        return false;
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka svih adresa iz baze
     * podataka.
     *
     * @return lista adresa sa svim geolokacijskim podacima adresa
     */
    public List<Adresa> dohvatiAdrese() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_adrese";
        List<Adresa> adrese = new ArrayList<Adresa>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Adresa adresa = new Adresa(Long.parseLong(odg.getString(1)), odg.getString(2), new Lokacija(odg.getString(3), odg.getString(4)), odg.getString(5), odg.getInt(6));
                adrese.add(index, adresa);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adrese;
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka korisnikovih adresa iz baze
     * podataka.
     *
     * @param korIme
     * @return lista adresa sa svim geolokacijskim podacima adresa
     */
    public List<Adresa> dohvatiKorisnikoveAdrese(String korIme) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_adrese where korisnik=?";
        List<Adresa> adrese = new ArrayList<Adresa>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, korIme);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Adresa adresa = new Adresa(Long.parseLong(odg.getString(1)), odg.getString(2), new Lokacija(odg.getString(3), odg.getString(4)), odg.getString(5), odg.getInt(6));
                adrese.add(index, adresa);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adrese;
    }

    /**
     * Metoda za provjeru da li se za adresu preuzimaju meteo podaci
     *
     * @param adresa
     */
    public void statusPreuzimanjaPodataka(String adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "UPDATE mkovacek_adrese SET statusPreuzimanja=? WHERE adresa=?";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setInt(1, 1);
                pstmt.setString(2, adresa);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za spremanje meteoroloških podataka za pojedinu adresu u bazu
     * podataka.
     *
     * @param adresa
     * @param mp objekt meteopodaci koji sadrži meteorološke podatke za adresu
     */
    public void spremiMeteoPodatke(String adresa, MeteoPodaci mp) {

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_meteo (adresa,datum,izlazaksunca,zalazaksunca,temperatura,vrijeme,tlakZraka) VALUES(?,?,?,?,?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, adresa);
                pstmt.setTimestamp(2, new Timestamp(mp.getLastUpdate().getTime()));
                pstmt.setTimestamp(3, new Timestamp(mp.getSunRise().getTime()));
                pstmt.setTimestamp(4, new Timestamp(mp.getSunSet().getTime()));
                pstmt.setString(5, mp.getTemperatureValue().toString());
                pstmt.setString(6, mp.getCloudsName());
                pstmt.setString(7, mp.getPressureValue().toString());
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za autentifikaciju korisnika.
     *
     * @param user
     * @param password
     * @return objekt Korisnik/null
     */
    public Korisnik autentifikacija(String user, String password) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_korisnici where korIme=? and lozinka=?";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, password);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                Korisnik korisnik = new Korisnik(odg.getInt(1), odg.getString(2), odg.getString(3), odg.getString(4), odg.getString(5), odg.getString(6), odg.getDouble(7), odg.getInt(8));
                return korisnik;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda za provjeru tipa korisnika.
     *
     * @param user
     * @return tip korisnika
     */
    public Korisnik provjeraKorisnika(String user) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_korisnici where korIme=?";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, user);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                Korisnik korisnik = new Korisnik();
                korisnik.setTip(odg.getInt("tip"));
                return korisnik;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda za postavljanje tipa korisnika.
     *
     * @param user
     * @param rola
     */
    public void postaviRolu(String user, int rola) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "UPDATE mkovacek_korisnici SET tip=? WHERE korIme=?";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setInt(1, rola);
                pstmt.setString(2, user);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za ažuriranje sredstava korisnika.
     *
     * @param user
     * @param sredstva
     */
    public void azurirajSredstva(String user, double sredstva) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "UPDATE mkovacek_korisnici SET racun=? WHERE korIme=?";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setDouble(1, sredstva);
                pstmt.setString(2, user);
                int response = pstmt.executeUpdate();
                System.out.println("update racuna: " + response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za dohvaćanje financijskih podataka korisnika.
     *
     * @param user
     */
    public Korisnik dajFinancije(String user) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT racun FROM mkovacek_korisnici where korIme=?";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, user);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                Korisnik korisnik = new Korisnik();
                korisnik.setRacun(odg.getDouble(1));
                return korisnik;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda za dohvaćanje financijskih transakcija korisnika.
     *
     * @param korIme
     * @return
     */
    public List<Transakcije> dohvatiKorisnikoveTransakcije(String korIme) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_transakcije where korisnik=? ORDER BY id DESC";
        List<Transakcije> transakcije = new ArrayList<Transakcije>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, korIme);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Transakcije tran = new Transakcije(odg.getString(2), odg.getString(3), odg.getDouble(4), odg.getDouble(5), odg.getDouble(6), odg.getTimestamp(7), odg.getString(8));
                transakcije.add(index, tran);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transakcije;
    }

    /**
     * Metoda za spremanje financijske transakcija korisnika.
     *
     * @param korIme
     * @param usluga
     * @param staroStanje
     * @param novoStanje
     * @param status
     * @param cijena
     */
    public void spremiTranskaciju(String korIme, String usluga, double staroStanje, double cijena, double novoStanje, String status) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_transakcije (korisnik,usluga,staroStanje,cijena,novoStanje,status) VALUES(?,?,?,?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, korIme);
                pstmt.setString(2, usluga);
                pstmt.setDouble(3, staroStanje);
                pstmt.setDouble(4, cijena);
                pstmt.setDouble(5, novoStanje);
                pstmt.setString(6, status);
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za pohranjivanje logova app.
     *
     * @param korIme
     * @param zahtjev
     * @param ipAdresa
     */
    public void log(String korIme, String zahtjev, String ipAdresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_dnevnik (korisnik,zahtjev,ipAdresa) VALUES(?,?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, korIme);
                pstmt.setString(2, zahtjev);
                pstmt.setString(3, ipAdresa);
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za pohranjivanje korisnika
     *
     * @param korIme
     * @param pass
     */
    public void spremiKorisnika(String korIme, String pass) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "INSERT INTO mkovacek_korisnici (korIme,lozinka) VALUES(?,?)";
        try {
            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka)) {
                PreparedStatement pstmt = veza.prepareStatement(sql);
                pstmt.setString(1, korIme);
                pstmt.setString(2, pass);
                pstmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za provjeru trenutnog broja korisnika
     *
     * @return broj
     */
    public int trenutniBrojKorisnika() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT COUNT(*) FROM mkovacek_korisnici";
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                return odg.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Metoda za dohvaćanje geolokacijskih podataka za pojedinu adresu iz baze
     * podataka.
     *
     * @param adresa
     * @return objekt tipa adresa, sa svim geolokacijskim podacima adrese
     */
    public Adresa dohvatiGeoPodatkeZaPojedinuAdresu(String adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_adrese where adresa=?";
        Adresa adr = null;
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, adresa);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                adr = new Adresa(Long.parseLong(odg.getString(1)), odg.getString(2), new Lokacija(odg.getString(3), odg.getString(4)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adr;
    }

    /**
     * Metoda za dohvaćanje zadnjih meteoroloških podataka za pojedinu adresu iz
     * baze podataka.
     *
     * @param adresa - adrese za koju se dohvaćaju posljednji meteorološki
     * podaci.
     * @return objekt tipa meteopodaci, sa svim zadnjim meteorološkim podacima
     * adrese
     */
    public MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(String adresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_meteo where adresa=? ORDER BY id DESC LIMIT 1";
        MeteoPodaci mp = null;
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, adresa);
            ResultSet odg = pstmt.executeQuery();
            if (odg.next()) {
                mp = new MeteoPodaci(odg.getFloat("temperatura"), odg.getFloat("tlakZraka"), odg.getString("vrijeme"), odg.getTimestamp("datumKreiranja"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mp;
    }

    /**
     * Metoda za dohvaćanje zadnjih N meteoroloških podataka za pojedinu adresu
     * iz baze podataka.
     *
     * @param adresa - adresa za koju se dohvaćaju posljednjih N meteorološki
     * podaci.
     * @param broj - broj podataka
     * @return objekt tipa meteopodaci, sa svim zadnjim meteorološkim podacima
     * adrese
     */
    public List<MeteoPodaci> dajZadnjihNMeteoPodatkaZaAdresu(String adresa, int broj) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_meteo where adresa=? ORDER BY id DESC LIMIT ?";
        List<MeteoPodaci> mpodaci = new ArrayList<MeteoPodaci>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, adresa);
            pstmt.setInt(2, broj);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                MeteoPodaci mp = new MeteoPodaci(odg.getTimestamp("izlazakSunca"), odg.getTimestamp("zalazakSunca"), odg.getFloat("temperatura"), odg.getFloat("tlakZraka"), odg.getString("vrijeme"), odg.getTimestamp("datumKreiranja"), odg.getString("adresa"));
                mpodaci.add(index, mp);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mpodaci;
    }

    /**
     * Metoda za dohvaćanje meteoroloških podataka za pojedinu adresu za
     * određeni vremenski interval
     *
     * @param adresa - adresa za koju se dohvaćaju posljednji meteorološki
     * podaci u intervalu
     * @param odDatum
     * @param doDatum
     * @return lista tipa meteopodaci, sa svim meteorološkim podacima adrese
     */
    public List<MeteoPodaci> dajOdDoMeteoPodatkeZaAdresu(String adresa, String odDatum, String doDatum) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_meteo where adresa=? and datumKreiranja BETWEEN ? and ?";
        List<MeteoPodaci> mpodaci = new ArrayList<MeteoPodaci>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setString(1, adresa);
            /* pstmt.setTimestamp(2, new Timestamp(odDatum.getTime()));
             pstmt.setTimestamp(3, new Timestamp(doDatum.getTime()));*/
            pstmt.setString(2, odDatum);
            pstmt.setString(3, doDatum);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                MeteoPodaci mp = new MeteoPodaci(odg.getTimestamp("izlazakSunca"), odg.getTimestamp("zalazakSunca"), odg.getFloat("temperatura"), odg.getFloat("tlakZraka"), odg.getString("vrijeme"), odg.getTimestamp("datumKreiranja"), odg.getString("adresa"));
                mpodaci.add(index, mp);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mpodaci;
    }

    /**
     * Metoda za dohvaćanje adresa s najviše prikupljenih meteo podataka
     *
     * @param brojAdresa
     * @return lista tipa Adresa
     */
    public List<Adresa> listaAdresaNajvisePreuzetihMeteoPodataka(int brojAdresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT adresa, COUNT(*) AS c FROM mkovacek_meteo GROUP BY adresa ORDER BY c DESC LIMIT ?";
        List<Adresa> adresa = new ArrayList<Adresa>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            pstmt.setInt(1, brojAdresa);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Adresa a = new Adresa(odg.getString("adresa"), odg.getInt("c"));
                adresa.add(index, a);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adresa;

    }

    /**
     * NE KORISTIM
     * Metoda za dohvaćanje svih meteoroloških podataka za pojedinu adresu iz
     * baze podataka.
     *
     * @param idAdresa - id adrese za koju se dohvaćaju svi meteorološki podaci.
     * @return lista tipa meteopodaci, sa svim meteorološkim podacima adrese
     */
    public List<MeteoPodaci> dajSveMeteoPodatkeZaAdresu(long idAdresa) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
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
//                MeteoPodaci mp = new MeteoPodaci(odg.getTimestamp(3), odg.getTimestamp(4), odg.getFloat(5), odg.getFloat(7), odg.getString(6));
//                meteoPodaci.add(index, mp);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meteoPodaci;
    }

    /**
     * Metoda za dohvaćanje logova
     *
     * @return
     */
    public List<Dnevnik> dohvatiDnevnik() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM mkovacek_dnevnik ORDER BY id DESC";
        List<Dnevnik> dnevnik = new ArrayList<Dnevnik>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(sql);
            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Dnevnik d = new Dnevnik(odg.getString(2), odg.getString(3), odg.getTimestamp(4), odg.getString(5));
                dnevnik.add(index, d);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dnevnik;
    }

    /**
     * Metoda za filtrirano dohvaćanje logova
     *
     * @return
     */
    public List<Dnevnik> filtrirajDnevnik(String zahtjev, String ipAdresa, String odDatum, String doDatum) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        String upit = "";
        int broj = 0;
        if (!zahtjev.equals("") && ipAdresa.equals("") && odDatum.equals("") && doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where zahtjev like ?";
            broj = 1;
        } else if (zahtjev.equals("") && ipAdresa.equals("") && !odDatum.equals("") && !doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where vrijeme BETWEEN ? and ?";
            broj = 2;
        } else if (!zahtjev.equals("") && ipAdresa.equals("") && !odDatum.equals("") && !doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where zahtjev like ? and vrijeme BETWEEN ? and ?";
            broj = 3;
        } else if (zahtjev.equals("") && !ipAdresa.equals("") && odDatum.equals("") && doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where ipAdresa like ?";
            broj = 4;
        } else if (zahtjev.equals("") && !ipAdresa.equals("") && !odDatum.equals("") && !doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where ipAdresa like ? and vrijeme BETWEEN ? and ?";
            broj = 5;
        } else if (!zahtjev.equals("") && !ipAdresa.equals("") && !odDatum.equals("") && !doDatum.equals("")) {
            upit = "SELECT * FROM mkovacek_dnevnik where zahtjev like ? and ipAdresa like ? and vrijeme BETWEEN ? and ?";
            broj = 6;
        }

        List<Dnevnik> dnevnik = new ArrayList<Dnevnik>();
        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            PreparedStatement pstmt = veza.prepareStatement(upit);
            if (broj == 1) {
                pstmt.setString(1, zahtjev);
            } else if (broj == 2) {
                pstmt.setString(1, odDatum);
                pstmt.setString(2, doDatum);
            } else if (broj == 3) {
                pstmt.setString(1, zahtjev);
                pstmt.setString(2, odDatum);
                pstmt.setString(3, doDatum);
            } else if (broj == 4) {
                pstmt.setString(1, ipAdresa);
            } else if (broj == 5) {
                pstmt.setString(1, ipAdresa);
                pstmt.setString(2, odDatum);
                pstmt.setString(3, doDatum);
            } else if (broj == 6) {
                pstmt.setString(1, zahtjev);
                pstmt.setString(2, ipAdresa);
                pstmt.setString(3, odDatum);
                pstmt.setString(4, doDatum);
            }

            ResultSet odg = pstmt.executeQuery();
            int index = 0;
            while (odg.next()) {
                Dnevnik d = new Dnevnik(odg.getString(2), odg.getString(3), odg.getTimestamp(4), odg.getString(5));
                dnevnik.add(index, d);
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dnevnik;
    }

}
