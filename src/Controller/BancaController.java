/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosBanca;
import Controller.Data.DadosRelatorio;
import Dao.AssinaturaDAO;
import Dao.BancaDAO;
import Dao.BancaFilaDAO;
import Dao.BancaStatusDAO;
import Dao.ConvidadoDAO;
import Dao.EmailConfigDAO;
import Dao.TrabalhoDAO;
import Model.Banca;
import Model.Bancafila;
import Model.Bancastatus;
import Model.Campus;
import Model.Convidado;
import Model.Login;
import Model.Professor;
import Model.Trabalho;

import Email.CommonsEmail;
import Model.Aluno;
import Model.Assinatura;
import Util.CalendarFormat;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "BancaBean")
@SessionScoped
public class BancaController extends DadosBanca {

    private Banca banca = new Banca();
    private final SistemaSession sessao = SistemaSession.getInstance();
    private String enviarEmail = "";
    private Bancafila fila = new Bancafila();
    private DualListModel<Bancafila> filaBanca;
    private Convidado selectedConvidado;
    private List<Bancafila> filaTarget = new ArrayList<>();

    public BancaController() {

    }

    @PostConstruct
    public void init() {
        /*usado no cadastro da banca*/
        this.inicializaSelecaoDeConvidados();
        this.inicializaStatusBanca();
        this.inicializaVisualizaBancaPresente();
        //----------------------
        EmailConfigDAO ecDAO = new EmailConfigDAO();
        try {
            this.setEmailConfig(ecDAO.getEmailConfig());
            ecDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Função para inicializar seleção de convidados para uma banca
     *
     */
    public void inicializaSelecaoDeConvidados() {
        List<Bancafila> filaSource;
        try {
            BancaFilaDAO bfDAO = new BancaFilaDAO();
            filaSource = bfDAO.getFila();
            filaBanca = new DualListModel<>(filaSource, filaTarget);
            bfDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Função para inicializar status da banca
     *
     */
    public void inicializaStatusBanca() {
        BancaStatusDAO bsDAO = new BancaStatusDAO();
        Bancastatus bstatus;
        try {
            bstatus = bsDAO.getStatusByTipo("Em andamento");
            this.getBanca().setStatusidstatus(bstatus);
            bsDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(Banca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Função para inicializar a visualização de bancas confirmadas do professor
     * logado visualizarOrientandoss.xhtml
     *
     */
    public void inicializaVisualizaBancaPresente() {
        ConvidadoDAO cDAO = new ConvidadoDAO();
        Login loginAtual = sessao.getLoginAtual();
        List<Convidado> presencas;
        try {
            presencas = cDAO.getConvidadoFromBancaBySiapeConfirmado(loginAtual.getProfessorsiape().getSiape());
            this.setListaBancaPresente(presencas);
            cDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * função que direciona para pagina de visualizar bancas que o professor
     * participou e ira participar
     *
     * @return
     */
    public String direcionaVisualizarBancaPresente() {
        return "visualizarBancaPresente.xhtml?facesRedirect=true";
    }

    /**
     * saber se o trabalho tem banca
     *
     * @param t
     * @return
     */
    public boolean existeBanca(Trabalho t) {
        boolean encontrado = false;
        try {
            BancaDAO bDAO = new BancaDAO();
            encontrado = bDAO.getExisteBancaByIdTrabalho(t.getIdtrabalho());
            bDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encontrado;
    }

    /**
     * valida alteração
     *
     * @return boolean
     */
    public boolean validacaoAlteracao() {
        boolean valido = true;

        if (this.getBanca().getLocalbanca().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o local da banca.", ""));
            valido = false;
        }

        if (this.getBanca().getHorario() == null) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o horário da banca.", ""));
            valido = false;
        }

        if (this.getBanca().getDatabanca() == null) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data da banca.", ""));
            valido = false;
        }
        if (this.getFilaBanca().getTarget().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione os professores que participarão da banca.", ""));
            valido = false;
        }

        if (this.getFilaBanca().getTarget().size() < 2) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione no mínimo 2 professores.", ""));
            valido = false;
        }

        return valido;
    }

    /**
     * valida cadastro
     *
     * @return boolean
     */
    public boolean validacaoCadastro() {
        boolean valido = true;

        if (this.getBanca().getTrabalhoidtrabalho() == null) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o trabalho.", ""));
            valido = false;
        }

        if (this.getBanca().getLocalbanca().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o local da banca.", ""));
            valido = false;
        }

        if (this.getBanca().getHorario() == null) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o horário da banca.", ""));
            valido = false;
        }

        if (this.getBanca().getDatabanca() == null) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data da banca.", ""));
            valido = false;
        }
        if (this.getFilaBanca().getTarget().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione os professores que participarão da banca.", ""));
            valido = false;
        }

        if (this.getFilaBanca().getTarget().size() < 2) {
            FacesContext.getCurrentInstance().addMessage("growlBanca",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione no mínimo 2 professores.", ""));
            valido = false;
        }

        return valido;
    }

    /**
     * confirmar selecao dos professores convidados
     *
     * @param opcao
     */
    public void confirmarSelecao(int opcao) {

        if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
            TrabalhoDAO tDAO = new TrabalhoDAO();
            Trabalho t = null;
            try {
                t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getBanca().setTrabalhoidtrabalho(t);
            sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
            tDAO.closeSession();
        }

        //cadastro de convidados
        if (opcao == 1) {
            if (this.validacaoCadastro() == true) {
                this.getListaConvidados().clear();
                for (int i = 0; i < this.getFilaBanca().getTarget().size(); i++) {
                    try {
                        Bancafila convidadoBanca = this.getFilaBanca().getTarget().get(i);
                        Convidado convidado = new Convidado();
                        convidado.setBancaidbanca(this.getBanca());
                        convidado.setDataconvite(CalendarFormat.getDataSO());
                        convidado.setConfirmado(false);
                        Professor prof = new Professor(convidadoBanca.getSiape());
                        prof.setNome(convidadoBanca.getNome());
                        prof.setEmail(convidadoBanca.getEmail());
                        prof.setTelefone(convidadoBanca.getTelefone());
                        convidado.setProfessorsiape(prof);
                        this.getListaConvidados().add(convidado);
                    } catch (Exception ex) {
                        Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.getListaConvidados().size() >= 2) {
                    sessao.getSession().setAttribute("listaConvidado", this.getListaConvidados());
                }

                this.isAtivaGerarAta();
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('dlgConfirmacaoConvidado').show()");
            }
            //alteração de convidados
        } else if (opcao == 2) {
            if (this.validacaoAlteracao() == true) {
                this.getListaConvidados().clear();
                ConvidadoDAO cDAO = new ConvidadoDAO();
                for (int i = 0; i < this.getFilaBanca().getTarget().size(); i++) {
                    try {
                        Bancafila convidadoBanca = this.getFilaBanca().getTarget().get(i);
                        Convidado convidadoPersist = cDAO.getExisteConvidadoFromBancaBySiape(this.getBanca().getIdbanca(), convidadoBanca.getSiape());
                        if (convidadoPersist != null) {
                            this.getListaConvidados().add(convidadoPersist);
                        } else {
                            Convidado convidado = new Convidado();
                            convidado.setBancaidbanca(this.getBanca());
                            convidado.setDataconvite(CalendarFormat.getDataSO());
                            convidado.setConfirmado(false);
                            Professor prof = new Professor(convidadoBanca.getSiape());
                            prof.setNome(convidadoBanca.getNome());
                            prof.setEmail(convidadoBanca.getEmail());
                            prof.setTelefone(convidadoBanca.getTelefone());
                            convidado.setProfessorsiape(prof);
                            this.getListaConvidados().add(convidado);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (this.getListaConvidados().size() >= 2) {
                    sessao.getSession().setAttribute("listaConvidado", this.getListaConvidados());
                }

                this.isAtivaGerarAta();
                cDAO.closeSession();
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('dlgConfirmacaoConvidado').show()");
            }
        }
    }

    /**
     * função para gerar ata de defesa
     *
     * @param coordenador
     * @param campus
     */
    public void gerarAtaDeDefesa(Professor coordenador, Campus campus) {

        if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
            TrabalhoDAO tDAO = new TrabalhoDAO();
            Trabalho t = null;
            try {
                t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getBanca().setTrabalhoidtrabalho(t);
            sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
            tDAO.closeSession();
        } else {
            Banca b = (Banca) sessao.getSession().getAttribute("BancaSelecao");
            this.setBanca(b);
        }

        //se banca finalizada pegar confirmados no banco
        if (this.getBanca().getStatusidstatus().getTipostatus().equals("Finalizada")) {
            ConvidadoDAO cDAO = new ConvidadoDAO();
            try {
                //clear importante para validação ok
                this.getListaConvidados().clear();
                this.getListaConvidados().addAll(cDAO.getAllConvidadosConfirmadosFromBanca(this.getBanca().getIdbanca()));
                cDAO.closeSession();
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (this.validacaoMinConvidados() == true) {

            DadosRelatorio dr = new DadosRelatorio();
            try {
                dr.gerarRelatorioAtaDeDefesa(this.getBanca(), this.getListaConvidados(), coordenador, campus);
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesContext.getCurrentInstance().addMessage("growlConvidados",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "O Nº Mínimo de convidados confirmados é 2.", ""));
        }
    }

    /**
     * função que define se o botão exibir ata será mostrado
     *
     * @return boolean
     */
    public boolean isBancaFinalizada() {
        if (this.getBanca().getStatusidstatus().getTipostatus().equals("Finalizada")) {
            return true;
        }

        return false;
    }

    /**
     * função que define se o botão exibir ata será mostrado
     *
     * @return boolean
     */
    public boolean isAtivaGerarAta() {
        if (this.validacaoMinConvidados() == true
                || this.getBanca().getStatusidstatus().getTipostatus().equals("Finalizada")) {
            return false;
        }
        return true;
    }

    /**
     * validação do numero de candidatos que no minimo deve ser 2
     *
     * @return boolean
     */
    public boolean validacaoMinConvidados() {
        boolean valido = false;
        int contaCandidatos = 0;
        for (int i = 0; i < this.getListaConvidados().size(); i++) {
            Convidado convidado = this.getListaConvidados().get(i);
            if (convidado.getConfirmado() == true) {
                contaCandidatos++;
            }
        }
        if (contaCandidatos == 2) {
            valido = true;
        }
        return valido;
    }

    /**
     * validação do numero de candidatos que no maximo deve ser 2
     *
     * @return boolean
     */
    public boolean validacaoMaxConvidados() {
        boolean valido = false;
        int contaCandidatos = 0;
        for (int i = 0; i < this.getListaConvidados().size(); i++) {
            Convidado convidado = this.getListaConvidados().get(i);
            if (convidado.getConfirmado() == true) {
                contaCandidatos++;
            }
        }
        if (contaCandidatos <= 2) {
            valido = true;
        }
        return valido;
    }

    /**
     * função para fechar a banca com no minimo 2 convidados
     *
     */
    public void bancaFinalizar() {
        boolean realizado;

        if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
            TrabalhoDAO tDAO = new TrabalhoDAO();
            Trabalho t = null;
            try {
                t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getBanca().setTrabalhoidtrabalho(t);
            sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
            tDAO.closeSession();
        } else {
            Banca b = (Banca) sessao.getSession().getAttribute("BancaSelecao");
            this.setBanca(b);
        }

        if (this.validacaoMinConvidados() == true
                && this.getBanca().getStatusidstatus().getTipostatus().equals("Em andamento")) {
            this.salvarAlteracaoConvidados();
            BancaStatusDAO bsDAO = new BancaStatusDAO();
            Bancastatus bstatus = null;

            try {
                bstatus = bsDAO.getStatusByTipo("Finalizada");
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            BancaDAO bDAO = new BancaDAO();
            this.getBanca().setDatafinalizacao(CalendarFormat.getDataSO());
            this.getBanca().setStatusidstatus(bstatus);
            realizado = bDAO.update(this.getBanca());
            //apos atualização insere na sessao
            sessao.getSession().setAttribute("BancaSelecao", this.getBanca());

            bDAO.closeSession();

            if (realizado == true) {
                FacesContext.getCurrentInstance().addMessage("growlConvidados",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Banca finalizada com sucesso!", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlConvidados",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na finalização! Informe o administrador.", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("growlConvidados",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "O Nº Mínimo de convidados é 2.", ""));
        }
    }

    /**
     * função para salvar seleção dos professores convidados
     *
     */
    public void salvarAlteracaoConvidados() {

        if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
            TrabalhoDAO tDAO = new TrabalhoDAO();
            Trabalho t = null;
            try {
                t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getBanca().setTrabalhoidtrabalho(t);
            sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
            tDAO.closeSession();
        } else {
            Banca b = (Banca) sessao.getSession().getAttribute("BancaSelecao");
            this.setBanca(b);
        }

        boolean realizado = false;
        List<Convidado> convidadosBase = null;
        if (this.validacaoMaxConvidados() == true) {
            ConvidadoDAO cDAO = new ConvidadoDAO();
            try {
                convidadosBase = cDAO.getAllConvidadosFromBanca(this.getBanca().getIdbanca());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < this.getListaConvidados().size(); i++) {
                Convidado convidado = this.getListaConvidados().get(i);
                try {
                    Convidado convidadoPersist = cDAO.getExisteConvidadoFromBancaBySiape(this.getBanca().getIdbanca(), convidado.getProfessorsiape().getSiape());
                    if (convidadoPersist != null) {
                        //----------------------------------------------------------
                        //verificar se um convidado saiu da lista de convidados
                        boolean encontrado = false;
                        for (Convidado a : convidadosBase) {
                            if (Objects.equals(a.getIdconvidado(), convidado.getIdconvidado())) {
                                encontrado = true;
                            }
                        }
                        //se não encontrado deleta porque saiu da lista de convidados
                        if (encontrado == false) {
                            realizado = cDAO.delete(convidado);
                        } else {
                            //se encontrado atualiza
                            if (convidado.getConfirmado() == true) {
                                convidadoPersist.setConfirmado(convidado.getConfirmado());
                                convidadoPersist.setDataconfirmacao(CalendarFormat.getDataSO());
                            } else {
                                convidadoPersist.setConfirmado(false);
                                convidadoPersist.setDataconfirmacao(null);
                            }
                            realizado = cDAO.update(convidadoPersist);
                        }
                    } else {
                        if (convidado.getConfirmado() == true) {
                            convidado.setDataconfirmacao(CalendarFormat.getDataSO());
                        }
                        realizado = cDAO.insert(convidado);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (this.getListaConvidados().size() >= 2) {
                sessao.getSession().setAttribute("listaConvidado", this.getListaConvidados());
            }

            if (realizado == true) {
                FacesContext.getCurrentInstance().addMessage("growlConvidados",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Convidados salvos com sucesso!", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlConvidados",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na alteração! Informe o administrador.", ""));
            }
            cDAO.closeSession();
        } else {
            FacesContext.getCurrentInstance().addMessage("growlConvidados",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "O Nº Máximo de convidados é 2.", ""));
        }
    }

    /**
     * cadastra banca
     *
     * @throws java.lang.Exception
     */
    public void salvarConvidados() throws Exception {
        boolean realizado = false;

        if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
            TrabalhoDAO tDAO = new TrabalhoDAO();
            Trabalho t = null;
            try {
                t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
            } catch (Exception ex) {
                Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getBanca().setTrabalhoidtrabalho(t);
            tDAO.closeSession();
        }

        if (this.validacaoCadastro() == true) {
            ConvidadoDAO cDAO = new ConvidadoDAO();
            for (int i = 0; i < this.getListaConvidados().size(); i++) {
                Convidado convidado = this.getListaConvidados().get(i);
                //se existe banca e convidado apenas atualiza
                if (this.getBanca().getIdbanca() != null) {
                    Convidado convidadoPersist = cDAO.getExisteConvidadoFromBancaBySiape(this.getBanca().getIdbanca(), convidado.getProfessorsiape().getSiape());
                    if (convidadoPersist != null) {
                        if (convidado.getConfirmado() == true) {
                            convidadoPersist.setConfirmado(convidado.getConfirmado());
                            convidadoPersist.setDataconfirmacao(CalendarFormat.getDataSO());
                        } else {
                            convidadoPersist.setConfirmado(false);
                            convidadoPersist.setDataconfirmacao(null);
                        }
                        realizado = cDAO.update(convidadoPersist);
                    } else {
                        convidado.setBancaidbanca(this.getBanca());
                        convidado.setDataconvite(CalendarFormat.getDataSO());
                        convidado.setConfirmado(false);
                        Professor prof = new Professor(convidado.getProfessorsiape().getSiape());
                        prof.setNome(convidado.getProfessorsiape().getNome());
                        prof.setEmail(convidado.getProfessorsiape().getEmail());
                        prof.setTelefone(convidado.getProfessorsiape().getTelefone());
                        convidado.setProfessorsiape(prof);
                        realizado = cDAO.insert(convidado);
                    }
                } else {
                    //se não existe banca e convidado insere
                    if (i == 0) {
                        BancaDAO bDAO = new BancaDAO();
                        realizado = bDAO.insert(this.getBanca());
                        sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
                        bDAO.closeSession();
                    }
                    if (realizado == true) {
                        convidado.setBancaidbanca(this.getBanca());
                        convidado.setDataconvite(CalendarFormat.getDataSO());
                        convidado.setConfirmado(false);
                        Professor prof = new Professor(convidado.getProfessorsiape().getSiape());
                        prof.setNome(convidado.getProfessorsiape().getNome());
                        prof.setEmail(convidado.getProfessorsiape().getEmail());
                        prof.setTelefone(convidado.getProfessorsiape().getTelefone());
                        convidado.setProfessorsiape(prof);

                        realizado = cDAO.insert(convidado);
                    } else {
                        FacesContext.getCurrentInstance().addMessage("growlBanca",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no cadastro!", ""));
                    }
                }
            }

            sessao.getSession().setAttribute("listaConvidado", this.getListaConvidados());
            cDAO.closeSession();
            if (realizado == true) {
                FacesContext.getCurrentInstance().addMessage("growlBanca",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlBanca",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no cadastro!", ""));
            }
        }
    }

    /**
     * função para enviar convite por email para professor convidado
     *
     * @param c
     */
    public void enviarConvitePorEmail(Convidado c) {
        boolean realizado;
        ConvidadoDAO cDAO = new ConvidadoDAO();
        c.setEnviadoemail(true);
        realizado = cDAO.update(c);
        cDAO.closeSession();

        this.emailConvidaProfessor(c);
    }

    /**
     * retorna a banca que será alterado
     *
     * @return
     */
    public Banca carregarBanca() {
        sessao.verificaAcesso();
        Trabalho trab;
        try {
            trab = sessao.getTrabalho();

            //pegar a banca pelo id do trabalho
            Integer idTrabalho = trab.getIdtrabalho();
            BancaDAO bDAO = new BancaDAO();
            Banca ibanca = bDAO.getBancaByIdTrabalho(idTrabalho);
            this.setBanca(ibanca);
            this.setTituloBanca("Matrícula: " + this.getBanca().getTrabalhoidtrabalho().getAlunomatricula().getMatricula() + "  -  Nome: " + this.getBanca().getTrabalhoidtrabalho().getAlunomatricula().getNome() + "");
            bDAO.closeSession();

            //carregar convidados
            List<Bancafila> filaSource;

            BancaFilaDAO bfDAO = new BancaFilaDAO();
            filaSource = bfDAO.getFila();
            bfDAO.closeSession();
            ConvidadoDAO cDAO = new ConvidadoDAO();
            List<Convidado> listConvidados = cDAO.getAllConvidadosFromBanca(ibanca.getIdbanca());

            for (int i = 0; i < filaSource.size(); i++) {
                //retirar o orientador das opções de convidados
                if (filaSource.get(i).getSiape().equals(trab.getProfessorsiape().getSiape())) {
                    filaSource.remove(i);
                }
            }

            for (Convidado listConvidado : listConvidados) {
                for (int i = 0; i < filaSource.size(); i++) {
                    //verificar se alguem ja foi convidado
                    if (filaSource.get(i).getSiape().equals(listConvidado.getProfessorsiape().getSiape())) {
                        this.filaTarget.add(filaSource.get(i));
                        filaSource.remove(i);
                        i--;
                    }
                }
            }
            cDAO.closeSession();
            filaBanca = new DualListModel<>(filaSource, this.filaTarget);
        } catch (Exception ex) {
            Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.getBanca();
    }

    /**
     * enviar email para banca
     *
     * @param opcao
     */
    @SuppressWarnings("unchecked")
    public void confirmacaoBancaEmail(int opcao) {

        if (this.getBanca().getIdbanca() != null) {

            if (this.getBanca().getTrabalhoidtrabalho().getIdtrabalho() != null) {
                TrabalhoDAO tDAO = new TrabalhoDAO();
                Trabalho t = null;
                try {
                    t = tDAO.getTrabalhoById(this.getBanca().getTrabalhoidtrabalho().getIdtrabalho());
                } catch (Exception ex) {
                    Logger.getLogger(BancaController.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.getBanca().setTrabalhoidtrabalho(t);
                sessao.getSession().setAttribute("BancaSelecao", this.getBanca());
                tDAO.closeSession();
            }

            //----------------------
            EmailConfigDAO ecDAO = new EmailConfigDAO();
            try {
                this.setEmailConfig(ecDAO.getEmailConfig());
                ecDAO.closeSession();
            } catch (Exception ex) {
                Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (this.getListaConvidados().isEmpty() && opcao == 1) {
                this.setListaConvidados((List<Convidado>) sessao.getSession().getAttribute("listaConvidado"));
            }

            StringBuilder destino = new StringBuilder();
            if (opcao == 1 && this.getListaConvidados().size() >= 2) {
                //opcao para tela cadastro
                destino.append(this.getBanca().getTrabalhoidtrabalho().getProfessorsiape().getEmail()).append(";");
                for (Convidado c : this.getListaConvidados()) {
                    destino.append(c.getProfessorsiape().getEmail()).append(";");
                }
            } else if (opcao == 2) {
                //opcao 2 para tela alteracao
                destino.append(this.getBanca().getTrabalhoidtrabalho().getProfessorsiape().getEmail()).append(";");
                for (Bancafila bf : this.getFilaTarget()) {
                    destino.append(bf.getEmail()).append(";");
                }
            }

            this.setDestinatarios(destino.toString());
            this.setTitulo("FACOM/UFU - Coordenacao de Estagio Supervisionado - DEFESA");

            StringBuilder msgEmail = new StringBuilder();
            msgEmail.append("<html><body>");
            msgEmail.append("Prezados,");
            msgEmail.append("<br/><br/>");
            msgEmail.append("Venho comunicar que a defesa de estagio supervisionado do(a) aluno(a) ").append(this.getBanca().getTrabalhoidtrabalho().getAlunomatricula().getNome()).append(" ficou agendada para :");
            msgEmail.append("<br/><br/>");
            msgEmail.append("Data: ").append(CalendarFormat.getDataBRtoDate(this.getBanca().getDatabanca()));
            msgEmail.append("<br/>");
            msgEmail.append("Horário: ").append(CalendarFormat.parseDateToTimeString(this.getBanca().getHorario()));
            msgEmail.append("<br/>");
            msgEmail.append("Local: ").append(this.getBanca().getLocalbanca());
            msgEmail.append("<br/><br/>");
            msgEmail.append("A banca sera composta por:");
            msgEmail.append("<br/>");
            msgEmail.append(this.getBanca().getTrabalhoidtrabalho().getProfessorsiape().getNome()).append(" (orientador(a) e presidente da banca)");
            msgEmail.append("<br/>");

            if (opcao == 1 && this.getListaConvidados().size() >= 2) {
                //opcao para tela cadastro
                for (Convidado c : this.getListaConvidados()) {
                    msgEmail.append(c.getProfessorsiape().getNome()).append(" (convidado(a))");
                    msgEmail.append("<br/>");
                }
            } else if (opcao == 2) {
                //opcao 2 para tela alteracao
                for (Bancafila lb : this.getFilaTarget()) {
                    msgEmail.append(lb.getNome()).append(" (convidado(a))");
                    msgEmail.append("<br/>");
                }
            }

            msgEmail.append("<br/>");
            msgEmail.append("Titulo do trabalho: ").append(this.getBanca().getTrabalhoidtrabalho().getTitulo());
            msgEmail.append("<br/><br/>");
            msgEmail.append("Os relatorios estarão disponíveis em seus escaninhos.");
            msgEmail.append("<br/><br/>");
            msgEmail.append("Tenham uma excelente defesa!");
            //-------------------------------------------------------------------
            //assinatura
            Login loginAtual = sessao.getLoginAtual();
            AssinaturaDAO aDAO = new AssinaturaDAO();
            Assinatura a;
            try {
                a = aDAO.getAssinaturaBySiapeProfessor(loginAtual.getProfessorsiape().getSiape());
                msgEmail.append("<br/><br/>");
                msgEmail.append(a.getAssinatura());
            } catch (Exception ex) {
                Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            aDAO.closeSession();
            //-------------------------------------------------------------------

            msgEmail.append("<br/>");
            msgEmail.append("</body></html>");
            this.setMensagem(msgEmail.toString());
        }

    }

    /**
     * função para enviar convite para professor participar da banca
     *
     * @param convidado
     */
    public void emailConvidaProfessor(Convidado convidado) {
        Professor p = convidado.getProfessorsiape();
        Aluno a = convidado.getBancaidbanca().getTrabalhoidtrabalho().getAlunomatricula();
        Trabalho t = convidado.getBancaidbanca().getTrabalhoidtrabalho();
        Banca b = convidado.getBancaidbanca();

        this.setDestinatarios(p.getEmail());
        this.setTitulo("FACOM/UFU - Coordenacao de Estagio Supervisionado");

        StringBuilder msgEmail = new StringBuilder();
        msgEmail.append("<html><body>");
        msgEmail.append("Bom dia Prof. ").append(p.getNome()).append(", <br/>");
        msgEmail.append("<br/>Gostariamos de saber se podemos contar com a sua participação na banca de estágio supervisionado do aluno ")
                .append(a.getNome()).append("? <br/>");
        msgEmail.append("orientando do prof. ").append(t.getProfessorsiape().getNome()).append("<br/><br/>");

        msgEmail.append("Seria possível na data ").append(CalendarFormat.getDataBRtoDate(b.getDatabanca())).append(" às ").append(CalendarFormat.parseDateToTimeString(b.getHorario())).append(" horas.<br/>" + "<br/>Local: ").append(b.getLocalbanca()).append("<br/>");

        msgEmail.append("<br/>Título do trabalho: ").append(t.getTitulo());
        msgEmail.append("<br/><br/>");
        msgEmail.append("Antecipando agradecimentos,");

        //-------------------------------------------------------------------
        //assinatura
        Login loginAtual = sessao.getLoginAtual();
        AssinaturaDAO aDAO = new AssinaturaDAO();
        Assinatura ass;
        try {
            ass = aDAO.getAssinaturaBySiapeProfessor(loginAtual.getProfessorsiape().getSiape());
            msgEmail.append("<br/><br/>");
            msgEmail.append(ass.getAssinatura());
        } catch (Exception ex) {
            Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        aDAO.closeSession();
        //-------------------------------------------------------------------
        msgEmail.append("<br/>");
        msgEmail.append("</body></html>");
        this.setMensagem(msgEmail.toString());

    }

    /**
     * enviar email
     *
     */
    public void enviarEmail() {
        CommonsEmail email;
        try {
            email = new CommonsEmail();
            boolean enviado = email.enviarEmail(this.getTitulo(), this.getMensagem(), this.getDestinatarios());
            if (enviado == true) {
                FacesContext.getCurrentInstance().addMessage("growlNovoEmail",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("growlNovoEmail",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
            }
        } catch (EmailException | MalformedURLException ex) {
            FacesContext.getCurrentInstance().addMessage("growlNovoEmail",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Bancafila getFila() {
        return fila;
    }

    public void setFila(Bancafila fila) {
        this.fila = fila;
    }

    public DualListModel<Bancafila> getFilaBanca() {
        return filaBanca;
    }

    public void setFilaBanca(DualListModel<Bancafila> filaBanca) {
        this.filaBanca = filaBanca;
    }

    public Banca getBanca() {
        return banca;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    public String getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(String enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public Convidado getSelectedConvidado() {
        return selectedConvidado;
    }

    public void setSelectedConvidado(Convidado selectedConvidado) {
        this.selectedConvidado = selectedConvidado;
    }

    public List<Bancafila> getFilaTarget() {
        return filaTarget;
    }

    public void setFilaTarget(List<Bancafila> filaTarget) {
        this.filaTarget = filaTarget;
    }

}
