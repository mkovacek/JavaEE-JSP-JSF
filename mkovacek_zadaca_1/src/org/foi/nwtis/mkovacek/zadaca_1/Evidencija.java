/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Matija
 */
public class Evidencija implements Serializable{
    
    private static HashMap<String, EvidencijaModel> evidencijaRada=new HashMap<>();

    public static HashMap<String, EvidencijaModel> getEvidencijaRada() {
        return evidencijaRada;
    }

    public static void setEvidencijaRada(HashMap<String, EvidencijaModel> evidencijaRada) {
       // this.evidencijaRada = evidencijaRada;
        Evidencija.evidencijaRada = evidencijaRada;
    }
          
}
