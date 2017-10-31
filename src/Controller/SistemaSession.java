/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Aluno;
import Model.Assunto;
import Model.Campus;
import Model.Curso;
import Model.Empresa;
import Model.Login;
import Model.Professor;
import Model.Trabalho;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alessandro O Singleton � um dos padr�es de projecto mais simples. Ele
 * � usado quando for necess�ria a exist�ncia de apenas uma inst�ncia de uma
 * classe. Foi apresentado no GoF como um padr�o de cria��o, por lidar com a
 * cria��o de objectos.
 */
public class SistemaSession implements Serializable {

    private static SistemaSession instance = null;
    private final FacesContext facesContext;
    private HttpSession session = null;

    public static SistemaSession getInstance() {
        if (SistemaSession.instance == null) {
            SistemaSession.instance = new SistemaSession();

        }
        return SistemaSession.instance;
    }

    public void setInstance(SistemaSession instance) {
        SistemaSession.instance = instance;
        this.session.setMaxInactiveInterval(2 * 60 * 60); // two hours
    }

    private SistemaSession() {
        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(false);
    }

    /**
     * limpar todos inputs do manager bean
     *
     * @param nomeBean
     */
    public void destroyBean(String nomeBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nomeBean, null);
    }

    /**
     * Fun��o para retornar o template
     *
     * @return String
     */
    public String getTemplateAtual() {
        return (String) session.getAttribute("template");
    }

    /**
     * pega os atributos do login do professor atualmente no sistema
     *
     * @return Login
     */
    public Login getLoginAtual() {
        return (Login) session.getAttribute("login");
    }

    /**
     * retorna o professor que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Professor getProfessorAlterar() throws Exception {
        Professor p = (Professor) session.getAttribute("alterarProfessor");
        return p;
    }

    /**
     * retorna o professor do cadastro
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Login getProfessorCadastrar() throws Exception {
        Login l = (Login) session.getAttribute("cadastroProfessor");
        return l;
    }

    /**
     * retorna o aluno que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Aluno getAlunoAlterar() throws Exception {
        Aluno a = (Aluno) session.getAttribute("alterarAluno");
        return a;
    }

    /**
     * retorna o campus que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Campus getCampusAlterar() throws Exception {
        Campus c = (Campus) session.getAttribute("alterarCampus");
        return c;
    }

    /**
     * retorna o curso que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Curso getCursoAlterar() throws Exception {
        Curso c = (Curso) session.getAttribute("alterarCurso");
        return c;
    }

    /**
     * retorna a empresa que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Empresa getEmpresaAlterar() throws Exception {
        Empresa e = (Empresa) session.getAttribute("alterarEmpresa");
        return e;
    }

    /**
     * retorna o assunto que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Assunto getAssuntoAlterar() throws Exception {
        Assunto a = (Assunto) session.getAttribute("alterarAssunto");
        return a;
    }

    /**
     * retorna o trabalho que ser� alterado
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Trabalho getTrabalhoAlterar() throws Exception {
        Trabalho t = (Trabalho) session.getAttribute("alterarTrabalho");
        return t;
    }

    /**
     * retorna o trabalho
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Trabalho getTrabalho() throws Exception {
        Trabalho t = (Trabalho) session.getAttribute("Trabalho");
        return t;
    }

    /**
     * retorna o id do trabalho para saber a banca
     *
     * @return idTrabalho
     * @throws java.lang.Exception
     */
    public Integer getIdTrabalho() throws Exception {
        Integer id = (Integer) session.getAttribute("idTrabalho");
        return id;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * fun��o para verificar se est� logado corretamente se n�o sai do sistema
     *
     */
    public void verificaAcesso() {
        try {
            Login login = this.getLoginAtual();
            if (login == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                fc.getExternalContext().redirect(ec.getRequestContextPath() + "/views/login.xhtml");
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FacesContext getFacesContext() {
        return facesContext;
    }

}
