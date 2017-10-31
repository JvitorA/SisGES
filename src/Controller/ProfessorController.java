/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosProfessor;
import Dao.AssinaturaDAO;
import Dao.LoginDAO;
import Dao.ProfessorDAO;
import Dao.ProfessorDisponiblidadeDAO;
import Email.CommonsEmail;
import Model.Assinatura;
import Model.Login;
import Model.Professor;
import Model.Professordisponibilidade;
import Util.Functions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "ProfessorBean")
@SessionScoped
public class ProfessorController extends DadosProfessor {

    private String mensagemErro;
    private String enviarEmail;
    private Professor professor;
    private Login login = new Login();
    private final SistemaSession sessao;

    public ProfessorController() {
        this.sessao = SistemaSession.getInstance();
        this.mensagemErro = "";
        this.enviarEmail = "";
        this.professor = new Professor();
    }

    @PostConstruct
    public void init() {
        try {
            this.getListaProfessores().clear();
            ProfessorDAO pDAO = new ProfessorDAO();
            List<Professor> allProfessores = pDAO.getAllProfessores();
            this.setListaProfessores(allProfessores);
            pDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(ProfessorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * função que direciona para pagina de visualizar
     *
     * @return
     */
    public String direcionaVisualizarProfessor() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("ProfessorBean");
        return "visualizarProfessor.xhtml?facesRedirect=true";
    }

    /**
     * valida a disponibilidade de um professor
     *
     * @param opcao
     * @return
     */
    public boolean isValidoDisponibilidade(int opcao) {
        boolean isValido;
        int contarSelecionados = 0;

        for (int i = 0; i < this.getListaDisponibilidade().size(); i++) {
            Professordisponibilidade pd = this.getListaDisponibilidade().get(i);
            if (pd.getManha() == true || pd.getTarde() == true || pd.getNoite() == true) {
                contarSelecionados++;
            }
        }
        if (contarSelecionados > 0) {
            isValido = true;
            if (opcao == 0) {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('dlgEmail').show()");
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("growlProfessorDisponibilidade",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione os dias de disponibilidade para participar de banca.", ""));
            isValido = false;
        }
        return isValido;
    }

    /**
     * valida alteracao de professor
     *
     * @param opcao
     * @return
     */
    public boolean isValidoAlteracao(int opcao) {
        boolean isValido = true;

        if (this.getProfessor().getNome().equals("") || this.getProfessor().getNome() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome.", ""));
            isValido = false;
        }

        if (this.getProfessor().getEmail().equals("") || this.getProfessor().getEmail() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o email.", ""));
            isValido = false;
        }

        if (this.getProfessor().getTelefone().equals("") || this.getProfessor().getTelefone() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o telefone.", ""));
            isValido = false;
        }

        if (this.isUsuarioProfessor() == false) {
            if (this.getProfessor().getPerfilidperfil().getIdperfil() == null) {
                FacesContext.getCurrentInstance().addMessage("growlProfessor",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o perfil.", ""));
                isValido = false;
            }
        }

        if (isValido == true && opcao == 0) {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabProfessorAlterar').select(1)");
        }

        return isValido;
    }

    /**
     * valida um cadastra de professor
     *
     * @param opcao
     * @return
     */
    public boolean isValidoAssinatura(int opcao) {
        boolean isValido = false;
        if (!this.getAssinatura().equalsIgnoreCase("")) {
            isValido = true;
        }
        if (opcao == 0 && isValido == true) {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabProfessor').select(2)");
        }

        if (opcao == 1 && isValido == true) {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabProfessorAlterar').select(2)");
        }
        return isValido;
    }

    /**
     * valida um cadastra de professor
     *
     * @param opcao
     * @return
     */
    public boolean isValidoCadastro(int opcao) {
        boolean isValido = true;

        if (login.getProfessorsiape().getNome().equals("") || login.getProfessorsiape().getNome() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getSiape() == 0 || login.getProfessorsiape().getSiape() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o siape.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getEmail().equals("") || login.getProfessorsiape().getEmail() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o email.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getTelefone().equals("") || login.getProfessorsiape().getTelefone() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o telefone.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getPerfilidperfil().getIdperfil() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o perfil.", ""));
            isValido = false;
        }

        if (login.getSenha().equals("") || login.getSenha() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite a senha.", ""));
            isValido = false;
        }

        if (isValido == true && opcao == 0) {
            RequestContext rc = RequestContext.getCurrentInstance();
            //rc.execute("PF('dlgEmail').show()");
            //rc.execute("PF('dlgDisponibilidade').show()");
            //rc.execute("PF('wizardProfessor').next()");
            //rc.execute("PF('wizardProfessor').loadStep('dadosDisponibilidade',true)");
            //rc.execute("PF('tabProfessor').loadStep('dadosDisponibilidade',true)");
            rc.execute("PF('tabProfessor').select(1)");
        }
        return isValido;
    }

    /**
     * cadastra um professor
     *
     */
    public void cadastrar() {
        if (this.isValidoCadastro(1) == true && this.isValidoDisponibilidade(1) == true) {

            try {
                ProfessorDAO pDAO = new ProfessorDAO();
                if (pDAO.getProfessorBySiape(login.getProfessorsiape().getSiape()) == null) {

                    String nome = login.getProfessorsiape().getNome();
                    nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
                    login.getProfessorsiape().setNome(nome);
                    login.getProfessorsiape().setAtivo(true);
                    pDAO.insert(login.getProfessorsiape());
                    pDAO.closeSession();

                    //----------------------------------------------------------
                    //assinatura
                    AssinaturaDAO aDAO = new AssinaturaDAO();
                    Assinatura ass = new Assinatura();
                    ass.setProfessorsiape(login.getProfessorsiape());
                    ass.setAssinatura(this.getAssinatura());
                    aDAO.insert(ass);
                    aDAO.closeSession();
                    //----------------------------------------------------------
                    //login
                    boolean realizado;
                    LoginDAO lDAO = new LoginDAO();
                    String senhaTemp = login.getSenha();
                    login.setSenha(Functions.senhaMD5(senhaTemp));

                    realizado = lDAO.insert(login);
                    lDAO.closeSession();
                    //----------------------------------------------------------

                    if (realizado == true) {
                        /*salvar disponibilidade*/
                        for (int i = 0; i < this.getListaDisponibilidade().size(); i++) {
                            Professordisponibilidade pd = this.getListaDisponibilidade().get(i);
                            if (pd.getManha() == true || pd.getTarde() == true || pd.getNoite() == true) {
                                Professordisponibilidade prof = new Professordisponibilidade();
                                prof.setDiasemana(pd.getDiasemana());
                                prof.setManha(pd.getManha());
                                prof.setTarde(pd.getTarde());
                                prof.setNoite(pd.getNoite());
                                prof.setProfessorsiape(login.getProfessorsiape());
                                ProfessorDisponiblidadeDAO pdDAO = new ProfessorDisponiblidadeDAO();
                                pdDAO.insert(prof);
                                pdDAO.closeSession();
                            }
                        }
                        /*enviar email informando do cadastro*/
                        if (this.getEnviarEmail().equals("sim")) {
                            CommonsEmail email = new CommonsEmail();
                            StringBuilder msgEmail = new StringBuilder();
                            msgEmail.append("<html><body>");
                            msgEmail.append("<h1>SisGES - Sistema de Gestão de Estágios Supervisionados</h1>");
                            msgEmail.append("<br/>");
                            msgEmail.append("<h3>Dados de Acesso:</h3>");
                            msgEmail.append("Siape: ").append(login.getProfessorsiape().getSiape().toString()).append("<br/>");
                            msgEmail.append("Senha: ").append(senhaTemp).append("");
                            msgEmail.append("<br/>");
                            msgEmail.append("</body></html>");
                            String titulo = "Login Sistema SisGES";
                            boolean enviado = email.enviarEmail(titulo, msgEmail.toString(), login.getProfessorsiape().getEmail());

                            if (enviado == true) {
                                FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + login.getProfessorsiape().getEmail(), ""));
                            } else {
                                FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
                            }
                            //----------------------------------------------------------------------------
                            FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!\nEmail enviado para " + login.getProfessorsiape().getEmail(), ""));
                        } else {
                            FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao realizar o cadastro. Informe ao administrador.", ""));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("growlProfessorSalvar",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Professor já cadastrado.", ""));
                }

            } catch (Exception ex) {
                System.err.println(ex.getCause());
            }
        }
    }

    /**
     * valida alteracao de professor
     *
     * @return
     */
    public boolean isValidoUsuarioAlteracao() {
        boolean isValido = true;

        if (login.getProfessorsiape().getNome().equals("") || login.getProfessorsiape().getNome() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getEmail().equals("") || login.getProfessorsiape().getEmail() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o email.", ""));
            isValido = false;
        }

        if (login.getProfessorsiape().getTelefone().equals("") || login.getProfessorsiape().getTelefone() == null) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o telefone.", ""));
            isValido = false;
        }

        return isValido;
    }

    /**
     * altera os dados do usuario atual
     *
     * @throws java.lang.Exception
     */
    public void alterarLogin() throws Exception {

        if (this.isValidoUsuarioAlteracao()) {
            ProfessorDAO pDAO = new ProfessorDAO();
            login.getProfessorsiape().setAtivo(true);

            Login lTemp = sessao.getLoginAtual();
            login.setIdlogin(lTemp.getIdlogin());

            pDAO.update(login.getProfessorsiape());
            login.getProfessorsiape().setPerfilidperfil(lTemp.getProfessorsiape().getPerfilidperfil());
            SistemaSession instance = SistemaSession.getInstance();
            instance.getSession().setAttribute("login", login);
            pDAO.closeSession();
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        }
    }

    /**
     * altera os dados de um professor
     *
     * @param p
     * @param ld
     * @param assinatura
     * @throws java.lang.Exception
     */
    public void alterarProfessor(Professor p, List<Professordisponibilidade> ld,String assinatura) throws Exception {
        Boolean realizado;
        Integer siape;

        if (this.isUsuarioProfessor() == true) {
            Professor pTemp = sessao.getProfessorAlterar();
            p.setAtivo(true);
            p.setPerfilidperfil(pTemp.getPerfilidperfil());
        } else {
            if (p.getAtivo() == null) {
                p.setAtivo(false);
            }
        }

        siape = this.getProfessorAlterar().getSiape();
        p.setSiape(siape);
        ProfessorDAO pDAO = new ProfessorDAO();
        realizado = pDAO.update(p);
        pDAO.closeSession();
        //-------------------------------------------------------
        /*atualizar dados do usuário atual*/
        if (this.isUsuarioProfessor() == true) {
            SistemaSession instance = SistemaSession.getInstance();
            Login loginAtual = instance.getLoginAtual();
            loginAtual.setProfessorsiape(p);
            instance.getSession().setAttribute("login", loginAtual);
        }
        //-------------------------------------------------------
        //assinatura
        AssinaturaDAO aDAO = new AssinaturaDAO();
        Assinatura a = aDAO.getAssinaturaBySiapeProfessor(p.getSiape());
        a.setAssinatura(assinatura);
        aDAO.update(a);
        aDAO.closeSession();
        //-------------------------------------------------------
        /*alterar disponibilidade*/
        int contador = 0;
        for (int i = 0; i < this.getListaDisponibilidade().size(); i++) {
            Professordisponibilidade pd1 = this.getListaDisponibilidade().get(i);
            Professordisponibilidade pd2 = ld.get(contador);

            if (pd1.getIdprofessordisponiblidade() == null) {
                pd1.setProfessorsiape(p);
                pd1.setManha(pd2.getManha());
                pd1.setTarde(pd2.getTarde());
                pd1.setNoite(pd2.getNoite());
                ProfessorDisponiblidadeDAO pdDAO = new ProfessorDisponiblidadeDAO();
                pdDAO.insert(pd1);
                pdDAO.closeSession();
            } else {
                while (!pd1.getDiasemana().equals(pd2.getDiasemana()) && contador < ld.size()) {
                    contador++;
                    pd2 = ld.get(contador);
                }
                if (pd1.getDiasemana().equals(pd2.getDiasemana())) {
                    pd1.setManha(pd2.getManha());
                    pd1.setTarde(pd2.getTarde());
                    pd1.setNoite(pd2.getNoite());
                    ProfessorDisponiblidadeDAO pdDAO = new ProfessorDisponiblidadeDAO();
                    pdDAO.update(pd1);
                    pdDAO.closeSession();
                    contador = 0;
                }
            }

        }

        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar dados!", "Informe o admistrador."));
        }
    }

    /*
     * altera os dados de um professor
     *
     * @param p
     * @return pagina
     * @throws java.lang.Exception
     */
    public String desativarProfessor(Professor p) throws Exception {
        Boolean realizado;
        ProfessorDAO pDAO = new ProfessorDAO();
        p.setAtivo(false);
        realizado = pDAO.update(p);
        pDAO.closeSession();
        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlVisualzarProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Acesso desativado com sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlVisualzarProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro desativar acesso!", "Informe o admistrador."));
        }

        return "visualizarProfessor.xhtml?facesRedirect=true";
    }

    /**
     * Função para verificar se o perfil é professor porque não é possivel
     * alterar o perfil e se está ativo ficando não visivel
     *
     * @return boolean
     */
    public boolean isUsuarioProfessor() {
        SistemaSession instance = SistemaSession.getInstance();
        Login loginAtual = instance.getLoginAtual();
        if (loginAtual != null && loginAtual.getProfessorsiape() != null) {
            if (loginAtual.getProfessorsiape().getPerfilidperfil().getNomeperfil().equals("Professor")) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Função executada pelo professor em seu perfil para alterar seus dados
     *
     * @return url
     */
    public String direcionaAlterarMeusDadosProfessor() {
        SistemaSession instance = SistemaSession.getInstance();
        Login loginAtual = instance.getLoginAtual();
        if (loginAtual != null) {
            Professor p = loginAtual.getProfessorsiape();
            this.setProfessor(p);
            instance.getSession().setAttribute("alterarProfessor", p);
        }
        return "alteraProfessor.xhtml?facesRedirect=true";
    }

    /**
     * direciona para pagina de alteração de dados de um professor
     *
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarProfessor(Professor p) throws Exception {
        sessao.getSession().setAttribute("alterarProfessor", p);
        return "alteraProfessor.xhtml?facesRedirect=true";
    }

    /**
     * solicitar acesso ao sistema
     *
     * @throws java.lang.Exception
     */
    public void solicitarAcesso() throws Exception {
        CommonsEmail email = new CommonsEmail();
        ProfessorDAO pDAO = new ProfessorDAO();
        if (pDAO.getProfessorBySiape(login.getProfessorsiape().getSiape()) == null) {
            String nome = login.getProfessorsiape().getNome();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
            login.getProfessorsiape().setNome(nome);

            StringBuilder msgEmail = new StringBuilder();
            msgEmail.append("<html><body>");
            msgEmail.append("<h1>SisGES - Sistema de Gestão de Estágios Supervisionados</h1>");
            msgEmail.append("<br/>");
            msgEmail.append("<h2>Dados da solicitação de acesso ao SisGES</h2>");
            msgEmail.append("<br/>");
            msgEmail.append("Siape: ").append(login.getProfessorsiape().getSiape().toString());
            msgEmail.append("<br/>");
            msgEmail.append("Nome: ").append(login.getProfessorsiape().getNome());
            msgEmail.append("<br/>");
            msgEmail.append("Email: ").append(login.getProfessorsiape().getEmail());
            msgEmail.append("<br/>");
            msgEmail.append("Telefone: ").append(login.getProfessorsiape().getTelefone());
            msgEmail.append("<br/>");
            msgEmail.append("</body></html>");
            boolean enviado = email.enviarEmail("Solicitação de acesso ao sistema SisGES", msgEmail.toString(), email.getEmailConfig().getEmail());

            if (enviado == true) {
                FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + email.getEmailConfig().getEmail(), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
            }

            //----------------------------------------------------------------------------
            FacesContext.getCurrentInstance().addMessage("growlAcesso",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação realizada com sucesso!\nAguarde o email com dados de acesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlAcesso",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Professor já cadastrado.", ""));
        }
        pDAO.closeSession();
    }

    /**
     * retorna o professor que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Professor getProfessorAlterar() throws Exception {
        sessao.verificaAcesso();
        //-------------------------------------------------------
        this.setProfessor(sessao.getProfessorAlterar());
        //-------------------------------------------------------
        //assinatura
        AssinaturaDAO aDAO = new AssinaturaDAO();
        Assinatura a = aDAO.getAssinaturaBySiapeProfessor(this.getProfessor().getSiape());
        if (a != null) {
            this.setAssinatura(a.getAssinatura());
        } else {
            a = new Assinatura();
            a.setAssinatura("");
            a.setProfessorsiape(this.getProfessor());
            aDAO.insert(a);
        }
        aDAO.closeSession();
        //-------------------------------------------------------
        //verifica disponibilidade
        ProfessorDisponiblidadeDAO pdDAO = new ProfessorDisponiblidadeDAO();
        List<Professordisponibilidade> listaDisp = pdDAO.getDisponibilidadeBySiape(this.professor.getSiape());
        if (listaDisp.size() > 0) {
            List<Professordisponibilidade> listaDispPadrao = this.initDisponibilidade();
            this.getListaProfessores().clear();
            for (int i = 0; i < listaDisp.size(); i++) {
                Professordisponibilidade pd = listaDisp.get(i);

                if (pd.getDiasemana().equals("Segunda")) {
                    listaDispPadrao.set(0, pd);
                }
                if (pd.getDiasemana().equals("Terça")) {
                    listaDispPadrao.set(1, pd);
                }

                if (pd.getDiasemana().equals("Quarta")) {
                    listaDispPadrao.set(2, pd);
                }

                if (pd.getDiasemana().equals("Quinta")) {
                    listaDispPadrao.set(3, pd);
                }

                if (pd.getDiasemana().equals("Sexta")) {
                    listaDispPadrao.set(4, pd);
                }
            }

            this.setListaDisponibilidade(listaDispPadrao);
        } else {
            this.initDisponibilidade();
        }
        return this.getProfessor();
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
        return login;
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

    public String getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(String enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

}
