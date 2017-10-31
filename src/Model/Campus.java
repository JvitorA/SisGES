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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "campus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campus.findAll", query = "SELECT c FROM Campus c"),
    @NamedQuery(name = "Campus.findByNomecampus", query = "SELECT c FROM Campus c WHERE c.nomecampus = :nomecampus"),
    @NamedQuery(name = "Campus.findByCep", query = "SELECT c FROM Campus c WHERE c.cep = :cep"),
    @NamedQuery(name = "Campus.findByCidade", query = "SELECT c FROM Campus c WHERE c.cidade = :cidade"),
    @NamedQuery(name = "Campus.findByEstado", query = "SELECT c FROM Campus c WHERE c.estado = :estado"),
    @NamedQuery(name = "Campus.findByEstadosigla", query = "SELECT c FROM Campus c WHERE c.estadosigla = :estadosigla"),
    @NamedQuery(name = "Campus.findByIdcampus", query = "SELECT c FROM Campus c WHERE c.idcampus = :idcampus")})
public class Campus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "nomecampus", nullable = false, length = 2147483647)
    private String nomecampus;
    @Column(name = "cep", length = 2147483647)
    private String cep;
    @Column(name = "cidade", length = 2147483647)
    private String cidade;
    @Column(name = "estado", length = 2147483647)
    private String estado;
    @Column(name = "estadosigla", length = 2147483647)
    private String estadosigla;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcampus", nullable = false)
    private Integer idcampus;

    public Campus() {
    }

    public Campus(Integer idcampus) {
        this.idcampus = idcampus;
    }

    public Campus(Integer idcampus, String nomecampus) {
        this.idcampus = idcampus;
        this.nomecampus = nomecampus;
    }

    public String getNomecampus() {
        return nomecampus;
    }

    public void setNomecampus(String nomecampus) {
        this.nomecampus = nomecampus;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadosigla() {
        return estadosigla;
    }

    public void setEstadosigla(String estadosigla) {
        this.estadosigla = estadosigla;
    }

    public Integer getIdcampus() {
        return idcampus;
    }

    public void setIdcampus(Integer idcampus) {
        this.idcampus = idcampus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcampus != null ? idcampus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campus)) {
            return false;
        }
        Campus other = (Campus) object;
        if ((this.idcampus == null && other.idcampus != null) || (this.idcampus != null && !this.idcampus.equals(other.idcampus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Campus[ idcampus=" + idcampus + " ]";
    }

}
