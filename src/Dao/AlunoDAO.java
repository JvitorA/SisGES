/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Aluno;
import Model.Alunoaprovado;
import Model.Alunomatriculado;
import Model.Sumarioaprovado;
import Model.Sumariomatricula;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class AlunoDAO extends GenericDAO<Aluno> {

    @SuppressWarnings("unchecked")
    public List<Aluno> getAlunoByCurso(int idCurso) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Aluno.findByAlunoByIdCurso").setParameter("idcurso", idCurso);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Aluno> getAlunoByNomeAndCurso(String nome, int idCurso) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Aluno.findByNomeAndIdCurso").setParameter("nome", nome + "%").setParameter("idcurso", idCurso);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<Aluno> getAlunoByNome(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Aluno.findByNome").setParameter("nome", nome + "%");
        return q.list();
    }

    public Aluno getAlunoByMatricula(String matricula) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Aluno.findByMatricula").setParameter("matricula", matricula);
        Aluno u = (Aluno) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

    @SuppressWarnings("unchecked")
    public List<Aluno> getAllAlunos() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Aluno.findAll");
        return q.list();
    }

    /**
     * Função para trazer dados do sumario de matriculas
     *
     * @param nomeCurso
     * @param dtInicial
     * @param dtFinal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Sumariomatricula> getSumarioMatricula(String nomeCurso, Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT 'Matricula' AS status, "
                + "est.tipoestagio, "
                + "(SELECT count(a.*) AS count "
                + "FROM aluno a, "
                + "trabalho t, "
                + "trabalhostatus ts, "
                + "estagio e, curso c "
                + "WHERE e.tipoestagio = est.tipoestagio "
                + "and a.cursoidcurso = c.idcurso "
                + "and c.nomecurso = :curso "
                + "AND t.alunomatricula = a.matricula "
                + "AND t.estagioidestagio = e.idestagio "
                + "AND t.statusidstatus = ts.idstatus "
                + "AND t.datamatricula between :dtInicial and :dtFinal "
                + "GROUP BY e.tipoestagio) AS matriculas "
                + "FROM estagio est ORDER BY est.tipoestagio";

        /* strSQL = "SELECT 'Matricula' as status,e.tipoestagio,count(a.*) as matriculas "
         + "FROM aluno a, trabalho t,estagio e, curso c "
         + "WHERE  "
         + "t.alunomatricula = matricula  "
         + "and t.estagioidestagio = e.idestagio "
         + "and a.cursoidcurso = c.idcurso "
         + "and c.nomecurso = :curso "
         + "and t.datamatricula between :dtInicial and :dtFinal  "
         + "group by e.tipoestagio "
         + "order by e.tipoestagio ";*/
        Query query = session.createSQLQuery(strSQL).addEntity(Sumariomatricula.class);
        query.setParameter("curso", nomeCurso);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return (ArrayList<Sumariomatricula>) query.list();
    }

    /**
     * Função para trazer dados do sumario de atividades do relatorio anual
     *
     * @param nomeCurso
     * @param dtInicial
     * @param dtFinal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Sumarioaprovado> getSumarioAprovado(String nomeCurso, Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT 'Aprovados' AS status, "
                + "est.tipoestagio, "
                + "( SELECT count(a.*) AS count "
                + "FROM aluno a, "
                + "trabalho t, "
                + "trabalhostatus ts, "
                + "estagio e, "
                + "curso c "
                + "WHERE t.alunomatricula = a.matricula "
                + "AND t.estagioidestagio = e.idestagio "
                + "AND e.idestagio = est.idestagio "
                + "AND t.statusidstatus = ts.idstatus "
                + "AND ts.tipostatus = 'Finalizado' "
                + "AND a.cursoidcurso = c.idcurso "
                + "AND c.nomecurso = :curso "
                + "AND t.datafinalizacao between :dtInicial and :dtFinal "
                + "GROUP BY est.tipoestagio) AS aprovados "
                + "FROM estagio est "
                + "ORDER BY est.tipoestagio ";

        Query query = session.createSQLQuery(strSQL).addEntity(Sumarioaprovado.class);
        query.setParameter("curso", nomeCurso);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return (ArrayList<Sumarioaprovado>) query.list();
    }

    /**
     * Função para trazer dados dos alunos matriculados no periodo em um curso
     *
     * @param nomeCurso
     * @param dtInicial
     * @param dtFinal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Alunomatriculado> getAlunoMatriculado(String nomeCurso, Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT e.tipoestagio, "
                + "a.matricula, "
                + "a.nome, "
                + "p.nome AS orientador, "
                + "t.datamatricula "
                + "FROM aluno a, "
                + "trabalho t, "
                + "trabalhostatus ts, "
                + "estagio e, "
                + "curso c, "
                + "professor p "
                + "WHERE t.alunomatricula = a.matricula "
                + "AND t.estagioidestagio = e.idestagio  "
                + "AND t.statusidstatus = ts.idstatus  "
                + "AND t.professorsiape = p.siape  "
                + "AND a.cursoidcurso = c.idcurso  "
                + "AND c.nomecurso = :curso "
                + "AND t.datamatricula between :dtInicial and :dtFinal "
                + "GROUP BY e.tipoestagio, a.matricula, p.nome, t.datamatricula "
                + "ORDER BY e.tipoestagio; ";

        Query query = session.createSQLQuery(strSQL).addEntity(Alunomatriculado.class);
        query.setParameter("curso", nomeCurso);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return (ArrayList<Alunomatriculado>) query.list();
    }

    /**
     * Função para trazer dados dos alunos que terminaram estagio obrigatorio no
     * periodo em um curso
     *
     * @param nomeCurso
     * @param dtInicial
     * @param dtFinal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Alunoaprovado> getAlunoAprovadosEstagioObrigatorio(String nomeCurso, Date dtInicial, Date dtFinal) throws Exception {
        Session session = this.getSession();
        String strSQL;

        strSQL = "SELECT e.tipoestagio, "
                + "a.matricula,  "
                + "a.nome, "
                + "p.nome AS orientador, "
                + "t.datamatricula, "
                + "t.datafinalizacao "
                + "FROM aluno a, "
                + "trabalho t, "
                + "trabalhostatus ts, "
                + "estagio e, "
                + "curso c, "
                + "professor p "
                + "WHERE t.alunomatricula = a.matricula "
                + "AND t.estagioidestagio = e.idestagio  "
                + "AND e.tipoestagio = 'Estágio Obrigatório' "
                + "AND t.statusidstatus = ts.idstatus  "
                + "AND ts.tipostatus = 'Finalizado' "
                + "AND t.professorsiape = p.siape  "
                + "AND a.cursoidcurso = c.idcurso "
                + "AND c.nomecurso = :curso  "
                + "AND t.datafinalizacao between :dtInicial and :dtFinal  "
                + "GROUP BY e.tipoestagio, a.matricula, p.nome, t.datamatricula, t.datafinalizacao "
                + "ORDER BY e.tipoestagio";

        Query query = session.createSQLQuery(strSQL).addEntity(Alunoaprovado.class);
        query.setParameter("curso", nomeCurso);
        query.setParameter("dtInicial", dtInicial);
        query.setParameter("dtFinal", dtFinal);
        return (ArrayList<Alunoaprovado>) query.list();
    }

}
