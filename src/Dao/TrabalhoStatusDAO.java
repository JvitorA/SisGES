/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Trabalhostatus;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class TrabalhoStatusDAO extends GenericDAO<Trabalhostatus> {

    @SuppressWarnings("unchecked")
    public List<Trabalhostatus> getAllTrabalhoStatus() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalhostatus.findAll");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public Trabalhostatus getStatusByTipo(String tipo) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalhostatus.findByTipostatus").setParameter("tipostatus", tipo);
        Trabalhostatus u = (Trabalhostatus) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public Trabalhostatus getStatusById(Integer id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalhostatus.findByIdstatus").setParameter("idstatus", id);
        Trabalhostatus u = (Trabalhostatus) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }
}
