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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mkovacek.konfiguracije.Konfiguracija;

/**
 *
 * @author Matija
 */
public class AdministratorSustava {

    protected Konfiguracija konfig;
    protected String parametri;
    protected Matcher mParametri;

    public AdministratorSustava(String parametri) throws Exception {
        this.parametri = parametri;
        this.mParametri = provjeraParametara(parametri);
        if (this.mParametri == null) {
            throw new Exception("ERROR 90: Parametri administratora ne odgovaraju!");
        }
    }

    public void pokreniAdmina() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        String server=this.mParametri.group(1);
        int port=Integer.parseInt(this.mParametri.group(2));
        String user=this.mParametri.group(3);
        String password=this.mParametri.group(4);
                
        String komanda="";
        if(this.mParametri.group(7)!=null){
            komanda=this.mParametri.group(7);
            komanda=komanda.toUpperCase();
        }

        try {
            socket = new Socket(server,port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            
            String zahtjev ="USER " + user + "; PASSWD "+password+"; "+komanda+";";
            System.out.println(zahtjev);
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

    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-admin\\s+-s +([^\\s]+)\\s+-port +(\\d{4})\\s+-u +([a-zA-Z0-9_-]+)\\s+-p +([a-zA-Z0-9-_#!]+)(\\s+(-(pause|start|stop|clean|stat|upload|download)))?$";
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
    
}
