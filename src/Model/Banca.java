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
@Table(name = "banca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Banca.findAll", query = "SELECT b FROM Banca b"),
    @NamedQuery(name = "Banca.findAllByOrientador", query = "SELECT b FROM Banca b WHERE b.trabalhoidtrabalho.professorsiape.siape = :siape and b.databanca between :dtinicio and :dtfinal"),
    @NamedQuery(name = "Banca.findByIdTrabalho", query = "SELECT b FROM Banca b WHERE b.trabalhoidtrabalho.idtrabalho = :idtrabalho"),
    @NamedQuery(name = "Banca.findByIdbanca", query = "SELECT b FROM Banca b WHERE b.idbanca = :idbanca"),
    @NamedQuery(name = "Banca.findByLocalbanca", query = "SELECT b FROM Banca b WHERE b.localbanca = :localbanca"),
    @NamedQuery(name = "Banca.findByDatabanca", query = "SELECT b FROM Banca b WHERE b.databanca = :databanca"),
    @NamedQuery(name = "Banca.findByHorario", query = "SELECT b FROM Banca b WHERE b.horario = :horario")})
public class Banca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbanca", nullable = false)
    private Integer idbanca;
    @Column(name = "localbanca", length = 2147483647)
    private String localbanca;
    @Column(name = "databanca")
    @Temporal(TemporalType.DATE)
    private Date databanca;

    @Column(name = "datafinalizacao")
    @Temporal(TemporalType.DATE)
    private Date datafinalizacao;

    @Column(name = "horario")
    @Temporal(TemporalType.TIME)
    private Date horario;
    @JoinColumn(name = "trabalhoidtrabalho", referencedColumnName = "idtrabalho", nullable = false)
    @ManyToOne(optional = false)
    private Trabalho trabalhoidtrabalho;
    @JoinColumn(name = "statusidstatus", referencedColumnName = "idstatus")
    @ManyToOne
    private Bancastatus statusidstatus;

    public Banca() {
        this.localbanca = "";
        this.trabalhoidtrabalho = new Trabalho();
        this.statusidstatus = new Bancastatus();
    }

    public Banca(Integer idbanca) {
        this.idbanca = idbanca;
    }

    public Integer getIdbanca() {
        return idbanca;
    }

    public void setIdbanca(Integer idbanca) {
        this.idbanca = idbanca;
    }

    public String getLocalbanca() {
        return localbanca;
    }

    public void setLocalbanca(String localbanca) {
        this.localbanca = localbanca;
    }

    public Date getDatabanca() {
        return databanca;
    }

    public void setDatabanca(Date databanca) {
        this.databanca = databanca;
    }

    public Date getDatafinalizacao() {
        return datafinalizacao;
    }

    public void setDatafinalizacao(Date datafinalizacao) {
        this.datafinalizacao = datafinalizacao;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public Trabalho getTrabalhoidtrabalho() {
        return trabalhoidtrabalho;
    }

    public void setTrabalhoidtrabalho(Trabalho trabalhoidtrabalho) {
        this.trabalhoidtrabalho = trabalhoidtrabalho;
    }

    public Bancastatus getStatusidstatus() {
        return statusidstatus;
    }

    public void setStatusidstatus(Bancastatus statusidstatus) {
        this.statusidstatus = statusidstatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbanca != null ? idbanca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banca)) {
            return false;
        }
        Banca other = (Banca) object;
        if ((this.idbanca == null && other.idbanca != null) || (this.idbanca != null && !this.idbanca.equals(other.idbanca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Banca[ idbanca=" + idbanca + " ]";
    }

}
