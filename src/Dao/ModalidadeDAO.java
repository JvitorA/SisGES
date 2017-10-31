/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Modalidade;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class ModalidadeDAO extends GenericDAO<Modalidade> {

    public Modalidade getModalidadeByNome(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Modalidade.findByNomemodalidade").setParameter("nomemodalidade", nome);
        Modalidade u = (Modalidade) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    public Modalidade getModalidadeById(String id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Modalidade.findByIdmodalidade").setParameter("idmodalidade", id);
        Modalidade u = (Modalidade) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Modalidade> getAllModalidades() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Modalidade.findAll");
        return q.list();
    }
}
