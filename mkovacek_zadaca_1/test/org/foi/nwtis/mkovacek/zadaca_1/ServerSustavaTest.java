/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author NWTiS_4
 */
public class ServerSustavaTest {

    public ServerSustavaTest() {
    }

    /**
     * Test of provjeraParametara method, of class ServerSustava.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara (Server)");
        String p = "-server -konf NWTIS_mkovacek_1.txt";
        ServerSustava instance;
        try {
            instance = new ServerSustava(p);
            Matcher result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-server -konf NWTIS_mkovacek_1.xml";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-server -konf NWTIS_mkovacek_1.txt -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-server -konf NWTIS_mkovacek_1.xml -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-server    -konf   NWTIS_mkovacek_1.txt    -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

            p = "-server    -konf   NWTIS_mkovacek_1.xml    -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);

        } catch (Exception ex) {
            Logger.getLogger(ServerSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
