/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main klasa
 *
 * @author Matija Kovacek
 */
public class Zadaca_mkovacek_1 {

    /**
     * Metoda pokrece jedan od cetiri nacina rada programa
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();
        Zadaca_mkovacek_1 zadaca = new Zadaca_mkovacek_1();
        Matcher m = zadaca.provjeraParametara(p);
        if (m == null) {
            return;
        }
        if (m.group(1) != null) {
            try {
                ServerSustava server = new ServerSustava(p);
                server.pokreniServer();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_mkovacek_1.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (m.group(2) != null) {
            AdministratorSustava admin = null;
            try {
                admin = new AdministratorSustava(p);
                admin.pokreniAdmina();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_mkovacek_1.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (m.group(3) != null) {
            KlijentSustava klijent = null;
            try {
                klijent = new KlijentSustava(p);
                klijent.pokreniKlijenta();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_mkovacek_1.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (m.group(4) != null) {
            PregledSustava ps = null;
            try {
                ps = new PregledSustava(p);
                ps.pokreniPreglednik();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_mkovacek_1.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Parametri ne odgovaraju!");
            return;
        }
    }

    /**
     * Metoda provjerava ispravnost upisanog argumenta
     *
     * @param p - argument za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraParametara(String p) {
        String sintaksa = "(^-server.+)|(^-admin.+)|(^-user.+)|(^-show.+)";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
            return m;
        } else {
            System.out.println("Ne odgovara!");
            return null;
        }
    }

}
