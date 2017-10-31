/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Assinatura;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class AssinaturaDAO extends GenericDAO<Assinatura> {

    public Assinatura getAssinaturaBySiapeProfessor(int siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Assinatura.findBySiapeProfessor").setParameter("siape", siape);
        Assinatura u = (Assinatura) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }
}
