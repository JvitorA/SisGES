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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e"),
    @NamedQuery(name = "Email.findByIdemail", query = "SELECT e FROM Email e WHERE e.idemail = :idemail"),
    @NamedQuery(name = "Email.findByTitulo", query = "SELECT e FROM Email e WHERE e.titulo = :titulo"),
    @NamedQuery(name = "Email.findByMensagem", query = "SELECT e FROM Email e WHERE e.mensagem = :mensagem")})
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idemail", nullable = false)
    private Integer idemail;
    @Column(name = "titulo", length = 2147483647)
    private String titulo;
    @Column(name = "mensagem", length = 2147483647)
    private String mensagem;
    @JoinTable(name = "emailmascara", joinColumns = {
        @JoinColumn(name = "emailidemail", referencedColumnName = "idemail", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "mascaraidmascara", referencedColumnName = "idmascara", nullable = false)})
    @ManyToMany
    private List<Mascara> mascaraList;
    @JoinColumn(name = "hostname", referencedColumnName = "email", nullable = false)
    @ManyToOne(optional = false)
    private EmailConfig hostname;

    public Email() {
    }

    public Email(Integer idemail) {
        this.idemail = idemail;
    }

    public Integer getIdemail() {
        return idemail;
    }

    public void setIdemail(Integer idemail) {
        this.idemail = idemail;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @XmlTransient
    public List<Mascara> getMascaraList() {
        return mascaraList;
    }

    public void setMascaraList(List<Mascara> mascaraList) {
        this.mascaraList = mascaraList;
    }

    public EmailConfig getHostname() {
        return hostname;
    }

    public void setHostname(EmailConfig hostname) {
        this.hostname = hostname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idemail != null ? idemail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.idemail == null && other.idemail != null) || (this.idemail != null && !this.idemail.equals(other.idemail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Email[ idemail=" + idemail + " ]";
    }

}
