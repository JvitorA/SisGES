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
@Table(name = "sumarioaprovado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sumarioaprovado.findAll", query = "SELECT s FROM Sumarioaprovado s"),
    @NamedQuery(name = "Sumarioaprovado.findByStatus", query = "SELECT s FROM Sumarioaprovado s WHERE s.status = :status"),
    @NamedQuery(name = "Sumarioaprovado.findByTipoestagio", query = "SELECT s FROM Sumarioaprovado s WHERE s.tipoestagio = :tipoestagio"),
    @NamedQuery(name = "Sumarioaprovado.findByAprovados", query = "SELECT s FROM Sumarioaprovado s WHERE s.aprovados = :aprovados")})
public class Sumarioaprovado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "status", length = 2147483647)
    private String status;
    @Column(name = "tipoestagio", length = 2147483647)
    @Id
    private String tipoestagio;
    @Column(name = "aprovados")
    private BigInteger aprovados;

    public Sumarioaprovado() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoestagio() {
        return tipoestagio;
    }

    public void setTipoestagio(String tipoestagio) {
        this.tipoestagio = tipoestagio;
    }

    public BigInteger getAprovados() {
        return aprovados;
    }

    public void setAprovados(BigInteger aprovados) {
        this.aprovados = aprovados;
    }

}
