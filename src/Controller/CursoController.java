/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosCurso;
import Dao.CursoDAO;
import Model.Curso;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "CursoBean")
@SessionScoped
public class CursoController extends DadosCurso {

    private String mensagemErro = "";
    private String enviarEmail = "";
    private Curso curso = new Curso();
    private final SistemaSession sessao = SistemaSession.getInstance();

    public CursoController() {
    }

    /**
     * retornar todos cursos cadastrados
     *
     */
    @PostConstruct
    public void init() {
        try {
            this.getListaCursos().clear();
            CursoDAO cDAO = new CursoDAO();
            List<Curso> allCursos = cDAO.getAllCursos();
            this.setListaCursos(allCursos);
            cDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * função que direciona para pagina de visualizar
     *
     * @return
     */
    public String direcionaVisualizarCurso() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("CursoBean");
        return "visualizarCurso.xhtml?facesRedirect=true";
    }

    /**
     * valida um cadastro de curso
     *
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValido() throws Exception {
        boolean isValido = true;

        if (curso.getNomecurso().equals("") || curso.getNomecurso() == null) {
            FacesContext.getCurrentInstance().addMessage("growlCurso",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome do curso.", ""));
            isValido = false;
        }

        if (curso.getHorasobrigatorias() == 0 || curso.getHorasobrigatorias() == null) {
            FacesContext.getCurrentInstance().addMessage("growlCurso",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite as horas obrigatórias.", ""));
            isValido = false;
        }

        return isValido;
    }

    /**
     * cadastra um curso
     *
     * @throws java.lang.Exception
     */
    public void cadastrar() throws Exception {
        if (this.isValido()) {
            CursoDAO cDAO = new CursoDAO();
            String nome = curso.getNomecurso();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
            curso.setNomecurso(nome);
            if (cDAO.getCursoByNomeCurso(curso.getNomecurso()) != null) {
                FacesContext.getCurrentInstance().addMessage("growlCurso",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Curso já cadastrado.", ""));
            } else {
                cDAO.insert(curso);
                cDAO.closeSession();
                FacesContext.getCurrentInstance().addMessage("growlCurso",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
            }
        }
    }

    /**
     * altera os dados de um curso
     *
     * @throws java.lang.Exception
     */
    public void alterarCurso() throws Exception {
        Boolean realizado;
        CursoDAO cDAO = new CursoDAO();
        Curso cTemp = sessao.getCursoAlterar();
        this.getCurso().setIdcurso(cTemp.getIdcurso());
        realizado = cDAO.update(this.getCurso());
        cDAO.closeSession();
        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlCurso",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlCurso",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!\n Informe o admistrador.", ""));
        }
    }

    /**
     * direciona para pagina de alteração de dados de um curso
     *
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarCurso(Curso p) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("alterarCurso", p);
        return "alteraCurso.xhtml?facesRedirect=true";
    }

    /**
     * retorna o curso que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Curso getCursoAlterar() throws Exception {
        sessao.verificaAcesso();
        this.curso = sessao.getCursoAlterar();
        return curso;
    }

    public boolean isErro() {
        if (mensagemErro != null && (!(mensagemErro.equals("")))) {
            return false;
        } else {
            return true;
        }
    }

    public String validaDadosCurso() {
        int erro = 0;
        if (curso.getNomecurso().equals("")) {
            this.setMensagemErro("Preencher campo matricula.");
            erro++;
        }
        if (erro == 0) {
            this.setMensagemErro("");
        }

        return this.getMensagemErro();
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public String getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(String enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

}
