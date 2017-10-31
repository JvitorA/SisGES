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
@Table(name = "professordisponibilidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Professordisponibilidade.findAll", query = "SELECT p FROM Professordisponibilidade p"),
    @NamedQuery(name = "Professordisponibilidade.findByIdprofessordisponiblidade", query = "SELECT p FROM Professordisponibilidade p WHERE p.idprofessordisponiblidade = :idprofessordisponiblidade"),
    @NamedQuery(name = "Professordisponibilidade.findBySiape", query = "SELECT p FROM Professordisponibilidade p WHERE p.professorsiape.siape = :siape ORDER BY p.idprofessordisponiblidade"),
    @NamedQuery(name = "Professordisponibilidade.findByDiasemana", query = "SELECT p FROM Professordisponibilidade p WHERE p.diasemana = :diasemana"),
    @NamedQuery(name = "Professordisponibilidade.findByManha", query = "SELECT p FROM Professordisponibilidade p WHERE p.manha = :manha"),
    @NamedQuery(name = "Professordisponibilidade.findByTarde", query = "SELECT p FROM Professordisponibilidade p WHERE p.tarde = :tarde"),
    @NamedQuery(name = "Professordisponibilidade.findByNoite", query = "SELECT p FROM Professordisponibilidade p WHERE p.noite = :noite")})
public class Professordisponibilidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idprofessordisponiblidade", nullable = false)
    private Integer idprofessordisponiblidade;
    @Column(name = "diasemana", length = 2147483647)
    private String diasemana;
    @Column(name = "manha")
    private Boolean manha;
    @Column(name = "tarde")
    private Boolean tarde;
    @Column(name = "noite")
    private Boolean noite;
    @JoinColumn(name = "professorsiape", referencedColumnName = "siape", nullable = false)
    @ManyToOne(optional = false)
    private Professor professorsiape;

    public Professordisponibilidade() {
    }

    public Professordisponibilidade(Integer idprofessordisponiblidade) {
        this.idprofessordisponiblidade = idprofessordisponiblidade;
    }

    public Integer getIdprofessordisponiblidade() {
        return idprofessordisponiblidade;
    }

    public void setIdprofessordisponiblidade(Integer idprofessordisponiblidade) {
        this.idprofessordisponiblidade = idprofessordisponiblidade;
    }

    public String getDiasemana() {
        return diasemana;
    }

    public void setDiasemana(String diasemana) {
        this.diasemana = diasemana;
    }

    public Boolean getManha() {
        return manha;
    }

    public void setManha(Boolean manha) {
        this.manha = manha;
    }

    public Boolean getTarde() {
        return tarde;
    }

    public void setTarde(Boolean tarde) {
        this.tarde = tarde;
    }

    public Boolean getNoite() {
        return noite;
    }

    public void setNoite(Boolean noite) {
        this.noite = noite;
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
        hash += (idprofessordisponiblidade != null ? idprofessordisponiblidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Professordisponibilidade)) {
            return false;
        }
        Professordisponibilidade other = (Professordisponibilidade) object;
        if ((this.idprofessordisponiblidade == null && other.idprofessordisponiblidade != null) || (this.idprofessordisponiblidade != null && !this.idprofessordisponiblidade.equals(other.idprofessordisponiblidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Professordisponibilidade[ idprofessordisponiblidade=" + idprofessordisponiblidade + " ]";
    }

}
