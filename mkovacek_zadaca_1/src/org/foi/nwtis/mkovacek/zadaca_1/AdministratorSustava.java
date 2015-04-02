/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;

/**
 * Klasa AdministratorSustava sluzi za slanje administratorskih zahtjeva serveru
 * i obradu odgovora od servera.
 *
 * @author Matija Kovacek
 */
public class AdministratorSustava {

    protected Konfiguracija konfig;
    protected String parametri;
    protected Matcher mParametri;
    private String velicinaDatoteke;

    /**
     * Konstruktor
     *
     * @param parametri - parametri administratora
     */
    public AdministratorSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("ERROR 90: Parametri administratora ne odgovaraju!");
        }
    }

    /**
     * Metoda za pokretanje administratora. Sluzi za slanje administratorskih
     * zahtjeva serveru i obradu odgovora od servera.
     *
     */
    public void pokreniAdmina() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        String server = this.mParametri.group(1);
        int port = Integer.parseInt(this.mParametri.group(2));
        String user = this.mParametri.group(3);
        String password = this.mParametri.group(4);

        /*for (int i = 1; i <=this.mParametri.groupCount(); i++) {
         System.out.println(i+" :"+this.mParametri.group(i));
         }*/
        String komanda = "";
        String zahtjev = "";
        if (this.mParametri.group(7) != null) {
            komanda = this.mParametri.group(7);
            komanda = komanda.toUpperCase();
            if (komanda.startsWith("UPLOAD")) {
                komanda = "UPLOAD";
                String nazivDatoteke = this.mParametri.group(8);
                if (!provjeraDatoteke(nazivDatoteke)) {
                    System.out.println("Datoteka za upload ne postoji!");
                    return;
                }
                zahtjev = "USER " + user + "; PASSWD " + password + "; " + komanda + " " + getVelicinaDatoteke() + ";\r\n" + getSadrzajDatoteke(nazivDatoteke);
            } else if (komanda.startsWith("DOWNLOAD")) {
                komanda = "DOWNLOAD";
                String nazivDatoteke = this.mParametri.group(10);
                if (provjeraDatoteke(nazivDatoteke)) {
                    System.out.println("Datoteka za download postoji!");
                    return;
                }
                zahtjev = "USER " + user + "; PASSWD " + password + "; " + komanda + ";";
            } else {
                zahtjev = "USER " + user + "; PASSWD " + password + "; " + komanda + ";";
            }

        }

        try {
            socket = new Socket(server, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(zahtjev.getBytes());
            os.flush();
            socket.shutdownOutput();

            StringBuilder sb = new StringBuilder();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            String odgovor = sb.toString();
            System.out.println(odgovor);
            if (odgovor.startsWith("DATA")) {
                String[] primljeniSadrzaj = odgovor.split(";\r\n");
                String[] primljenaVelicina = primljeniSadrzaj[0].split(" ");
                if (!Integer.toString(primljeniSadrzaj[1].length()).equals(primljenaVelicina[1])) {
                    String nazivDatoteke = ObradaZahtjeva.getNazivKonfiguracijskeDatoteke();
                    System.out.println(nazivDatoteke);
                    if (this.obrisiDatoteku(nazivDatoteke)) {
                        System.out.println("Datoteka je obrisna!");
                    }
                    System.out.println("Problem kod brisanja datoteke!");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (os != null) {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda provjerava ispravnost upisanog argumenta
     *
     * @param p - argument za provjeru
     * @return matcher ili null
     */
    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-admin\\s+-s +([^\\s]+)\\s+-port +(\\d{4})\\s+-u +([a-zA-Z0-9_-]+)\\s+-p +([a-zA-Z0-9-_#!]+)(\\s+(-(pause|start|stop|clean|stat|upload +([^\\s]+\\.(?i)(xml|txt))|download +([^\\s]+\\.(?i)(xml|txt)))))?$";
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
     * Metoda provjerava da li postoji datoteka.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return true ili false
     */
    public boolean provjeraDatoteke(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        if (!dat.exists()) {
            return false;
        }
        this.setVelicinaDatoteke(Integer.toString((int) dat.length()));
        return true;
    }

    /**
     * Metoda za dohvacanje sadrzaja iz datoteke.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return (string) sadrzaj
     */
    public String getSadrzajDatoteke(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = new FileInputStream(dat);
            sb = new StringBuilder();
            while (true) {
                int znak = fis.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    /**
     * Metoda za brisanje datoteke.
     *
     * @param nazivDatoteke - naziv datoteke
     * @return true ili false
     */
    public boolean obrisiDatoteku(String nazivDatoteke) {
        File dat = new File(nazivDatoteke);
        if (dat.exists()) {
            System.gc();
            return dat.delete();
        }
        return false;
    }

    /**
     * Metoda dohvaca velicinu datoteke.
     *
     * @return (String) velicina datoteke
     */
    public String getVelicinaDatoteke() {
        return velicinaDatoteke;
    }

    /**
     * Metoda postavlja velicinu datoteke.
     *
     * @param velicinaDatoteke - velicina datoteke
     */
    public void setVelicinaDatoteke(String velicinaDatoteke) {
        this.velicinaDatoteke = velicinaDatoteke;
    }

}
