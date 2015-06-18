/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.mkovacek.ejb.eb.Dnevnik;

/**
 *
 * @author Matija
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {

    @PersistenceContext(unitName = "mkovacek_aplikacija_2_2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DnevnikFacade() {
        super(Dnevnik.class);
    }

    /**
     * Metoda za pretraživanje tbl Dnevnik prema različitim kriterijima. Metoda
     * filtrira, dohvaća zapise koji odgovaraju unesenim kriterijima.
     *
     * @param korisnik - naziv korisnik
     * @param ipAdresa - ip adresa
     * @param trajanje - trajanje
     * @param status - status
     * @param odDatum - od
     * @param doDatum - do
     * @return List<Dnevnik> - popis zapisa koji odgovaraju zadnim kriterijima
     */
    public List<Dnevnik> filter(String korisnik, String ipAdresa, String trajanje, String status, String odDatum, String doDatum, String zahtjev) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Dnevnik> dnevnik = cq.from(Dnevnik.class);

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (!korisnik.equals("")) {
            predicates.add(cb.like(dnevnik.<String>get("korisnik"), korisnik));
        }
        if (!ipAdresa.equals("")) {
            predicates.add(cb.like(dnevnik.<String>get("ipadresa"), ipAdresa));
        }
        if (!trajanje.equals("")) {
            predicates.add(cb.equal(dnevnik.<String>get("trajanje"), trajanje));
        }
        if (!status.equals("")) {
            predicates.add(cb.equal(dnevnik.<String>get("status"), status));
        }
        if (!odDatum.equals("") && !doDatum.equals("")) {
            predicates.add(cb.between(dnevnik.<String>get("vrijeme"), odDatum, doDatum));
        }
        if (!zahtjev.equals("")) {
            predicates.add(cb.like(dnevnik.<String>get("zahtjev"), zahtjev));
        }

        cq.select(dnevnik).where(predicates.toArray(new Predicate[]{}));
        return getEntityManager().createQuery(cq).getResultList();

    }
}
