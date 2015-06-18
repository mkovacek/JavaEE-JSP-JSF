/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "KORISNICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korisnici.findAll", query = "SELECT k FROM Korisnici k"),
    @NamedQuery(name = "Korisnici.findById", query = "SELECT k FROM Korisnici k WHERE k.id = :id"),
    @NamedQuery(name = "Korisnici.findByKorime", query = "SELECT k FROM Korisnici k WHERE k.korime = :korime"),
    @NamedQuery(name = "Korisnici.findByIme", query = "SELECT k FROM Korisnici k WHERE k.ime = :ime"),
    @NamedQuery(name = "Korisnici.findByPrezime", query = "SELECT k FROM Korisnici k WHERE k.prezime = :prezime"),
    @NamedQuery(name = "Korisnici.findByLozinka", query = "SELECT k FROM Korisnici k WHERE k.lozinka = :lozinka"),
    @NamedQuery(name = "Korisnici.findByEmailAdresa", query = "SELECT k FROM Korisnici k WHERE k.emailAdresa = :emailAdresa"),
    @NamedQuery(name = "Korisnici.findByVrsta", query = "SELECT k FROM Korisnici k WHERE k.vrsta = :vrsta"),
    @NamedQuery(name = "Korisnici.findByDatumKreiranja", query = "SELECT k FROM Korisnici k WHERE k.datumKreiranja = :datumKreiranja")})
public class Korisnici implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "KORIME")
    private String korime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "IME")
    private String ime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PREZIME")
    private String prezime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOZINKA")
    private String lozinka;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "EMAIL_ADRESA")
    private String emailAdresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VRSTA")
    private int vrsta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_KREIRANJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumKreiranja;

    public Korisnici() {
    }

    public Korisnici(Integer id) {
        this.id = id;
    }

    public Korisnici(Integer id, String korime, String ime, String prezime, String lozinka, String emailAdresa, int vrsta, Date datumKreiranja) {
        this.id = id;
        this.korime = korime;
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.emailAdresa = emailAdresa;
        this.vrsta = vrsta;
        this.datumKreiranja = datumKreiranja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorime() {
        return korime;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korisnici)) {
            return false;
        }
        Korisnici other = (Korisnici) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.mkovacek.ejb.eb.Korisnici[ id=" + id + " ]";
    }
    
}
