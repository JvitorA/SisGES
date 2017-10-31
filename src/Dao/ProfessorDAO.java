/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import java.util.List;
import Model.Professor;
import Model.Professororientacao;
import Model.Professororientacaoseparada;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class ProfessorDAO extends GenericDAO<Professor> {

    @SuppressWarnings("unchecked")
    public Professor getProfessorBySiape(int siape) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Professor.findBySiape").setParameter("siape", siape);
        Professor u = (Professor) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public Professor getProfessorByNome(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Professor.findByNome").setParameter("nome", nome);
        Professor u = (Professor) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Professor> getAllProfessores() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Professor.findAll");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Professor> getAllCoordenadores() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Professor.findAllCoordenador");
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Professororientacao> getAllProfessorOrientacoes(Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT DISTINCT p.siape, "
                + "p.nome, "
                + "p.email, "
                + "p.telefone, "
                + "( SELECT count(t.idtrabalho) AS count "
                + "FROM trabalho t "
                + "WHERE t.professorsiape = p.siape and t.datamatricula between :dtInicial and :dtFinal ) AS orientacoes "
                + "FROM professor p "
                + "WHERE p.ativo = true  "
                + "ORDER BY p.nome, orientacoes";

        Query query = session.createSQLQuery(strSQL).addEntity(Professororientacao.class);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return query.list();
        /*Query query = session.createSQLQuery(strSQL)
         .addScalar("siape", StandardBasicTypes.INTEGER)
         .addScalar("nome", StandardBasicTypes.STRING)
         .addScalar("telefone", StandardBasicTypes.STRING)
         .addScalar("orientacoes", StandardBasicTypes.BIG_INTEGER);
         query.setParameter("dtInicial", dtInicial);
         query.setParameter("dtFinal", dtFinal);

         return query.list();*/

    }

    /*
     * Função que retorna todos os professores e o numero de orientações
     * em estágio obrigatório e não obrigatório
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Professororientacaoseparada> getAllProfessorOrientacaoSeparada(Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = " SELECT DISTINCT p.siape, "
                + "p.nome, "
                + "p.email, "
                + "p.telefone, "
                + "(SELECT count(t.idtrabalho) AS count "
                + "FROM trabalho t,estagio e "
                + "WHERE t.professorsiape = p.siape "
                + "AND t.estagioidestagio = e.idestagio "
                + "AND t.datamatricula between :dtInicial and :dtFinal  "
                + "AND e.tipoestagio = 'Estágio Obrigatório' ) AS orientacoesEstObrigatorio, "
                + "(SELECT count(t.idtrabalho) AS count "
                + "FROM trabalho t,estagio e "
                + "WHERE t.professorsiape = p.siape "
                + "AND t.estagioidestagio = e.idestagio "
                + "AND t.datamatricula between :dtInicial and :dtFinal  "
                + "AND e.tipoestagio = 'Estágio Não Obrigatório' ) AS orientacoesEstNaoObrigatorio "
                + "FROM professor p "
                + "WHERE p.ativo = true "
                + "ORDER BY p.nome ";

        Query query = session.createSQLQuery(strSQL).addEntity(Professororientacaoseparada.class);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return (ArrayList<Professororientacaoseparada>) query.list();
    }

    /**
     * Função para retornar o maior siape
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Integer getMaiorSiape() throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT * FROM professor p WHERE p.siape = (SELECT Max(pp.siape) FROM professor pp) ";

        Query query = session.createSQLQuery(strSQL).addEntity(Professor.class);
        Professor pr = (Professor) query.uniqueResult();
        return pr.getSiape();

    }

}
