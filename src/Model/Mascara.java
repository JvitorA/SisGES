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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "mascara")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mascara.findAll", query = "SELECT m FROM Mascara m"),
    @NamedQuery(name = "Mascara.findByIdmascara", query = "SELECT m FROM Mascara m WHERE m.idmascara = :idmascara"),
    @NamedQuery(name = "Mascara.findByObjeto", query = "SELECT m FROM Mascara m WHERE m.objeto = :objeto"),
    @NamedQuery(name = "Mascara.findByAtributo", query = "SELECT m FROM Mascara m WHERE m.atributo = :atributo"),
    @NamedQuery(name = "Mascara.findByNomemascara", query = "SELECT m FROM Mascara m WHERE m.nomemascara = :nomemascara")})
public class Mascara implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmascara", nullable = false)
    private Integer idmascara;
    @Column(name = "objeto", length = 2147483647)
    private String objeto;
    @Column(name = "atributo", length = 2147483647)
    private String atributo;
    @Column(name = "nomemascara", length = 2147483647)
    private String nomemascara;
    @ManyToMany(mappedBy = "mascaraList")
    private List<Email> emailList;

    public Mascara() {
    }

    public Mascara(Integer idmascara) {
        this.idmascara = idmascara;
    }

    public Integer getIdmascara() {
        return idmascara;
    }

    public void setIdmascara(Integer idmascara) {
        this.idmascara = idmascara;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getNomemascara() {
        return nomemascara;
    }

    public void setNomemascara(String nomemascara) {
        this.nomemascara = nomemascara;
    }

    @XmlTransient
    public List<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<Email> emailList) {
        this.emailList = emailList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmascara != null ? idmascara.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mascara)) {
            return false;
        }
        Mascara other = (Mascara) object;
        if ((this.idmascara == null && other.idmascara != null) || (this.idmascara != null && !this.idmascara.equals(other.idmascara))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Mascara[ idmascara=" + idmascara + " ]";
    }

}
