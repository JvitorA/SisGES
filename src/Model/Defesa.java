/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "defesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Defesa.findAll", query = "SELECT d FROM Defesa d"),
    @NamedQuery(name = "Defesa.findByIdTrabalho", query = "SELECT d FROM Defesa d WHERE d.trabalhoidtrabalho.idtrabalho = :idtrabalho"),
    @NamedQuery(name = "Defesa.findByIddefesa", query = "SELECT d FROM Defesa d WHERE d.iddefesa = :iddefesa"),
    @NamedQuery(name = "Defesa.findByStatusdefesa", query = "SELECT d FROM Defesa d WHERE d.statusdefesa = :statusdefesa"),
    @NamedQuery(name = "Defesa.findByMindatadefesa", query = "SELECT d FROM Defesa d WHERE d.mindatadefesa = :mindatadefesa"),
    @NamedQuery(name = "Defesa.findByMaxdatadefesa", query = "SELECT d FROM Defesa d WHERE d.maxdatadefesa = :maxdatadefesa")})
public class Defesa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddefesa", nullable = false)
    private Integer iddefesa;
    @Column(name = "statusdefesa")
    private Boolean statusdefesa;
    @Column(name = "mindatadefesa")
    @Temporal(TemporalType.DATE)
    private Date mindatadefesa;
    @Column(name = "maxdatadefesa")
    @Temporal(TemporalType.DATE)
    private Date maxdatadefesa;
    @Column(name = "matriculadatadefesa")
    @Temporal(TemporalType.DATE)
    private Date matriculadatadefesa;
    @JoinColumn(name = "trabalhoidtrabalho", referencedColumnName = "idtrabalho", nullable = false)
    @ManyToOne(optional = false)
    private Trabalho trabalhoidtrabalho;

    public Defesa() {
        this.statusdefesa = false;
    }

    public Defesa(Integer iddefesa) {
        this.iddefesa = iddefesa;
    }

    public Integer getIddefesa() {
        return iddefesa;
    }

    public void setIddefesa(Integer iddefesa) {
        this.iddefesa = iddefesa;
    }

    public Boolean getStatusdefesa() {
        return statusdefesa;
    }

    public void setStatusdefesa(Boolean statusdefesa) {
        this.statusdefesa = statusdefesa;
    }

    public Date getMindatadefesa() {
        return mindatadefesa;
    }

    public void setMindatadefesa(Date mindatadefesa) {
        this.mindatadefesa = mindatadefesa;
    }

    public Date getMaxdatadefesa() {
        return maxdatadefesa;
    }

    public void setMaxdatadefesa(Date maxdatadefesa) {
        this.maxdatadefesa = maxdatadefesa;
    }

    public Trabalho getTrabalhoidtrabalho() {
        return trabalhoidtrabalho;
    }

    public void setTrabalhoidtrabalho(Trabalho trabalhoidtrabalho) {
        this.trabalhoidtrabalho = trabalhoidtrabalho;
    }

    public Date getMatriculadatadefesa() {
        return matriculadatadefesa;
    }

    public void setMatriculadatadefesa(Date matriculadatadefesa) {
        this.matriculadatadefesa = matriculadatadefesa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddefesa != null ? iddefesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Defesa)) {
            return false;
        }
        Defesa other = (Defesa) object;
        if ((this.iddefesa == null && other.iddefesa != null) || (this.iddefesa != null && !this.iddefesa.equals(other.iddefesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Defesa[ iddefesa=" + iddefesa + " ]";
    }

}
