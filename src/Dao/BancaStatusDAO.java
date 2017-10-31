/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Bancastatus;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class BancaStatusDAO extends GenericDAO<Bancastatus> {

    @SuppressWarnings("unchecked")
    public List<Bancastatus> getAllBancaStatus() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Bancastatus.findAll");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public Bancastatus getStatusByTipo(String tipo) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Bancastatus.findByTipostatus").setParameter("tipostatus", tipo);
        Bancastatus u = (Bancastatus) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public Bancastatus getStatusById(Integer id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Bancastatus.findByIdstatus").setParameter("idstatus", id);
        Bancastatus u = (Bancastatus) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }
}
