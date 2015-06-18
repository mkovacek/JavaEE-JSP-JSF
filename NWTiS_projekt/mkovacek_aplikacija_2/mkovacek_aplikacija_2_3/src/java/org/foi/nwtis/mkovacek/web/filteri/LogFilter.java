/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.web.filteri;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mkovacek.ejb.eb.Dnevnik;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;
import org.foi.nwtis.mkovacek.ejb.sb.DnevnikFacade;


/**
 *
 * @author Matija
 */
@WebFilter(filterName = "LogFilter", urlPatterns = {"/*"})
public class LogFilter implements Filter {
    DnevnikFacade dnevnikFacade = lookupDnevnikFacadeBean();

    private static final boolean debug = true;
    private FilterConfig filterConfig = null;
    
    private long pocetak;
    private long kraj;
    
    public LogFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("LogFilter:DoBeforeProcessing");
        }
        pocetak = System.currentTimeMillis();
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("LogFilter:DoAfterProcessing");
        }

    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            log("LogFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        HttpServletRequest hsr = (HttpServletRequest) request;
        HttpSession hs = hsr.getSession(false);
        String zahtjev = hsr.getRequestURI();
        String ipAdresa=request.getRemoteAddr();
        Dnevnik dnevnik=new Dnevnik();
        dnevnik.setIpadresa(ipAdresa);
        dnevnik.setStatus(200);
        dnevnik.setUrl(zahtjev);
        dnevnik.setVrijeme(new Date());
        dnevnik.setZahtjev(zahtjev);
        
        if (hs == null || hs.getAttribute("korisnik") == null) {
            kraj = System.currentTimeMillis() - pocetak;
            dnevnik.setTrajanje((int) kraj);
            dnevnik.setKorisnik("nepoznati");
        } else {
            Korisnici user=new Korisnici();
            user=(Korisnici) hs.getAttribute("korisnik");
            kraj = System.currentTimeMillis() - pocetak;
            dnevnik.setTrajanje((int) kraj);
            dnevnik.setKorisnik(user.getKorime());
        }
        dnevnikFacade.create(dnevnik);
        
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
	    // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }
        
        doAfterProcessing(request, response);

	// If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("LogFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("LogFilter()");
        }
        StringBuffer sb = new StringBuffer("LogFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }

    private DnevnikFacade lookupDnevnikFacadeBean() {
        try {
            Context c = new InitialContext();
            return (DnevnikFacade) c.lookup("java:global/mkovacek_aplikacija_2/mkovacek_aplikacija_2_2/DnevnikFacade!org.foi.nwtis.mkovacek.ejb.sb.DnevnikFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
