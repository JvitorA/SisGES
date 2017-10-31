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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "professor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Professor.findAll", query = "SELECT p FROM Professor p ORDER BY p.nome ASC"),
    @NamedQuery(name = "Professor.findAllCoordenador", query = "SELECT p FROM Professor p WHERE p.perfilidperfil = 1 ORDER BY p.nome ASC"),
    @NamedQuery(name = "Professor.findBySiape", query = "SELECT p FROM Professor p WHERE p.siape = :siape ORDER BY p.nome ASC"),
    @NamedQuery(name = "Professor.findByNome", query = "SELECT p FROM Professor p WHERE p.nome = :nome ORDER BY p.nome ASC"),
    @NamedQuery(name = "Professor.findByEmail", query = "SELECT p FROM Professor p WHERE p.email = :email ORDER BY p.nome ASC"),
    @NamedQuery(name = "Professor.findByTelefone", query = "SELECT p FROM Professor p WHERE p.telefone = :telefone ORDER BY p.nome ASC")})
public class Professor implements Serializable {

    @OneToMany(mappedBy = "professorsiape")
    private List<Trabalho> trabalhoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professorsiape")
    private List<Professordisponibilidade> professordisponibilidadeList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "siape", nullable = false)
    private Integer siape;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @Column(name = "email", length = 2147483647)
    private String email;
    @Column(name = "telefone", length = 2147483647)
    private String telefone;
    @Column(name = "ativo")
    private Boolean ativo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professorsiape")
    private List<Assinatura> assinaturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professorsiape")
    private List<Convidado> convidadoList;
    @JoinColumn(name = "perfilidperfil", referencedColumnName = "idperfil", nullable = false)
    @ManyToOne(optional = false)
    private Perfil perfilidperfil;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professorsiape")
    private List<Login> loginList;

    public Professor() {
        this.perfilidperfil = new Perfil();
        this.ativo = false;
    }

    public Professor(Integer siape) {
        this.siape = siape;
    }

    public Integer getSiape() {
        return siape;
    }

    public void setSiape(Integer siape) {
        this.siape = siape;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @XmlTransient
    public List<Assinatura> getAssinaturaList() {
        return assinaturaList;
    }

    public void setAssinaturaList(List<Assinatura> assinaturaList) {
        this.assinaturaList = assinaturaList;
    }

    @XmlTransient
    public List<Convidado> getConvidadoList() {
        return convidadoList;
    }

    public void setConvidadoList(List<Convidado> convidadoList) {
        this.convidadoList = convidadoList;
    }

    public Perfil getPerfilidperfil() {
        return perfilidperfil;
    }

    public void setPerfilidperfil(Perfil perfilidperfil) {
        this.perfilidperfil = perfilidperfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    @XmlTransient
    public String getAtivoFormatado() {
        if (this.ativo == true) {
            return "Sim";
        }
        return "Não";
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @XmlTransient
    public List<Login> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<Login> loginList) {
        this.loginList = loginList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siape != null ? siape.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Professor)) {
            return false;
        }
        Professor other = (Professor) object;
        if ((this.siape == null && other.siape != null) || (this.siape != null && !this.siape.equals(other.siape))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Professor[ siape=" + siape + " ]";
    }

    @XmlTransient
    public List<Professordisponibilidade> getProfessordisponibilidadeList() {
        return professordisponibilidadeList;
    }

    public void setProfessordisponibilidadeList(List<Professordisponibilidade> professordisponibilidadeList) {
        this.professordisponibilidadeList = professordisponibilidadeList;
    }

    @XmlTransient
    public List<Trabalho> getTrabalhoList() {
        return trabalhoList;
    }

    public void setTrabalhoList(List<Trabalho> trabalhoList) {
        this.trabalhoList = trabalhoList;
    }

}
