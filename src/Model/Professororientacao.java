/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "professororientacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Professororientacao.findAll", query = "SELECT p FROM Professororientacao p"),
    @NamedQuery(name = "Professororientacao.findBySiape", query = "SELECT p FROM Professororientacao p WHERE p.siape = :siape"),
    @NamedQuery(name = "Professororientacao.findByNome", query = "SELECT p FROM Professororientacao p WHERE p.nome = :nome"),
    @NamedQuery(name = "Professororientacao.findByEmail", query = "SELECT p FROM Professororientacao p WHERE p.email = :email"),
    @NamedQuery(name = "Professororientacao.findByTelefone", query = "SELECT p FROM Professororientacao p WHERE p.telefone = :telefone"),
    @NamedQuery(name = "Professororientacao.findByOrientacoes", query = "SELECT p FROM Professororientacao p WHERE p.orientacoes = :orientacoes")})
public class Professororientacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "siape")
    @Id
    private Integer siape;
    @Column(name = "nome", length = 2147483647)
    private String nome;
    @Column(name = "email", length = 2147483647)
    private String email;
    @Column(name = "telefone", length = 2147483647)
    private String telefone;
    @Column(name = "orientacoes")
    private BigInteger orientacoes;

    public Professororientacao() {
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

    public BigInteger getOrientacoes() {
        return orientacoes;
    }

    public void setOrientacoes(BigInteger orientacoes) {
        this.orientacoes = orientacoes;
    }

}
