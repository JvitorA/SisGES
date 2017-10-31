/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "alunoaprovado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alunoaprovado.findAll", query = "SELECT a FROM Alunoaprovado a"),
    @NamedQuery(name = "Alunoaprovado.findByTipoestagio", query = "SELECT a FROM Alunoaprovado a WHERE a.tipoestagio = :tipoestagio"),
    @NamedQuery(name = "Alunoaprovado.findByMatricula", query = "SELECT a FROM Alunoaprovado a WHERE a.matricula = :matricula"),
    @NamedQuery(name = "Alunoaprovado.findByNome", query = "SELECT a FROM Alunoaprovado a WHERE a.nome = :nome"),
    @NamedQuery(name = "Alunoaprovado.findByOrientador", query = "SELECT a FROM Alunoaprovado a WHERE a.orientador = :orientador"),
    @NamedQuery(name = "Alunoaprovado.findByDatamatricula", query = "SELECT a FROM Alunoaprovado a WHERE a.datamatricula = :datamatricula"),
    @NamedQuery(name = "Alunoaprovado.findByDatafinalizacao", query = "SELECT a FROM Alunoaprovado a WHERE a.datafinalizacao = :datafinalizacao")})
public class Alunoaprovado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "tipoestagio", length = 2147483647)
    private String tipoestagio;
    @Column(name = "matricula", length = 2147483647)
    private String matricula;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @Column(name = "orientador", length = 2147483647)
    private String orientador;
    @Column(name = "datamatricula")
    @Temporal(TemporalType.DATE)
    @Id
    private Date datamatricula;
    @Column(name = "datafinalizacao")
    @Temporal(TemporalType.DATE)
    private Date datafinalizacao;

    public Alunoaprovado() {
    }

    public String getTipoestagio() {
        return tipoestagio;
    }

    public void setTipoestagio(String tipoestagio) {
        this.tipoestagio = tipoestagio;
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

    public String getOrientador() {
        return orientador;
    }

    public void setOrientador(String orientador) {
        this.orientador = orientador;
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

}
