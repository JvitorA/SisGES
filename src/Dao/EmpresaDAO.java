/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Empresa;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Alessandro
 */
public class EmpresaDAO extends GenericDAO<Empresa> {

    public Empresa getEmpresaByNome(String nome) throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Empresa.findByNome").setParameter("nome", nome);
        Empresa u = (Empresa) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> getAllEmpresas() throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Empresa.findAll");
        return q.list();
    }
}
