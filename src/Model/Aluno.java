/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "aluno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aluno.findAll", query = "SELECT a FROM Aluno a ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByMatricula", query = "SELECT a FROM Aluno a WHERE a.matricula = :matricula ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByAlunoByIdCurso", query = "SELECT a FROM Aluno a WHERE a.cursoidcurso.idcurso = :idcurso ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByNomeAndIdCurso", query = "SELECT a FROM Aluno a WHERE lower(a.nome) LIKE lower(:nome) and a.cursoidcurso.idcurso = :idcurso ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByNome", query = "SELECT a FROM Aluno a WHERE lower(a.nome) LIKE lower(:nome) ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByTelefone", query = "SELECT a FROM Aluno a WHERE a.telefone = :telefone ORDER BY a.nome ASC"),
    @NamedQuery(name = "Aluno.findByEmail", query = "SELECT a FROM Aluno a WHERE a.email = :email ORDER BY a.nome ASC")})
public class Aluno implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alunomatricula")
    private List<Trabalho> trabalhoList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "matricula", nullable = false, length = 2147483647)
    private String matricula;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @Column(name = "telefone", length = 2147483647)
    private String telefone;
    @Column(name = "email", length = 2147483647)
    private String email;
    @JoinColumn(name = "cursoidcurso", referencedColumnName = "idcurso", nullable = false)
    @ManyToOne(optional = false)
    private Curso cursoidcurso;

    public Aluno() {
        cursoidcurso = new Curso();
        trabalhoList = new ArrayList<>();
    }

    public Aluno(String matricula) {
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Curso getCursoidcurso() {
        return cursoidcurso;
    }

    public void setCursoidcurso(Curso cursoidcurso) {
        this.cursoidcurso = cursoidcurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aluno)) {
            return false;
        }
        Aluno other = (Aluno) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Aluno[ matricula=" + matricula + " ]";
    }

    @XmlTransient
    public List<Trabalho> getTrabalhoList() {
        return trabalhoList;
    }

    public void setTrabalhoList(List<Trabalho> trabalhoList) {
        this.trabalhoList = trabalhoList;
    }

}
