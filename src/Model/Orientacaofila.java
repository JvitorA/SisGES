/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "orientacaofila")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orientacaofila.findAll", query = "SELECT o FROM Orientacaofila o"),
    @NamedQuery(name = "Orientacaofila.findBySiape", query = "SELECT o FROM Orientacaofila o WHERE o.siape = :siape"),
    @NamedQuery(name = "Orientacaofila.findByNome", query = "SELECT o FROM Orientacaofila o WHERE o.nome = :nome"),
    @NamedQuery(name = "Orientacaofila.findByEmail", query = "SELECT o FROM Orientacaofila o WHERE o.email = :email"),
    @NamedQuery(name = "Orientacaofila.findByTelefone", query = "SELECT o FROM Orientacaofila o WHERE o.telefone = :telefone"),
    @NamedQuery(name = "Orientacaofila.findByParticipacoes", query = "SELECT o FROM Orientacaofila o WHERE o.participacoes = :participacoes"),
    @NamedQuery(name = "Orientacaofila.findByUltimaorientacao", query = "SELECT o FROM Orientacaofila o WHERE o.ultimaorientacao = :ultimaorientacao"),
    @NamedQuery(name = "Orientacaofila.findByCalculoposicao", query = "SELECT o FROM Orientacaofila o WHERE o.calculoposicao = :calculoposicao")})
public class Orientacaofila implements Serializable {
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
    @Column(name = "participacoes")
    private BigInteger participacoes;
    @Column(name = "ultimaorientacao")
    @Temporal(TemporalType.DATE)
    private Date ultimaorientacao;
    @Column(name = "calculoposicao")
    private BigInteger calculoposicao;

    public Orientacaofila() {
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

    public BigInteger getParticipacoes() {
        return participacoes;
    }

    public void setParticipacoes(BigInteger participacoes) {
        this.participacoes = participacoes;
    }

    public Date getUltimaorientacao() {
        return ultimaorientacao;
    }

    public void setUltimaorientacao(Date ultimaorientacao) {
        this.ultimaorientacao = ultimaorientacao;
    }

    public BigInteger getCalculoposicao() {
        return calculoposicao;
    }

    public void setCalculoposicao(BigInteger calculoposicao) {
        this.calculoposicao = calculoposicao;
    }
    
}
