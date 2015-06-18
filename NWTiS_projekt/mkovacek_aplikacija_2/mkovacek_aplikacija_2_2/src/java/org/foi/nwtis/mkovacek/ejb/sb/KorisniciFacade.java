/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.mkovacek.ejb.eb.Korisnici;

/**
 *
 * @author Matija
 */
@Stateless
public class KorisniciFacade extends AbstractFacade<Korisnici> {

    @PersistenceContext(unitName = "mkovacek_aplikacija_2_2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KorisniciFacade() {
        super(Korisnici.class);
    }

    /**
     * Metoda za provjeru postojanja korisnika u db
     *
     * @param korIme - naziv korisnik
     * @return 
     */
    public boolean provjeraKorisnika(String korIme) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Korisnici> korisnici = cq.from(Korisnici.class);
        cq.select(korisnici).where(cb.equal(korisnici.get("korime"), korIme));
        if (getEntityManager().createQuery(cq).getResultList().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metoda za autentifikaciju korisnika
     *
     * @param korIme 
     * @param lozinka
     * @return 
     */
    public Korisnici autentifikacija(String korIme, String lozinka) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Korisnici> korisnici = cq.from(Korisnici.class);
        cq.select(korisnici).where(cb.and(cb.equal(korisnici.get("korime"), korIme), cb.equal(korisnici.get("lozinka"), lozinka)));
        List<Korisnici> korisnik = (List<Korisnici>) getEntityManager().createQuery(cq).getResultList();
        if (!korisnik.isEmpty()) {
            return korisnik.get(0);
        }
        return null;
    }
}
