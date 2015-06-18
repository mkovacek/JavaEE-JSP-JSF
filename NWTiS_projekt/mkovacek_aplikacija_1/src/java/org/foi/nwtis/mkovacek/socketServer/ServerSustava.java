    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;

/**
 * Klasa koja pokreće threadove za obradu zahtjeva socket servera
 *
 * @author Matija
 */
public class ServerSustava extends Thread {

    public static boolean stop = false;
    public static boolean pauza = false;
    private ObradaZahtjeva oz;

    @Override
    /**
     * Metoda osluškuje socket server zahtjeva i šalje ih na obradu
     *
     * @author Matija
     */
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("port")));
            while (!stop) {
                System.out.println("pokrenuto!");
                Socket socket = ss.accept();
                System.out.println("novi serversocket zahtjev!");
                oz = new ObradaZahtjeva();
                oz.setSocket(socket);
                oz.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean isStop() {
        return stop;
    }

    public static void setStop(boolean stop) {
        ServerSustava.stop = stop;
    }

    public static void setPauza(boolean pauza) {
        ServerSustava.pauza = pauza;
    }

    public static boolean isPauza() {
        return pauza;
    }

}
