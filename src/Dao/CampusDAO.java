/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Campus;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class CampusDAO extends GenericDAO<Campus> {

    /*
     * Função para retornar todos os campus
     *
     */
    @SuppressWarnings("unchecked")
    public List<Campus> getAllCampus() throws Exception {
        Session session = this.getSessionFactory().openSession();
        Query q = (Query) session.getNamedQuery("Campus.findAll");
        return q.list();
    }

    /*
     * Função para retornar o campus pelo nome
     *
     */
    public Campus getCampusByNome(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Campus.findByNomecampus").setParameter("nomecampus", nome);
        // para retornar um objeto apenas
        return (Campus) q.uniqueResult();
    }

    /*
     * Função para retornar o campus pelo id
     *
     */
    public Campus getCampusById(int id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Campus.findByIdcampus").setParameter("idcampus", id);
        // para retornar um objeto apenas
        return (Campus) q.uniqueResult();
    }
}
