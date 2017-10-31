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
@Table(name = "emailconfig")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailConfig.findAll", query = "SELECT e FROM EmailConfig e"),
    @NamedQuery(name = "EmailConfig.findByEmail", query = "SELECT e FROM EmailConfig e WHERE e.email = :email"),
    @NamedQuery(name = "EmailConfig.findByPorta", query = "SELECT e FROM EmailConfig e WHERE e.porta = :porta"),
    @NamedQuery(name = "EmailConfig.findByUsuario", query = "SELECT e FROM EmailConfig e WHERE e.usuario = :usuario"),
    @NamedQuery(name = "EmailConfig.findBySenha", query = "SELECT e FROM EmailConfig e WHERE e.senha = :senha"),
    @NamedQuery(name = "EmailConfig.findBySmtp", query = "SELECT e FROM EmailConfig e WHERE e.smtp = :smtp"),
    @NamedQuery(name = "EmailConfig.findBySsl", query = "SELECT e FROM EmailConfig e WHERE e.ssl = :ssl"),
    @NamedQuery(name = "EmailConfig.findByNome", query = "SELECT e FROM EmailConfig e WHERE e.nome = :nome"),
    @NamedQuery(name = "EmailConfig.findByHostname", query = "SELECT e FROM EmailConfig e WHERE e.hostname = :hostname"),
    @NamedQuery(name = "EmailConfig.findByTsl", query = "SELECT e FROM EmailConfig e WHERE e.tsl = :tsl")})
public class EmailConfig implements Serializable {

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "hostname")
    //private List<Email> emailList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 2147483647)
    private String email;
    @Column(name = "porta")
    private Integer porta;
    @Column(name = "usuario", length = 2147483647)
    private String usuario;
    @Column(name = "senha", length = 2147483647)
    private String senha;
    @Column(name = "smtp", length = 2147483647)
    private String smtp;
    @Column(name = "ssl")
    private Boolean ssl;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @Column(name = "hostname", length = 2147483647)
    private String hostname;
    @Column(name = "tsl")
    private Boolean tsl;

    public EmailConfig() {
        this.nome = "";
        this.email = "";
        this.hostname = "";
    }

    public EmailConfig(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPorta() {
        return porta;
    }

    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Boolean getTsl() {
        return tsl;
    }

    public void setTsl(Boolean tsl) {
        this.tsl = tsl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmailConfig)) {
            return false;
        }
        EmailConfig other = (EmailConfig) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.EmailConfig[ email=" + email + " ]";
    }
    /*
     @XmlTransient
     public List<Email> getEmailList() {
     return emailList;
     }

     public void setEmailList(List<Email> emailList) {
     this.emailList = emailList;
     }
     */
}
