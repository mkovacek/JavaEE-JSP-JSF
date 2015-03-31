/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.util.regex.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matija
 */
public class PregledSustavaTest {
    
    public PregledSustavaTest() {
    }
    
  

    /**
     * Test of provjeraParametara method, of class PregledSustava.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara (show)");
        String p = "-show -s NWTIS_evidencija.bin";
        PregledSustava instance;
        try {
            instance = new PregledSustava(p);
            Matcher result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-show   -s   NWTIS_evidencija.bin";
            result = instance.provjeraParametara(p);
            assertNotNull(result);           
        } catch (Exception e) {
        }
        
    }

    
}
