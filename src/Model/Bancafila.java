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
@Table(name = "bancafila")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bancafila.findAll", query = "SELECT b FROM Bancafila b"),
    @NamedQuery(name = "Bancafila.findBySiape", query = "SELECT b FROM Bancafila b WHERE b.siape = :siape"),
    @NamedQuery(name = "Bancafila.findByNome", query = "SELECT b FROM Bancafila b WHERE b.nome = :nome"),
    @NamedQuery(name = "Bancafila.findByEmail", query = "SELECT b FROM Bancafila b WHERE b.email = :email"),
    @NamedQuery(name = "Bancafila.findByTelefone", query = "SELECT b FROM Bancafila b WHERE b.telefone = :telefone"),
    @NamedQuery(name = "Bancafila.findByDataconvite", query = "SELECT b FROM Bancafila b WHERE b.dataconvite = :dataconvite"),
    @NamedQuery(name = "Bancafila.findByDataconfirmacao", query = "SELECT b FROM Bancafila b WHERE b.dataconfirmacao = :dataconfirmacao"),
    @NamedQuery(name = "Bancafila.findByConfirmado", query = "SELECT b FROM Bancafila b WHERE b.confirmado = :confirmado"),
    @NamedQuery(name = "Bancafila.findByParticipacoes", query = "SELECT b FROM Bancafila b WHERE b.participacoes = :participacoes"),
    @NamedQuery(name = "Bancafila.findByUltimaconfirmacao", query = "SELECT b FROM Bancafila b WHERE b.ultimaconfirmacao = :ultimaconfirmacao"),
    @NamedQuery(name = "Bancafila.findByCalculoposicao", query = "SELECT b FROM Bancafila b WHERE b.calculoposicao = :calculoposicao")})
public class Bancafila implements Serializable {
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
    @Column(name = "dataconvite")
    @Temporal(TemporalType.DATE)
    private Date dataconvite;
    @Column(name = "dataconfirmacao")
    @Temporal(TemporalType.DATE)
    private Date dataconfirmacao;
    @Column(name = "confirmado")
    private Boolean confirmado;
    @Column(name = "participacoes")
    private BigInteger participacoes;
    @Column(name = "ultimaconfirmacao")
    @Temporal(TemporalType.DATE)
    private Date ultimaconfirmacao;
    @Column(name = "calculoposicao")
    private BigInteger calculoposicao;

    public Bancafila() {
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

    public Date getDataconvite() {
        return dataconvite;
    }

    public void setDataconvite(Date dataconvite) {
        this.dataconvite = dataconvite;
    }

    public Date getDataconfirmacao() {
        return dataconfirmacao;
    }

    public void setDataconfirmacao(Date dataconfirmacao) {
        this.dataconfirmacao = dataconfirmacao;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }

    public BigInteger getParticipacoes() {
        return participacoes;
    }

    public void setParticipacoes(BigInteger participacoes) {
        this.participacoes = participacoes;
    }

    public Date getUltimaconfirmacao() {
        return ultimaconfirmacao;
    }

    public void setUltimaconfirmacao(Date ultimaconfirmacao) {
        this.ultimaconfirmacao = ultimaconfirmacao;
    }

    public BigInteger getCalculoposicao() {
        return calculoposicao;
    }

    public void setCalculoposicao(BigInteger calculoposicao) {
        this.calculoposicao = calculoposicao;
    }
    
}
