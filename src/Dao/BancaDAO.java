/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Banca;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class BancaDAO extends GenericDAO<Banca> {

    @SuppressWarnings("unchecked")
    public Banca getBancaByIdTrabalho(int idTrabalho) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Banca.findByIdTrabalho").setParameter("idtrabalho", idTrabalho);
        // para retornar um objeto apenas
        return (Banca) q.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public boolean getExisteBancaByIdTrabalho(int idTrabalho) throws Exception {
        boolean encontrado = false;
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Banca.findByIdTrabalho").setParameter("idtrabalho", idTrabalho);
        List<Banca> list = q.list();
        if (list.size() > 0) {
            encontrado = true;
        }
        return encontrado;
    }
    /*
     * Função para retornar todas as bancas que um professor é orientador
     *
     */

    @SuppressWarnings("unchecked")
    public List<Banca> getAllBancasByOrientador(int siape, Date dtInicio, Date dtFinal) throws Exception {
        Session session = this.getSessionFactory().openSession();
        Query q = (Query) session.getNamedQuery("Banca.findAllByOrientador").setParameter("siape", siape).setParameter("dtinicio", dtInicio).setParameter("dtfinal", dtFinal);
        return q.list();
    }
}
