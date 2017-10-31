/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Assunto;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class AssuntoDAO extends GenericDAO<Assunto> {

    public Assunto getAssuntoByNome(String nome) throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Assunto.findByNome").setParameter("nome", nome);
        Assunto u = (Assunto) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Assunto> getAllAssuntos() throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Assunto.findAll");
        return q.list();
    }
}
