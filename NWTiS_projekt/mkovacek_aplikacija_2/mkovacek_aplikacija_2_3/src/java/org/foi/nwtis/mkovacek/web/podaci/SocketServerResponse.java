/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.podaci;

/**
 *
 * @author Matija
 */
public class SocketServerResponse {
    public static String response;

    public SocketServerResponse() {
    }

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        SocketServerResponse.response = response;
    }
    
    
}
