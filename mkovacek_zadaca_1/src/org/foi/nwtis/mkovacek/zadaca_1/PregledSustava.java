/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Matija
 */
public class PregledSustava {

    protected String parametri;
    protected Matcher mParametri;

    /**
     * Konstruktor
     *
     * @param parametri - parametri show klijenta
     */
    public PregledSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("Parametri ne odgovaraju!");
        }
    }

    /**
     * Metoda provjerava ispravnost upisanog argumenta
     *
     * @param p - argument za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-show\\s+-s +([^\\s]+)$";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.out.println("Ne odgovara!");
            return null;
        }
    }

    /**
     * Metoda za pokretanje preglednika.
     *
     */
    public void pokreniPreglednik() {
        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka evidencije ne postoji!");
            return;
        }
        this.citanjeEvidencije(datoteka);
        return;
    }

    /**
     * Metoda za prikazivanje evidencije rada dretvi iz datoteke.
     *
     * @param datoteka - naziv datoteke koja sadrzi evidenciju
     */
    public void citanjeEvidencije(String datoteka) {
        SerijalizatorEvidencije.deserijalizator(datoteka);
        if (!Evidencija.getEvidencijaRada().isEmpty()) {
            for (Map.Entry<String, EvidencijaModel> entrySet : Evidencija.getEvidencijaRada().entrySet()) {
                System.out.println("Oznaka dretve: " + entrySet.getValue().getOznaka() + "\nPrviZahtjev: " + entrySet.getValue().getPrvizahtjev()
                        + "\nZadnji zahtjev: " + entrySet.getValue().getZadnjiZahtjev() + "\nUkupan broj zahtjeva: " + entrySet.getValue().getUkupanBrojZahtjeva()
                        + "\nNeuspjesan br. zahtjeva: " + entrySet.getValue().getNeuspjesanBrojZahtjeva() + "\nUkupno vrijeme rada: " + entrySet.getValue().getUkupnoVrijemeRada()
                        + "\nKorisnikovi zahtjevi: ");
                if (!entrySet.getValue().getZahtjevi().isEmpty()) {
                    for (EvidencijaModel.ZahtjevKorisnika item : entrySet.getValue().getZahtjevi()) {
                        System.out.println("\tIP adresa: " + item.getIpAdresa()
                                + "\n\tVrijeme: " + item.getVrijeme()
                                + "\n\tZahtjev: " + item.getZahtjev()
                                + "\n\tOdgovor: " + item.getOdgovor() + "\n");
                    }
                }
                System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°\n");
            }
        } else {
            System.out.println("Evidencija rad je prazna!");
        }
    }
}
