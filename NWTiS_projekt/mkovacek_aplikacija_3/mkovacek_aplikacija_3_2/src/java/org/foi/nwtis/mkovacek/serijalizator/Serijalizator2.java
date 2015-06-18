/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.serijalizator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.AdreseOM;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.EmailOM;

/**
 * Klasa SerijalizatorEvidencije extends Thread. Sluzi za za serijalizaciju
 * JMS poruka
 *
 * @author Matija Kovacek
 */
public class Serijalizator2 implements Serializable {

    private static Serijalizator2 instance = null;

    protected Serijalizator2() {
        // Exists only to defeat instantiation.
    }

    public static Serijalizator2 getInstance() {
        if (instance == null) {
            instance = new Serijalizator2();
        }
        return instance;
    }

    private List<EmailOM> listaEmail = new ArrayList<>();
    private List<AdreseOM> listaAdresa = new ArrayList<>();


    /**
     * Metoda sluzi serijalizaciju JMS email u datoteku.
     *
     * @param datoteka - naziv datoteke
     * @param lista
     */
    public void serijalizatorEmail(String datoteka, List<EmailOM> lista) {
        File f = new File(datoteka);
        if (!f.exists()) {
            System.err.println("file email ne postoji!");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("file email postoji!");
            if (lista.isEmpty()) {
                System.out.println("lista email je prazna..");
                return;
            } else {
                for (EmailOM lista1 : lista) {
                    System.out.println(lista1.getVrijemePocetak());
                }
                System.out.println("serijalizator email lista nije prazna");
                System.out.println(datoteka);
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = new FileOutputStream(datoteka);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(lista);
                    oos.close();

                } catch (FileNotFoundException ex) {
                    System.out.println("email:FileNotFoundException ex.");
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println("email:IOException ex.");
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("serijalizator email zapisano");
            }
        }

    }

    /**
     * Metoda sluzi serijalizaciju JMS adrese u datoteku.
     *
     * @param datoteka - naziv datoteke
     * @param lista
     */
    public  void serijalizatorAdresa(String datoteka, List<AdreseOM> lista) {

        File f = new File(datoteka);
        if (!f.exists()) {
            System.err.println("file adr ne postoji!");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("file adr postoji!");
            if (lista.isEmpty()) {
                System.out.println("lista adresa je prazna..");
                return;
            } else {
                System.out.println("serijalizator adresa, lista nije prazna");
                for (AdreseOM lista1 : lista) {
                    System.out.println(lista1.getAdresa());
                }
                System.out.println(datoteka);
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = new FileOutputStream(datoteka);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(lista);
                    oos.close();
                } catch (FileNotFoundException ex) {
                    System.out.println("adr:FileNotFoundException ex.");
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println("adr:IOException ex");
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("serijalizator adresa zapisano");

            }
        }

    }

    /**
     * Metoda sluzi deserijalizaciju JMS email iz datoteke.
     *
     * @param datoteka - naziv datoteke
     */
    public  void deserijalizatorEmail(String datoteka) {
        System.out.println("deserijalizator email");
        System.out.println("datoteka: " + datoteka);
        File f = new File(datoteka);
        if (!f.exists()) {
            System.err.println("file email ne postoji!");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("file email postoji!");

            List<EmailOM> evidencija = new ArrayList<EmailOM>();
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(datoteka);
                ois = new ObjectInputStream(fis);
                evidencija = (List<EmailOM>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!evidencija.isEmpty()) {
                getInstance().setListaEmail(evidencija);
                /*for (EmailOM evidencija1 : evidencija) {
                 System.out.println(evidencija1.getVrijemePocetak());
                 }*/
            } else {
                System.out.println("Dohvaćena evidencija emaila je prazna!");
            }
        }
    }

    /**
     * Metoda sluzi deserijalizaciju JMS adrese iz datoteke.
     *
     * @param datoteka - naziv datoteke
     */
    public  void deserijalizatorAdresa(String datoteka) {
        System.out.println("deserijalizator adresa");
        System.out.println("datoteka: " + datoteka);
        File f = new File(datoteka);
        if (!f.exists()) {
            System.err.println("file adresa ne postoji!");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("file adresa postoji!");

            List<AdreseOM> evidencija = new ArrayList<AdreseOM>();
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(datoteka);
                ois = new ObjectInputStream(fis);
                evidencija = (List<AdreseOM>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(Serijalizator2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!evidencija.isEmpty()) {
                getInstance().setListaAdresa(evidencija);
            } else {
                System.out.println("Dohvaćena evidencija adresa je prazna!");
            }

        }

    }

    public List<EmailOM> getListaEmail() {
        return listaEmail;
    }

    public void setListaEmail(List<EmailOM> listaEmail) {
        this.listaEmail = listaEmail;
    }

    public List<AdreseOM> getListaAdresa() {
        return listaAdresa;
    }

    public void setListaAdresa(List<AdreseOM> listaAdresa) {
        this.listaAdresa = listaAdresa;
    }

}
