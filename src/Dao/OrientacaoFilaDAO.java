/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Orientacaofila;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class OrientacaoFilaDAO extends GenericDAO<Orientacaofila> {

    /**
     * Função para retornar um professor para orientacao
     *
     * @param siape
     * @return
     * @throws Exception
     */
    public Orientacaofila getFilaBancaBySiape(Integer siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Orientacaofila.findBySiape").setParameter("siape", siape);
        Orientacaofila u = (Orientacaofila) q.uniqueResult();
        return u;
    }

    /**
     * Função para retornar fila para orientação
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Orientacaofila> getFila() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Orientacaofila.findAll");
        return q.list();
    }
}
