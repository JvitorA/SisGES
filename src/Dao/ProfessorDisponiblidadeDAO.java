/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Professordisponibilidade;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class ProfessorDisponiblidadeDAO extends GenericDAO<Professordisponibilidade> {

    @SuppressWarnings("unchecked")
    public List<Professordisponibilidade> getDisponibilidadeBySiape(int siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Professordisponibilidade.findBySiape").setParameter("siape", siape);
        return q.list();
    }
}
