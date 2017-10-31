/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosAssunto;
import Dao.AssuntoDAO;
import Model.Assunto;
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
@ManagedBean(name = "AssuntoBean")
@SessionScoped
public class AssuntoController extends DadosAssunto {

    private String mensagemErro = "";
    private String enviarEmail = "";
    private Assunto assunto = new Assunto();
    private final SistemaSession sessao = SistemaSession.getInstance();

    public AssuntoController() {
    }

    /**
     * retornar todos assuntos cadastrados
     *
     */
    @PostConstruct
    public void init() {
        try {
            this.getListaAssuntos().clear();
            AssuntoDAO aDAO = new AssuntoDAO();
            List<Assunto> allAssunto = aDAO.getAllAssuntos();
            this.setListaAssuntos(allAssunto);
            aDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * função que direciona para pagina de visualizar
     *
     * @return
     */
    public String direcionaVisualizarAssunto() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("AssuntoBean");
        return "visualizarAssunto.xhtml?facesRedirect=true";
    }

    /**
     * validacao de um cadastro de assunto
     *
     * @return pagina
     * @throws java.lang.Exception
     */
    public boolean isValido() throws Exception {
        boolean isValido = true;

        if (assunto.getNome().equals("") || assunto.getNome() == null) {
            FacesContext.getCurrentInstance().addMessage("growlAssunto",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite a área ou assunto do estágio.", ""));
            isValido = false;
        }

        return isValido;
    }

    /**
     * cadastro de assunto
     *
     * @throws java.lang.Exception
     */
    public void cadastrar() throws Exception {
        if (this.isValido()) {
            AssuntoDAO cDAO = new AssuntoDAO();
            if (cDAO.getAssuntoByNome(assunto.getNome()) != null) {
                FacesContext.getCurrentInstance().addMessage("growlMessage",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Área já cadastrada.", ""));
            } else {
                String nome = assunto.getNome();
                nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
                assunto.setNome(nome);
                cDAO.insert(assunto);
                cDAO.closeSession();
                FacesContext.getCurrentInstance().addMessage("growlMessage",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));

            }
        }
    }

    /**
     * alterar o assunto do estágio
     *
     * @throws java.lang.Exception
     */
    public void alterarAssunto() throws Exception {
        Boolean realizado;
        AssuntoDAO aDAO = new AssuntoDAO();
        Assunto aTemp = sessao.getAssuntoAlterar();
        this.getAssunto().setIdassunto(aTemp.getIdassunto());
        realizado = aDAO.update(this.getAssunto());
        aDAO.closeSession();
        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlAssunto",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlAssunto",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!\n Informe o admistrador.", ""));
        }
    }

    /**
     * direciona para pagina de alteração de dados de um professor
     *
     * @param a
     * @return
     */
    public String direcionaAlterarAssunto(Assunto a) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("alterarAssunto", a);
        return "alteraAssunto.xhtml?facesRedirect=true";
    }

    /**
     * retorna o professor que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Assunto getAssuntoAlterar() throws Exception {
        sessao.verificaAcesso();
        this.assunto = sessao.getAssuntoAlterar();
        return assunto;
    }

    public boolean isErro() {
        return mensagemErro == null || (mensagemErro.equals(""));
    }

    public String validaDadosAssunto() {
        int erro = 0;
        if (assunto.getNome().equals("")) {
            this.setMensagemErro("Preencher campo nome.");
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

    public Assunto getAssunto() {
        return assunto;
    }

    public void setAssunto(Assunto assunto) {
        this.assunto = assunto;
    }

}
