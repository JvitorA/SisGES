/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "trabalho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabalho.findAll", query = "SELECT t FROM Trabalho t ORDER BY t.statusidstatus.idstatus DESC"),
    @NamedQuery(name = "Trabalho.findByIdtrabalho", query = "SELECT t FROM Trabalho t WHERE t.idtrabalho = :idtrabalho"),
    @NamedQuery(name = "Trabalho.findByHasDefesaNotBanca", query = "SELECT t FROM Trabalho t, Defesa d WHERE t.statusidstatus.tipostatus='Em andamento' and t.idtrabalho = d.trabalhoidtrabalho.idtrabalho and t.idtrabalho not in(SELECT b.trabalhoidtrabalho.idtrabalho FROM Banca b)"),
    @NamedQuery(name = "Trabalho.findByTitulo", query = "SELECT t FROM Trabalho t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Trabalho.findByAlunoAndTituloAndStatusAndOrientador", query = "SELECT t FROM Trabalho t WHERE t.alunomatricula = :aluno and t.empresaidempresa = :empresa and t.titulo = :titulo and t.estagioidestagio.tipoestagio = :tipoestagio and t.professorsiape.siape = :siape and t.professorsiape.nome = :nomeProf and t.datamatricula = :datamatricula"),
    @NamedQuery(name = "Trabalho.findByConvidado", query = "SELECT t FROM Trabalho t, Banca b WHERE t.alunomatricula = :aluno and t.empresaidempresa = :empresa and t.titulo = :titulo and t.estagioidestagio.tipoestagio = :tipoestagio and t.professorsiape.siape = :siape and t.professorsiape.nome = :nomeProf and t.datamatricula = :datamatricula and t.idtrabalho = b.trabalhoidtrabalho and b.localbanca = :local"),
    @NamedQuery(name = "Trabalho.findByOrientador", query = "SELECT t FROM Trabalho t WHERE t.professorsiape.siape = :siape ORDER BY t.statusidstatus.idstatus DESC"),
    @NamedQuery(name = "Trabalho.findByDatamatricula", query = "SELECT t FROM Trabalho t WHERE t.datamatricula = :datamatricula"),
    @NamedQuery(name = "Trabalho.findByQtdhorasdia", query = "SELECT t FROM Trabalho t WHERE t.qtdhorasdia = :qtdhorasdia")})
public class Trabalho implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabalho")
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtrabalho", nullable = false)
    private Integer idtrabalho;
    @Column(name = "titulo", length = 2147483647)
    private String titulo;
    @Column(name = "datamatricula")
    @Temporal(TemporalType.DATE)
    private Date datamatricula;

    @Column(name = "datafinalizacao")
    @Temporal(TemporalType.DATE)
    private Date datafinalizacao;

    @Column(name = "qtdhorasdia")
    @Temporal(TemporalType.TIME)
    private Date qtdhorasdia;

    @JoinTable(name = "trabalhoemail", joinColumns = {
        @JoinColumn(name = "trabalhoidtrabalho", referencedColumnName = "idtrabalho", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "emailidemail", referencedColumnName = "idemail", nullable = false)})
    @ManyToMany
    private List<Email> emailList;

    @JoinColumn(name = "statusidstatus", referencedColumnName = "idstatus", nullable = false)
    @ManyToOne(optional = false)
    private Trabalhostatus statusidstatus;
    @JoinColumn(name = "modalidadeidmodalidade", referencedColumnName = "idmodalidade")
    @ManyToOne
    private Modalidade modalidadeidmodalidade;
    @JoinColumn(name = "estagioidestagio", referencedColumnName = "idestagio", nullable = false)
    @ManyToOne(optional = false)
    private Estagio estagioidestagio;
    @JoinColumn(name = "empresaidempresa", referencedColumnName = "idempresa", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresaidempresa;
    @JoinColumn(name = "assuntoidassunto", referencedColumnName = "idassunto", nullable = false)
    @ManyToOne(optional = false)
    private Assunto assuntoidassunto;
    @JoinColumn(name = "alunomatricula", referencedColumnName = "matricula", nullable = false)
    @ManyToOne(optional = false)
    private Aluno alunomatricula;
    @JoinColumn(name = "professorsiape", referencedColumnName = "siape", nullable = false)
    @ManyToOne(optional = false)
    private Professor professorsiape;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabalhoidtrabalho")
    private List<Banca> bancaList;

    public Trabalho() {
        alunomatricula = new Aluno();
        assuntoidassunto = new Assunto();
        empresaidempresa = new Empresa();
        estagioidestagio = new Estagio();
        modalidadeidmodalidade = new Modalidade();
        statusidstatus = new Trabalhostatus();
        professorsiape = new Professor();
        emailList = new ArrayList<>();
        bancaList = new ArrayList<>();
    }

    public Trabalho(Integer idtrabalho) {
        this.idtrabalho = idtrabalho;
    }

    public Integer getIdtrabalho() {
        return idtrabalho;
    }

    public void setIdtrabalho(Integer idtrabalho) {
        this.idtrabalho = idtrabalho;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDatamatricula() {
        return datamatricula;
    }

    public void setDatamatricula(Date datamatricula) {
        this.datamatricula = datamatricula;
    }

    public Date getDatafinalizacao() {
        return datafinalizacao;
    }

    public void setDatafinalizacao(Date datafinalizacao) {
        this.datafinalizacao = datafinalizacao;
    }

    public Date getQtdhorasdia() {
        return qtdhorasdia;
    }

    public void setQtdhorasdia(Date qtdhorasdia) {
        this.qtdhorasdia = qtdhorasdia;
    }

    @XmlTransient
    public List<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<Email> emailList) {
        this.emailList = emailList;
    }

    public Trabalhostatus getStatusidstatus() {
        return statusidstatus;
    }

    public void setStatusidstatus(Trabalhostatus statusidstatus) {
        this.statusidstatus = statusidstatus;
    }

    public Modalidade getModalidadeidmodalidade() {
        return modalidadeidmodalidade;
    }

    public void setModalidadeidmodalidade(Modalidade modalidadeidmodalidade) {
        this.modalidadeidmodalidade = modalidadeidmodalidade;
    }

    public Estagio getEstagioidestagio() {
        return estagioidestagio;
    }

    public void setEstagioidestagio(Estagio estagioidestagio) {
        this.estagioidestagio = estagioidestagio;
    }

    public Empresa getEmpresaidempresa() {
        return empresaidempresa;
    }

    public void setEmpresaidempresa(Empresa empresaidempresa) {
        this.empresaidempresa = empresaidempresa;
    }

    public Assunto getAssuntoidassunto() {
        return assuntoidassunto;
    }

    public void setAssuntoidassunto(Assunto assuntoidassunto) {
        this.assuntoidassunto = assuntoidassunto;
    }

    public Aluno getAlunomatricula() {
        return alunomatricula;
    }

    public void setAlunomatricula(Aluno alunomatricula) {
        this.alunomatricula = alunomatricula;
    }

    public Professor getProfessorsiape() {
        return professorsiape;
    }

    public void setProfessorsiape(Professor professorsiape) {
        this.professorsiape = professorsiape;
    }

    @XmlTransient
    public List<Banca> getBancaList() {
        return bancaList;
    }

    public void setBancaList(List<Banca> bancaList) {
        this.bancaList = bancaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtrabalho != null ? idtrabalho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabalho)) {
            return false;
        }
        Trabalho other = (Trabalho) object;
        if ((this.idtrabalho == null && other.idtrabalho != null) || (this.idtrabalho != null && !this.idtrabalho.equals(other.idtrabalho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Matrícula: " + this.getAlunomatricula().getMatricula() + "  -  Nome: " + this.getAlunomatricula().getNome();

    }

}
