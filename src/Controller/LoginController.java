/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.LoginDAO;
import Email.CommonsEmail;
import Model.Login;
import Util.Functions;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "LoginBean")
@ViewScoped
public final class LoginController implements Serializable {

    private String mensagemErro;
    private String novaSenha;
    private String novaSenhaCopy;
    private SistemaSession sessao = SistemaSession.getInstance();
    private Dialog trocaSenha = new Dialog();
    protected Login login = new Login();

    public LoginController() {
        this.setMensagemErro("");
        this.setNovaSenha("");
        this.setNovaSenhaCopy("");
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {

    }

    /**
     * Entrar no sistema
     *
     * @return pagina
     * @throws java.lang.Exception
     */
    public String entrar() throws Exception {
        if (!(validaDadosLogin().equals(""))) {
            return "login.xhtml?facesRedirect=true";
        } else {
            LoginDAO lDAO = new LoginDAO();
            if (lDAO.getLoginProfessor(login.getProfessorsiape().getSiape(), Functions.senhaMD5(login.getSenha())) != null) {
                login = lDAO.getLoginProfessor(login.getProfessorsiape().getSiape(), Functions.senhaMD5(login.getSenha()));

                if (login.getProfessorsiape().getAtivo() == true) {
                    lDAO.closeSession();
                    SistemaSession instance = SistemaSession.getInstance();
                    instance.getSession().setAttribute("login", login);
                    
                    System.out.println("oi");
                    if(true) {
                    	System.out.println("Stop");
                    }

                    if (login.getProfessorsiape().getPerfilidperfil().getNomeperfil().equals("Professor")) {
                        instance.getSession().setAttribute("template", "./../templates/templateProfessor.xhtml");
                    } else {
                        instance.getSession().setAttribute("template", "./../templates/template.xhtml");
                    }

                    return "sistema.xhtml?facesRedirect=true";

                } else {
                    lDAO.closeSession();
                    login.setSenha("");
                    FacesContext.getCurrentInstance().addMessage("growlLogin",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso Inativo!", "Procure o Coordenador de Estágios."));
                    return "login.xhtml?facesRedirect=true";
                }

            } else {
                lDAO.closeSession();
                login.setSenha("");
                FacesContext.getCurrentInstance().addMessage("growlLogin",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "O Siape ou a Senha digitada está incorreto.", ""));
                return "login.xhtml?facesRedirect=true";
            }
        }
    }

    /**
     * abrir dialogo para alterar senha
     *
     */
    public void esqueceuSenha() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('dlgEsqueceuSenha').show()");
    }

    /**
     * Sair do sistema
     *
     * @return
     */
    public String logoff() {
        //----------------------------------------------
        //sessao.setSession(null);
        //sessao.setInstance(null);
        //sessao.getSession().invalidate();
        SistemaSession instance = SistemaSession.getInstance();
        instance.getSession().setAttribute("login", null);
        //----------------------------------------------
        //FacesContext.getCurrentInstance().addMessage("growlMenu", new FacesMessage(FacesMessage.SEVERITY_INFO, "Saída realizada com sucesso!", ""));
        return "/views/login.xhtml?faces-redirect=true";
    }

    /**
     * função para verificar se está logado corretamente se não sai do sistema
     *
     */
    public void verificaAcesso() {
        try {
            login = sessao.getLoginAtual();
            if (login == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                fc.getExternalContext().redirect(ec.getRequestContextPath() + "/views/login.xhtml");
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Solicitar troca de senha
     *
     * @throws java.lang.Exception
     */
    public void solicitacaoTrocaSenha() throws Exception {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validaSiape() == true) {
            LoginDAO lDAO = new LoginDAO();
            if (lDAO.findBySiapeProfessor(login.getProfessorsiape().getSiape()) == false) {
                FacesContext.getCurrentInstance().addMessage("growlEsqueceuSenha",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Siape não cadastrado no sistema!\nVerifique se o número está correto.", ""));
            } else {
                Login lTemp;
                lTemp = lDAO.getBySiapeProfessor(login.getProfessorsiape().getSiape());
                CommonsEmail email = new CommonsEmail();
                String senhaTemp;
                String senhaBanco;

                senhaTemp = Functions.gerarSenhaTemporaria();
                senhaBanco = Functions.senhaMD5(senhaTemp);
                lTemp.setSenha(senhaBanco);
                lDAO.update(lTemp);
                lDAO.closeSession();
                this.emailNovaSenha(senhaTemp, lTemp);
                FacesContext.getCurrentInstance().addMessage("growlEsqueceuSenha",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação realizada com sucesso!\nVerifique seu email \n" + lTemp.getProfessorsiape().getEmail(), ""));
            }
        } else {
            context.execute("dlg.show()");
        }
    }

    /**
     * Função para criar email com nova senha
     *
     * @param senhaTemp
     * @param lTemp
     * @throws org.apache.commons.mail.EmailException
     * @throws java.net.MalformedURLException
     * @throws java.lang.Exception
     */
    public void emailNovaSenha(String senhaTemp, Login lTemp) throws EmailException, MalformedURLException, Exception {
        CommonsEmail email = new CommonsEmail();
        String titulo;
        titulo = "Solicitação de troca de senha sistema SisGES";
        StringBuilder msgEmail = new StringBuilder();
        msgEmail.append("<html><body>");
        msgEmail.append("<h2>SisGES - Sistema para Gestão de Estágios Supervisionados</h2>");
        msgEmail.append("<br/>");
        msgEmail.append("<h3>Sua nova senha é: ").append(senhaTemp).append("</h3>");
        msgEmail.append("<br/>");
        msgEmail.append("</body></html>");
        boolean enviado = email.enviarEmail(titulo, msgEmail.toString(), lTemp.getProfessorsiape().getEmail());
        if (enviado == true) {
            FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + lTemp.getProfessorsiape().getEmail(), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
        }
    }

    /**
     * Alterar senha
     *
     * @throws java.lang.Exception
     */
    public void alterarSenha() throws Exception {

        //pega valor do formulario
        String senhaAnterior = login.getSenha();
        String senhaAnteriorMD5 = Functions.senhaMD5(senhaAnterior);
        String senhaNova = this.getNovaSenha();
        String senhaCopy = this.getNovaSenhaCopy();

        //buscar usuario atual na base
        login = sessao.getLoginAtual();
        LoginDAO lDAO = new LoginDAO();
        if (lDAO.getLoginProfessor(login.getProfessorsiape().getSiape(), senhaAnteriorMD5) != null) {
            if (senhaNova.equalsIgnoreCase(senhaCopy) == true) {
                String senhaTemp = Functions.senhaMD5(senhaNova);
                login.setSenha(senhaTemp);
                lDAO.update(login);
                lDAO.closeSession();
                FacesContext.getCurrentInstance().addMessage("growlAlterarSenha",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração realizado com sucesso!", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlAlterarSenha",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "As senhas novas são diferentes!", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("growlAlterarSenha",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha antiga errada!", ""));
        }
    }

    public boolean validaSiape() throws Exception {
        boolean realizado = true;
        if (login.getProfessorsiape().getSiape() == 0 || login.getProfessorsiape().getSiape() == null) {
            realizado = false;
        }
        return realizado;
    }

    public String validaDadosLogin() {
        int erro = 0;
        if (login.getSenha().equals("")) {
            this.setMensagemErro("Preencher campo Senha.");
            erro++;
        }
        if (login.getProfessorsiape().getSiape() == null) {
            this.setMensagemErro("Preencher campo Siape.");
            erro++;
        }

        if (erro == 0) {
            this.setMensagemErro("");
        }

        return this.getMensagemErro();
    }

    /**
     * pega os atributos do professor atualmente logado no sistema
     *
     * @return Login
     * @throws java.lang.Exception
     */
    public Login getLoginAtual() throws Exception {
        sessao.verificaAcesso();
        login = sessao.getLoginAtual();
        if (login == null) {
            this.verificaAcesso();
        }
        return login;
    }

    public boolean isExibirErro() {
        return mensagemErro != null && (!(mensagemErro.equals("")));
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Dialog getTrocaSenha() {
        return trocaSenha;
    }

    public void setTrocaSenha(Dialog trocaSenha) {
        this.trocaSenha = trocaSenha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getNovaSenhaCopy() {
        return novaSenhaCopy;
    }

    public void setNovaSenhaCopy(String novaSenhaCopy) {
        this.novaSenhaCopy = novaSenhaCopy;
    }
}
