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
@Table(name = "alunomatriculado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alunomatriculado.findAll", query = "SELECT a FROM Alunomatriculado a"),
    @NamedQuery(name = "Alunomatriculado.findByTipoestagio", query = "SELECT a FROM Alunomatriculado a WHERE a.tipoestagio = :tipoestagio"),
    @NamedQuery(name = "Alunomatriculado.findByMatricula", query = "SELECT a FROM Alunomatriculado a WHERE a.matricula = :matricula"),
    @NamedQuery(name = "Alunomatriculado.findByNome", query = "SELECT a FROM Alunomatriculado a WHERE a.nome = :nome"),
    @NamedQuery(name = "Alunomatriculado.findByOrientador", query = "SELECT a FROM Alunomatriculado a WHERE a.orientador = :orientador"),
    @NamedQuery(name = "Alunomatriculado.findByDatamatricula", query = "SELECT a FROM Alunomatriculado a WHERE a.datamatricula = :datamatricula")})
public class Alunomatriculado implements Serializable {

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

    public Alunomatriculado() {
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
}
