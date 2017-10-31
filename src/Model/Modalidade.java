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
@Table(name = "modalidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modalidade.findAll", query = "SELECT m FROM Modalidade m ORDER BY m.nomemodalidade ASC"),
    @NamedQuery(name = "Modalidade.findByIdmodalidade", query = "SELECT m FROM Modalidade m WHERE m.idmodalidade = :idmodalidade ORDER BY m.nomemodalidade ASC"),
    @NamedQuery(name = "Modalidade.findByNomemodalidade", query = "SELECT m FROM Modalidade m WHERE lower(m.nomemodalidade) = lower(:nomemodalidade) ORDER BY m.nomemodalidade ASC")})
public class Modalidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmodalidade", nullable = false)
    private Integer idmodalidade;
    @Column(name = "nomemodalidade", length = 2147483647)
    private String nomemodalidade;
    @OneToMany(mappedBy = "modalidadeidmodalidade")
    private List<Trabalho> trabalhoList;

    public Modalidade() {
    }

    public Modalidade(Integer idmodalidade) {
        this.idmodalidade = idmodalidade;
    }

    public Integer getIdmodalidade() {
        return idmodalidade;
    }

    public void setIdmodalidade(Integer idmodalidade) {
        this.idmodalidade = idmodalidade;
    }

    public String getNomemodalidade() {
        return nomemodalidade;
    }

    public void setNomemodalidade(String nomemodalidade) {
        this.nomemodalidade = nomemodalidade;
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
        hash += (idmodalidade != null ? idmodalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modalidade)) {
            return false;
        }
        Modalidade other = (Modalidade) object;
        if ((this.idmodalidade == null && other.idmodalidade != null) || (this.idmodalidade != null && !this.idmodalidade.equals(other.idmodalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Modalidade[ idmodalidade=" + idmodalidade + " ]";
    }

}
