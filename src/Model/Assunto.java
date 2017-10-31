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
@Table(name = "assunto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assunto.findAll", query = "SELECT a FROM Assunto a ORDER BY a.nome ASC"),
    @NamedQuery(name = "Assunto.findByIdassunto", query = "SELECT a FROM Assunto a WHERE a.idassunto = :idassunto ORDER BY a.nome ASC"),
    @NamedQuery(name = "Assunto.findByNome", query = "SELECT a FROM Assunto a WHERE a.nome = :nome ORDER BY a.nome ASC")})
public class Assunto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idassunto", nullable = false)
    private Integer idassunto;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assuntoidassunto")
    private List<Trabalho> trabalhoList;

    public Assunto() {
    }

    public Assunto(Integer idassunto) {
        this.idassunto = idassunto;
    }

    public Integer getIdassunto() {
        return idassunto;
    }

    public void setIdassunto(Integer idassunto) {
        this.idassunto = idassunto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        hash += (idassunto != null ? idassunto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assunto)) {
            return false;
        }
        Assunto other = (Assunto) object;
        if ((this.idassunto == null && other.idassunto != null) || (this.idassunto != null && !this.idassunto.equals(other.idassunto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Assunto[ idassunto=" + idassunto + " ]";
    }

}
