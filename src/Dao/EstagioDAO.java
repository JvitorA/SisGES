/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Estagio;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class EstagioDAO extends GenericDAO<Estagio> {

    @SuppressWarnings("unchecked")
    public List<Estagio> getAllEstagios() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Estagio.findAll");
        return q.list();
    }

    public Estagio getEstagioByTipo(String tipo) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Estagio.findByTipoestagio").setParameter("tipoestagio", tipo);
        Estagio u = (Estagio) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }
}
