/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Aluno;
import Model.Empresa;
import Model.Professor;
import Model.Trabalho;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class TrabalhoDAO extends GenericDAO<Trabalho> {

    @SuppressWarnings("unchecked")
    public Trabalho getTrabalhoByTitulo(String titulo) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByTitulo").setParameter("titulo", titulo);
        Trabalho u = (Trabalho) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public Trabalho getTrabalhoById(Integer id) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByIdtrabalho").setParameter("idtrabalho", id);
        Trabalho u = (Trabalho) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Trabalho> getAllTrabalhos() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findAll");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalho> getAllTrabalhosHaveDefesa() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByHasDefesaNotBanca");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalho> getAllTrabalhosByOrientador(Integer siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByOrientador").setParameter("siape", siape);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalho> getTrabalhos(Aluno a, Empresa e, String titulo, String tipoestagio, Integer siape, String nomeProf, Date dataMatricula) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByAlunoAndTituloAndStatusAndOrientador").setParameter("aluno", a).setParameter("empresa", e).setParameter("titulo", titulo).setParameter("tipoestagio", tipoestagio).setParameter("siape", siape).setParameter("nomeProf", nomeProf).setParameter("datamatricula", dataMatricula);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalho> getTrabalhosByConvidado(Aluno a, Empresa e, String titulo, String tipoestagio, Integer siape, String nomeProf, Date dataMatricula, String local) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Trabalho.findByConvidado").setParameter("aluno", a).setParameter("empresa", e).setParameter("titulo", titulo).setParameter("tipoestagio", tipoestagio).setParameter("siape", siape).setParameter("nomeProf", nomeProf).setParameter("datamatricula", dataMatricula).setParameter("local", local);
        return q.list();
    }
}
