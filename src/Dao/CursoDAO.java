/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Curso;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class CursoDAO extends GenericDAO<Curso> {

    @SuppressWarnings("unchecked")
    public Curso getCursoByIdCurso(int id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Curso.findByIdcurso").setParameter("idcurso", id);
        // para retornar um objeto apenas
        Curso u = (Curso) q.uniqueResult();
        return u;
    }

    @SuppressWarnings("unchecked")
    public Curso getCursoByNomeCurso(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Curso.findByNomecurso").setParameter("nomecurso", nome);
        // para retornar um objeto apenas
        Curso u = (Curso) q.uniqueResult();
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Curso> getAllCursos() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Curso.findAll");
        return q.list();
    }
}
