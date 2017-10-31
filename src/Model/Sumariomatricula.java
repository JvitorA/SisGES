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
@Table(name = "sumariomatricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sumariomatricula.findAll", query = "SELECT s FROM Sumariomatricula s"),
    @NamedQuery(name = "Sumariomatricula.findByStatus", query = "SELECT s FROM Sumariomatricula s WHERE s.status = :status"),
    @NamedQuery(name = "Sumariomatricula.findByTipoestagio", query = "SELECT s FROM Sumariomatricula s WHERE s.tipoestagio = :tipoestagio"),
    @NamedQuery(name = "Sumariomatricula.findByMatriculas", query = "SELECT s FROM Sumariomatricula s WHERE s.matriculas = :matriculas")})
public class Sumariomatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "status", length = 2147483647)
    private String status;
    @Column(name = "tipoestagio", length = 2147483647)
    @Id
    private String tipoestagio;
    @Column(name = "matriculas")
    private BigInteger matriculas;

    public Sumariomatricula() {
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

    public BigInteger getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(BigInteger matriculas) {
        this.matriculas = matriculas;
    }

}
