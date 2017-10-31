/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "estagio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estagio.findAll", query = "SELECT e FROM Estagio e"),
    @NamedQuery(name = "Estagio.findByIdestagio", query = "SELECT e FROM Estagio e WHERE e.idestagio = :idestagio"),
    @NamedQuery(name = "Estagio.findByTipoestagio", query = "SELECT e FROM Estagio e WHERE e.tipoestagio = :tipoestagio")})
public class Estagio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idestagio", nullable = false)
    private Integer idestagio;
    @Column(name = "tipoestagio", length = 2147483647)
    private String tipoestagio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estagioidestagio")
    private List<Trabalho> trabalhoList;

    public Estagio() {
    }

    public Estagio(Integer idestagio) {
        this.idestagio = idestagio;
    }

    public Integer getIdestagio() {
        return idestagio;
    }

    public void setIdestagio(Integer idestagio) {
        this.idestagio = idestagio;
    }

    public String getTipoestagio() {
        return tipoestagio;
    }

    public void setTipoestagio(String tipoestagio) {
        this.tipoestagio = tipoestagio;
    }

    @XmlTransient
    public List<Trabalho> getTrabalhoList() {
        return trabalhoList;
    }

    public void setTrabalhoList(List<Trabalho> trabalhoList) {
        this.trabalhoList = trabalhoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idestagio != null ? idestagio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estagio)) {
            return false;
        }
        Estagio other = (Estagio) object;
        if ((this.idestagio == null && other.idestagio != null) || (this.idestagio != null && !this.idestagio.equals(other.idestagio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Estagio[ idestagio=" + idestagio + " ]";
    }

}
