/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.socket.klijent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Klasa za slanje zahtjeva prema socket serveru
 *
 * @author Matija
 */
public class SlanjeZahtjeva {

    private String server = SlusacAplikacije.konfig.dajPostavku("socketServer");
    private int port = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("socketPort"));
    private String zahtjev;
    private String odgovor;

    public SlanjeZahtjeva(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    /**
     * Metoda za slanje zahtjeva prema socket serveru
     *
     */
    public String posaljiZahtjev() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

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
            System.out.println(sb.toString());
            odgovor = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (os != null) {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return odgovor;

    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }

}
