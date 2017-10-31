/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Convidado;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class ConvidadoDAO extends GenericDAO<Convidado> {

    @SuppressWarnings("unchecked")
    public boolean deletarTodosConvidadosDeUmaBanca(int idBanca) throws Exception {
        Session session = this.getSession();
        boolean realizado = false;
        String hql = "DELETE FROM Convidado c WHERE c.bancaidbanca.idbanca = :idbanca";
        Query q = session.createQuery(hql).setParameter("idbanca", idBanca);
        q.executeUpdate();
        int r = q.executeUpdate();
        if (r == 1) {
            realizado = true;
        }
        return realizado;
    }

    /**
     * Função para retornar os convidados de uma banca
     *
     * @param idBanca
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Convidado> getAllConvidadosFromBanca(int idBanca) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Convidado.findByIdbanca").setParameter("idbanca", idBanca);
        return q.list();
    }

    /**
     * Função para retornar os convidados confirmados de uma banca
     *
     * @param idBanca
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Convidado> getAllConvidadosConfirmadosFromBanca(int idBanca) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Convidado.findByIdbancaConvidadoConfirmado").setParameter("idbanca", idBanca);
        return q.list();
    }

    public boolean getExisteConvidadoById(int id) throws Exception {
        Session session = this.getSession();
        boolean existe = false;
        Query q = (Query) session.getNamedQuery("Convidado.findByIdconvidado").setParameter("idconvidado", id);
        // para retornar um objeto apenas
        Convidado c = (Convidado) q.uniqueResult();
        if (c != null) {
            existe = true;
        }
        return existe;
    }

    public Convidado getExisteConvidadoFromBancaBySiape(int idbanca, int siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Convidado.findFromBancaBySiape").setParameter("idbanca", idbanca).setParameter("siape", siape);
        // para retornar um objeto apenas
        Convidado c = (Convidado) q.uniqueResult();
        return c;
    }

    @SuppressWarnings("unchecked")
    public List<Convidado> getConvidadoFromBancaBySiapeConfirmado(int siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Convidado.findFromBancaBySiapeAndConfirmado").setParameter("siape", siape);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Convidado> getConvidadoFromBancaBySiapeConfirmadoInPeriodo(int siape, Date dtInicio, Date dtFinal) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Convidado.findFromBancaBySiapeAndConfirmadoInPeriodo").setParameter("siape", siape).setParameter("dtinicio", dtInicio).setParameter("dtfinal", dtFinal);
        return q.list();
    }
}
