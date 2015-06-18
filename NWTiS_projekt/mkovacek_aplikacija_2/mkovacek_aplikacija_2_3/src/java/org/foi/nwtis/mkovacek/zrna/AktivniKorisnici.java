/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;

/**
 * ManagedBean za praćenje aktivnih korisnika
 * @author Matija
 */
@ManagedBean
@ApplicationScoped
public class AktivniKorisnici implements HttpSessionBindingListener {

    public static List<Korisnici> aktivniKorisnici;
    public static Korisnici korisnik;

    public AktivniKorisnici() {
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {

        AktivniKorisnici korisnik = (AktivniKorisnici) event.getValue();
        System.out.println("dodan je korisnik: " + korisnik.getKorisnik().getKorime());
        if (aktivniKorisnici == null) {
            aktivniKorisnici = new ArrayList<>();
        }
        aktivniKorisnici.add(korisnik.getKorisnik());
        System.out.println("Svi aktivni korisnici:\n");
        for (Korisnici aktivniKorisnici1 : aktivniKorisnici) {            
            System.out.println(aktivniKorisnici1.getKorime());
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        AktivniKorisnici korisnik = (AktivniKorisnici) event.getValue();
        System.out.println("izbrisan je korisnik: " + korisnik.getKorisnik().getKorime());
        if (aktivniKorisnici == null) {
            aktivniKorisnici = new ArrayList<>();
        }
        aktivniKorisnici.remove(korisnik.getKorisnik());
        System.out.println("Svi aktivni korisnici:\n");
        for (Korisnici aktivniKorisnici1 : aktivniKorisnici) {            
            System.out.println(aktivniKorisnici1.getKorime());
        }
    }

    public List<Korisnici> getAktivniKorisnici() {
        return aktivniKorisnici;
    }

    public void setAktivniKorisnici(List<Korisnici> aktivniKorisnici) {
        AktivniKorisnici.aktivniKorisnici = aktivniKorisnici;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    public Korisnici getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnici korisnik) {
        AktivniKorisnici.korisnik = korisnik;
    }

}
