/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matija
 */
public class KlijentSustavaTest {

    public KlijentSustavaTest() {
    }

    /**
     * Test of provjeraParametara method, of class KlijentSustava.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara (Klijent)");
        String p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt";
        KlijentSustava instance;
        try {
            instance = new KlijentSustava(p);
            Matcher result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.xml";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-user   -s   localhost  -port  8000  -u  mkovacek  -konf  NWTiS_konfiguracija.txt";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-user -s localhost -port 8000 -u MKOVACEK -konf NWTiS_konfiguracija.txt";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u Mk0v4c3_-K -konf NWTiS_konfiguracija.txt -cekaj 1";
            result = instance.provjeraParametara(p);
            assertNotNull(result); 
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -cekaj  77";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -multi";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
                        
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -ponavljaj  88";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -ponavljaj  88";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -cekaj 20 -multi";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -cekaj 20 -ponavljaj 5";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -multi -ponavljaj 10";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
            p = "-user -s localhost -port 8000 -u mkovacek -konf NWTiS_konfiguracija.txt -cekaj 2 -multi -ponavljaj 3";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
           
        } catch (Exception e) {
        }

    }

}
