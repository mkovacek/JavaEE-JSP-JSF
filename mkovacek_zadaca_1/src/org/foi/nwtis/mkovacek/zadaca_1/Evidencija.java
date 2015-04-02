/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Klasa Evidencija implementira Serializable. Sluzi za postavljanje i
 * dohvacanje evidencije rada dretvi.
 *
 * @author Matija Kovacek
 */
public class Evidencija implements Serializable {

    private static HashMap<String, EvidencijaModel> evidencijaRada = new HashMap<>();

    /**
     * Metoda dohvaca evidencije rada dretvi.
     *
     * @return (HashMap<String, EvidencijaModel>) evidenciju rada dretvi
     */
    public static HashMap<String, EvidencijaModel> getEvidencijaRada() {
        return evidencijaRada;
    }

    /**
     * Metoda postavlja evidenciju rada dretvi.
     *
     * @param evidencijaRada - evidencija rada dretvi.
     */
    public static void setEvidencijaRada(HashMap<String, EvidencijaModel> evidencijaRada) {
        Evidencija.evidencijaRada = evidencijaRada;
    }

}
