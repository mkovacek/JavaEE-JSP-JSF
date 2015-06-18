/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.mkovacek.ejb.jms.objectMessage.AdreseOM;

/**
 *
 * @author Matija
 */
public class AdreseMessages {

    public Message createJMSMessageForjmsNWTiS_mkovacek_2(Session session, AdreseOM messageData) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(messageData);
        return message;
    }

    public void sendJMSMessageToNWTiS_mkovacek_2(AdreseOM messageData) throws JMSException, NamingException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("jms/NWTiS_QF_mkovacek_2");
        Connection conn = null;
        Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("jms/NWTiS_mkovacek_2");
            MessageProducer mp = s.createProducer(destination);
            mp.send(createJMSMessageForjmsNWTiS_mkovacek_2(s, messageData));

            conn.close();
            mp.close();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
