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
@Table(name = "convidado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convidado.findAll", query = "SELECT c FROM Convidado c"),
    @NamedQuery(name = "Convidado.findByIdbanca", query = "SELECT c FROM Convidado c WHERE c.bancaidbanca.idbanca = :idbanca"),
    @NamedQuery(name = "Convidado.findByIdbancaConvidadoConfirmado", query = "SELECT c FROM Convidado c WHERE c.bancaidbanca.idbanca = :idbanca and c.confirmado=true"),
    @NamedQuery(name = "Convidado.findByIdconvidado", query = "SELECT c FROM Convidado c WHERE c.idconvidado = :idconvidado"),
    @NamedQuery(name = "Convidado.findFromBancaBySiape", query = "SELECT c FROM Convidado c WHERE c.bancaidbanca.idbanca = :idbanca and c.professorsiape.siape = :siape"),
    @NamedQuery(name = "Convidado.findFromBancaBySiapeAndConfirmado", query = "SELECT c FROM Convidado c WHERE c.professorsiape.siape = :siape and c.confirmado=true ORDER BY c.bancaidbanca.databanca ASC, c.bancaidbanca.statusidstatus.idstatus DESC"),
    @NamedQuery(name = "Convidado.findFromBancaBySiapeAndConfirmadoInPeriodo", query = "SELECT c FROM Convidado c WHERE c.professorsiape.siape = :siape and c.confirmado=true and c.bancaidbanca.databanca between :dtinicio and :dtfinal ORDER BY c.bancaidbanca.databanca ASC, c.bancaidbanca.statusidstatus.idstatus DESC"),
    @NamedQuery(name = "Convidado.findByDataconvite", query = "SELECT c FROM Convidado c WHERE c.dataconvite = :dataconvite"),
    @NamedQuery(name = "Convidado.findByConfirmado", query = "SELECT c FROM Convidado c WHERE c.confirmado = :confirmado"),
    @NamedQuery(name = "Convidado.findByDataconfirmacao", query = "SELECT c FROM Convidado c WHERE c.dataconfirmacao = :dataconfirmacao"),
    @NamedQuery(name = "Convidado.findByEnviadoemail", query = "SELECT c FROM Convidado c WHERE c.enviadoemail = :enviadoemail")})
public class Convidado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idconvidado", nullable = false)
    private Integer idconvidado;
    @Column(name = "dataconvite")
    @Temporal(TemporalType.DATE)
    private Date dataconvite;
    @Column(name = "confirmado")
    private Boolean confirmado;
    @Column(name = "dataconfirmacao")
    @Temporal(TemporalType.DATE)
    private Date dataconfirmacao;
    @Column(name = "enviadoemail")
    private Boolean enviadoemail;
    @JoinColumn(name = "professorsiape", referencedColumnName = "siape", nullable = false)
    @ManyToOne(optional = false)
    private Professor professorsiape;
    @JoinColumn(name = "bancaidbanca", referencedColumnName = "idbanca", nullable = false)
    @ManyToOne(optional = false)
    private Banca bancaidbanca;

    public Convidado() {
    }

    public Convidado(Integer idconvidado) {
        this.idconvidado = idconvidado;
    }

    public Integer getIdconvidado() {
        return idconvidado;
    }

    public void setIdconvidado(Integer idconvidado) {
        this.idconvidado = idconvidado;
    }

    public Date getDataconvite() {
        return dataconvite;
    }

    public void setDataconvite(Date dataconvite) {
        this.dataconvite = dataconvite;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }

    public Date getDataconfirmacao() {
        return dataconfirmacao;
    }

    public void setDataconfirmacao(Date dataconfirmacao) {
        this.dataconfirmacao = dataconfirmacao;
    }

    public Boolean getEnviadoemail() {
        return enviadoemail;
    }

    public void setEnviadoemail(Boolean enviadoemail) {
        this.enviadoemail = enviadoemail;
    }

    public Professor getProfessorsiape() {
        return professorsiape;
    }

    public void setProfessorsiape(Professor professorsiape) {
        this.professorsiape = professorsiape;
    }

    public Banca getBancaidbanca() {
        return bancaidbanca;
    }

    public void setBancaidbanca(Banca bancaidbanca) {
        this.bancaidbanca = bancaidbanca;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconvidado != null ? idconvidado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convidado)) {
            return false;
        }
        Convidado other = (Convidado) object;
        if ((this.idconvidado == null && other.idconvidado != null) || (this.idconvidado != null && !this.idconvidado.equals(other.idconvidado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Convidado[ idconvidado=" + idconvidado + " ]";
    }

}
