/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "bancastatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bancastatus.findAll", query = "SELECT b FROM Bancastatus b"),
    @NamedQuery(name = "Bancastatus.findByIdstatus", query = "SELECT b FROM Bancastatus b WHERE b.idstatus = :idstatus"),
    @NamedQuery(name = "Bancastatus.findByTipostatus", query = "SELECT b FROM Bancastatus b WHERE b.tipostatus = :tipostatus")})
public class Bancastatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idstatus", nullable = false)
    private Integer idstatus;
    @Column(name = "tipostatus", length = 2147483647)
    private String tipostatus;
    @OneToMany(mappedBy = "statusidstatus")
    private List<Banca> bancaList;

    public Bancastatus() {
    }

    public Bancastatus(Integer idstatus) {
        this.idstatus = idstatus;
    }

    public Integer getIdstatus() {
        return idstatus;
    }

    public void setIdstatus(Integer idstatus) {
        this.idstatus = idstatus;
    }

    public String getTipostatus() {
        return tipostatus;
    }

    public void setTipostatus(String tipostatus) {
        this.tipostatus = tipostatus;
    }

    @XmlTransient
    public List<Banca> getBancaList() {
        return bancaList;
    }

    public void setBancaList(List<Banca> bancaList) {
        this.bancaList = bancaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idstatus != null ? idstatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bancastatus)) {
            return false;
        }
        Bancastatus other = (Bancastatus) object;
        if ((this.idstatus == null && other.idstatus != null) || (this.idstatus != null && !this.idstatus.equals(other.idstatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Bancastatus[ idstatus=" + idstatus + " ]";
    }

}
