/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author NWTiS_4
 */
public class Zadaca_mkovacek_1Test {
    
    public Zadaca_mkovacek_1Test() {
    }

    /**
     * Test of main method, of class Zadaca_mkovacek_1.
     */
    @Ignore
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Zadaca_mkovacek_1.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provjeraParametara method, of class Zadaca_mkovacek_1.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara");
        String p = "-pero";
        Zadaca_mkovacek_1 instance = new Zadaca_mkovacek_1();
        Matcher expResult = null;
        Matcher result = instance.provjeraParametara(p);
        assertNull(result);
        p="-server";
        result = instance.provjeraParametara(p);
        assertNull(result);
        p="-server -konf NWTIS_mkovacek_1.txt";
        result = instance.provjeraParametara(p);
        assertNotNull(result);
        p="-admin -konf NWTIS_mkovacek_1.txt";
        result = instance.provjeraParametara(p);
        assertNotNull(result);
    }
    
}
