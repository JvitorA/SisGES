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
@Table(name = "login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l"),
    @NamedQuery(name = "Login.findByLogin", query = "SELECT l FROM Login l WHERE l.professorsiape.siape = :professorsiape and l.senha = :senha"),
    @NamedQuery(name = "Login.findBySiape", query = "SELECT l FROM Login l WHERE l.professorsiape.siape = :professorsiape"),
    @NamedQuery(name = "Login.findByIdlogin", query = "SELECT l FROM Login l WHERE l.idlogin = :idlogin"),
    @NamedQuery(name = "Login.findBySenha", query = "SELECT l FROM Login l WHERE l.senha = :senha")})
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idlogin", nullable = false)
    private Integer idlogin;
    @Column(name = "senha", length = 2147483647)
    private String senha;
    @JoinColumn(name = "professorsiape", referencedColumnName = "siape", nullable = false)
    @ManyToOne(optional = false)
    private Professor professorsiape;

    public Login() {
        professorsiape = new Professor();
    }

    public Login(Integer idlogin) {
        this.idlogin = idlogin;
    }

    public Integer getIdlogin() {
        return idlogin;
    }

    public void setIdlogin(Integer idlogin) {
        this.idlogin = idlogin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
        hash += (idlogin != null ? idlogin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        if ((this.idlogin == null && other.idlogin != null) || (this.idlogin != null && !this.idlogin.equals(other.idlogin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Login[ idlogin=" + idlogin + " ]";
    }

}
