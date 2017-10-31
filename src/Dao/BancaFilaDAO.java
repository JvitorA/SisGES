/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Bancafila;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class BancaFilaDAO extends GenericDAO<Bancafila> {

    /**
     * Função para retornar um professor da banca
     *
     * @param siape
     * @return
     * @throws Exception
     */
    public Bancafila getFilaBancaBySiape(Integer siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Bancafila.findBySiape").setParameter("siape", siape);
        Bancafila u = (Bancafila) q.uniqueResult();
        return u;
    }

    /**
     * Função para retornar fila para banca
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Bancafila> getFila() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Bancafila.findAll");
        return q.list();
    }
}
