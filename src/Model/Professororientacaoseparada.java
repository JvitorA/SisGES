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
@Table(name = "professororientacaoseparada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Professororientacaoseparada.findAll", query = "SELECT p FROM Professororientacaoseparada p"),
    @NamedQuery(name = "Professororientacaoseparada.findBySiape", query = "SELECT p FROM Professororientacaoseparada p WHERE p.siape = :siape"),
    @NamedQuery(name = "Professororientacaoseparada.findByNome", query = "SELECT p FROM Professororientacaoseparada p WHERE p.nome = :nome"),
    @NamedQuery(name = "Professororientacaoseparada.findByEmail", query = "SELECT p FROM Professororientacaoseparada p WHERE p.email = :email"),
    @NamedQuery(name = "Professororientacaoseparada.findByTelefone", query = "SELECT p FROM Professororientacaoseparada p WHERE p.telefone = :telefone"),
    @NamedQuery(name = "Professororientacaoseparada.findByOrientacoesestobrigatorio", query = "SELECT p FROM Professororientacaoseparada p WHERE p.orientacoesestobrigatorio = :orientacoesestobrigatorio"),
    @NamedQuery(name = "Professororientacaoseparada.findByOrientacoesestnaoobrigatorio", query = "SELECT p FROM Professororientacaoseparada p WHERE p.orientacoesestnaoobrigatorio = :orientacoesestnaoobrigatorio")})
public class Professororientacaoseparada implements Serializable {

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
    @Column(name = "orientacoesestobrigatorio")
    private BigInteger orientacoesestobrigatorio;
    @Column(name = "orientacoesestnaoobrigatorio")
    private BigInteger orientacoesestnaoobrigatorio;

    public Professororientacaoseparada() {
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

    public BigInteger getOrientacoesestobrigatorio() {
        return orientacoesestobrigatorio;
    }

    public void setOrientacoesestobrigatorio(BigInteger orientacoesestobrigatorio) {
        this.orientacoesestobrigatorio = orientacoesestobrigatorio;
    }

    public BigInteger getOrientacoesestnaoobrigatorio() {
        return orientacoesestnaoobrigatorio;
    }

    public void setOrientacoesestnaoobrigatorio(BigInteger orientacoesestnaoobrigatorio) {
        this.orientacoesestnaoobrigatorio = orientacoesestnaoobrigatorio;
    }

}
