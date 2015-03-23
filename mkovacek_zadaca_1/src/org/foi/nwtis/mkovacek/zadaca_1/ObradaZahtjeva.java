/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija; //mkovacek

/**
 *
 * @author NWTiS_4
 */
public class ObradaZahtjeva extends Thread {

    public enum StanjeDretve {

        Slobodna, Zauzeta
    };

    private Konfiguracija konfig;
    private Socket socket;
    private StanjeDretve stanje;

    public ObradaZahtjeva(ThreadGroup group, String name) {
        super(group, name);
        this.stanje = StanjeDretve.Slobodna;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        Evidencija evidencija;
        String zahtjev = null;
        String odgovor = null;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            StringBuilder sb = new StringBuilder();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
                zahtjev=sb.toString();
            }
            System.out.println("Obrada zahtjeva: " + sb.toString()); //makni obrada zahtjeva
            os.write("OK".getBytes());
            os.flush();
            odgovor="OK";
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
        evidencija=new Evidencija(currentThread().getName(), zahtjev, odgovor);
        SerijalizatorEvidencije.evidencija.add(evidencija);
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
//encapsulate fields mijenja varijable u private (tipa iz public ili protected)

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setStanje(StanjeDretve stanje) {
        this.stanje = stanje;
    }

    public StanjeDretve getStanje() {
        return stanje;
    }

}
