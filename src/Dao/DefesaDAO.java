/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Defesa;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Alessandro
 */
public class DefesaDAO extends GenericDAO<Defesa> {

    public Defesa getDefesaByIdTrabalho(int idTrabalho) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Defesa.findByIdTrabalho").setParameter("idtrabalho", idTrabalho);
        Defesa u = (Defesa) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }
}
