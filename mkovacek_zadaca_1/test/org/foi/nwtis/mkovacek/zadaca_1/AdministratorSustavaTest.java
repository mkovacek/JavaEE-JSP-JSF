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
public class AdministratorSustavaTest {

    public AdministratorSustavaTest() {
    }

    /**
     * Test of provjeraParametara method, of class AdministratorSustava.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara (Admin)");
        String p = "-admin -s localhost -port 8000 -u pero  -p 654321 -pause";
        AdministratorSustava instance;
        try {
            instance = new AdministratorSustava(p);
            Matcher result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-admin   -s   localhost   -port   8000   -u   pe_-ro   -p   654321 -pause";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-admin -s localhost -port 8000 -u p_e-ro -p 6_5-4#3!21 -pause";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            
        } catch (Exception e) {
        }
    }

}
