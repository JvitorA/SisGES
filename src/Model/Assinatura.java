/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "assinatura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assinatura.findAll", query = "SELECT a FROM Assinatura a"),
    @NamedQuery(name = "Assinatura.findBySiapeProfessor", query = "SELECT a FROM Assinatura a WHERE a.professorsiape.siape = :siape"),
    @NamedQuery(name = "Assinatura.findByIdassinatura", query = "SELECT a FROM Assinatura a WHERE a.idassinatura = :idassinatura"),
    @NamedQuery(name = "Assinatura.findByAssinatura", query = "SELECT a FROM Assinatura a WHERE a.assinatura = :assinatura")})
public class Assinatura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idassinatura", nullable = false)
    private Integer idassinatura;
    @Column(name = "assinatura", length = 2147483647)
    private String assinatura;
    @JoinColumn(name = "professorsiape", referencedColumnName = "siape", nullable = false)
    @ManyToOne(optional = false)
    private Professor professorsiape;

    public Assinatura() {
    }

    public Assinatura(Integer idassinatura) {
        this.idassinatura = idassinatura;
    }

    public Integer getIdassinatura() {
        return idassinatura;
    }

    public void setIdassinatura(Integer idassinatura) {
        this.idassinatura = idassinatura;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public Professor getProfessorsiape() {
        return professorsiape;
    }

    public void setProfessorsiape(Professor professorsiape) {
        this.professorsiape = professorsiape;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idassinatura != null ? idassinatura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assinatura)) {
            return false;
        }
        Assinatura other = (Assinatura) object;
        if ((this.idassinatura == null && other.idassinatura != null) || (this.idassinatura != null && !this.idassinatura.equals(other.idassinatura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Assinatura[ idassinatura=" + idassinatura + " ]";
    }

}
