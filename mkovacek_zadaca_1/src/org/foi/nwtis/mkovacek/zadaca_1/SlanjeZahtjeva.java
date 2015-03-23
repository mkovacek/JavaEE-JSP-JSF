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
public class SlanjeZahtjeva extends Thread {

    private Konfiguracija konfig;
    private String server;
    private int port;

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        //TODO preuzmi korisnika iz parametara
        String korisnik = "mkovacek";

        while (true) {
            try {
                //TODO:ako nije s ponavljanjem onda prekid rada nakon 1.ciklusa
                socket = new Socket(server, port);
                os = socket.getOutputStream();
                is = socket.getInputStream();

                String komanda = "USER " + korisnik + "; TIME;";
                os.write(komanda.getBytes());
                os.flush();
                socket.shutdownOutput();
                
                StringBuilder sb = new StringBuilder();
                int j = 100;
                
                /*while((int znak=is.read())!=1){
                    sb.append((char) znak);
                }*/
                
                while (true) {
                    int znak = is.read();
                    if (znak == -1) {
                        break;
                    }
                    sb.append((char) znak);

                    //TODO ovo je privremeno
                    if (sb.length() == j) {
                        break;
                    }
                }
                System.out.println("Slanje zahtjeva: "+sb.toString()); //makni slanje zahtjeva

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
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }

            //ovo je privremeno (break)
            break;
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

}