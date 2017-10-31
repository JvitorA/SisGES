/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosEmail;
import Dao.AssinaturaDAO;
import Dao.EmailConfigDAO;
import Email.CommonsEmail;
import Model.Assinatura;
import Model.EmailConfig;
import Model.Login;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "EmailBean")
@SessionScoped
public class EmailController extends DadosEmail implements Serializable {

    private final SistemaSession sessao = SistemaSession.getInstance();

    public EmailController() {

    }

    @PostConstruct
    public void init() {
        EmailConfig ec = this.getConfiguracao();
        this.setEmailConfig(ec);
    }

    public EmailConfig getConfiguracao() {
        EmailConfig ec = null;
        EmailConfigDAO ecDAO = new EmailConfigDAO();
        try {
            ec = ecDAO.getEmailConfig();
            ecDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ec;
    }

    public void enviarEmail() {
        CommonsEmail email;
        try {
            email = new CommonsEmail();
            boolean enviado = email.enviarEmail(this.getTitulo(), this.getMensagem(), this.getDestinatarios());
            if (enviado == true) {
                FacesContext.getCurrentInstance().addMessage("growlNovoEmail",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!", ""));
            } else {
                //FacesContext.getCurrentInstance().addMessage("growlNovoEmail",
                //        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
            }
        } catch (EmailException | MalformedURLException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * função que direciona para pagina do email
     *
     * @return
     */
    public String direcionaVisualizarEmail() {
        return "visualizarEmailConfig.xhtml?facesRedirect=true";
    }

    /**
     * função carregar email
     *
     * @return
     */
    public EmailConfig carregarEmailConfig() {
        sessao.verificaAcesso();
        //-------------------------------------------------------------------
        //assinatura
        Login loginAtual = sessao.getLoginAtual();
        AssinaturaDAO aDAO = new AssinaturaDAO();
        Assinatura ass;
        try {
            ass = aDAO.getAssinaturaBySiapeProfessor(loginAtual.getProfessorsiape().getSiape());
            StringBuilder msgEmail = new StringBuilder();
            msgEmail.append("<br/><br/><br/>");
            msgEmail.append(ass.getAssinatura());
            this.setMensagem(msgEmail.toString());
        } catch (Exception ex) {
            Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        aDAO.closeSession();
        //-------------------------------------------------------------------
        return this.getEmailConfig();
    }

    /**
     * altera os dados de um professor
     *
     * @throws java.lang.Exception
     */
    public void alterarEmailConfg() throws Exception {
        Boolean realizado;
        EmailConfigDAO ecDAO = new EmailConfigDAO();

        realizado = ecDAO.update(this.getEmailConfig());
        ecDAO.closeSession();

        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlEmailConfig",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlEmailConfig",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!", "Informe o admistrador."));
        }
    }

}
