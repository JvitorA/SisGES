package Controller;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;

import Controller.Data.DadosTrabalho;
import Dao.AssinaturaDAO;
import Dao.BancaDAO;
import Dao.CursoDAO;
import Dao.DefesaDAO;
import Dao.EmailConfigDAO;
import Dao.ModalidadeDAO;
import Dao.ProfessorDAO;
import Dao.TrabalhoDAO;
import Dao.TrabalhoStatusDAO;
import Email.CommonsEmail;
import Model.Assinatura;
import Model.Banca;
import Model.Curso;
import Model.Defesa;
import Model.Login;
import Model.Modalidade;
import Model.Professor;
import Model.Trabalho;
import Model.Trabalhostatus;
import Util.CalendarFormat;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "TrabalhoBean")
@SessionScoped
public class TrabalhoController extends DadosTrabalho {

    private String enviarEmail = "";
    private final SistemaSession sessao = SistemaSession.getInstance();
    private Trabalho trabalho = new Trabalho();
    private boolean mostrarDataFinalizacao;

    public TrabalhoController() {
        this.mostrarDataFinalizacao = false;
    }

    /**
     * função para retornar todos os trabalhos cadastrados para a view
     * visualizarTrabalho.xhtml
     *
     */
    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        try {
            //------------------------------------------
            this.atualizaVisualizaTrabalhos();
            //------------------------------------------
            this.atualizaVisualizaOrientandos();
            //------------------------------------------
            this.getTrabalho().setDatamatricula(this.getDataAtual());
            //------------------------------------------
            this.getListagemCursos();
            //------------------------------------------
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * função para atualiza tabela de trabalhos da view visualizarTrabalho.xhtml
     *
     */
    public void atualizaVisualizaTrabalhos() {
        this.getListaTrabalhos().clear();
        TrabalhoDAO tDAO = new TrabalhoDAO();
        List<Trabalho> allTrabalho;
        try {
            allTrabalho = tDAO.getAllTrabalhos();
            this.setListaTrabalhos(allTrabalho);
            tDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Função para atualiza tabela de orientandos da view
     * visualizarOrientandoss.xhtml
     *
     */
    public void atualizaVisualizaOrientandos() {
        TrabalhoDAO tDAO = new TrabalhoDAO();
        List<Trabalho> allTrabalho;
        try {
            Login loginAtual = sessao.getLoginAtual();
            allTrabalho = tDAO.getAllTrabalhosByOrientador(loginAtual.getProfessorsiape().getSiape());
            this.setListaOrientandos(allTrabalho);
            tDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Função que verifica se o trabalho não está com status pré-aprovado para
     * mostrar o enviar email
     *
     * @return boolean
     */
    public boolean isTrabalhoPreAprovado() {
        if (this.getTrabalho().getStatusidstatus() != null) {
            if (this.getTrabalho().getStatusidstatus().getTipostatus().equalsIgnoreCase("Pré-Aprovado")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Função que verifica se o trabalho foi finalizado
     *
     * @return boolean
     */
    public boolean isTrabalhoFinalizado() {
        if (this.getTrabalho().getStatusidstatus() != null) {
            if (this.getTrabalho().getStatusidstatus().getTipostatus() != null) {
                if (this.getTrabalho().getStatusidstatus().getTipostatus().equals("Finalizado")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Função para verificar se o status está finalizado se estiver mostra a
     * data de finalização
     *
     * @return boolean
     */
    public boolean isExibirDataFinalizacao() {

        if (this.isMostrarDataFinalizacao() == true || isTrabalhoFinalizado() == true) {
            return true;
        }
        return false;
    }

    /**
     * Função para verificar se foi selecionado um curso e permitir a busca do
     * aluno e também verifica se o trabalho não foi finalizado
     *
     * @return boolean
     */
    public boolean isExibirAluno() {
        if (this.getIdCurso() == null || this.isTrabalhoFinalizado() == true) {
            return true;
        }
        return false;
    }

    /**
     * Função que valida cadastro
     *
     * @return boolean
     */
    public boolean validacaoCadastro() {
        boolean valido = true;

        if (isMostrarDataFinalizacao() == true && this.getTrabalho().getDatafinalizacao() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informar a data de finalização do trabalho.", ""));
            valido = false;
        }

        if (this.getTrabalho().getAlunomatricula().getMatricula() == null) {
            valido = false;
        }
        if (this.getTrabalho().getTitulo() == null) {
            valido = false;
        }
        if (this.getTrabalho().getAssuntoidassunto().getIdassunto() == null) {
            valido = false;
        }
        if (this.getTrabalho().getEstagioidestagio().getIdestagio() == null) {
            valido = false;
        }
        if (this.getNomeModalidade().equals("")) {
            valido = false;
        }
        if (this.getTrabalho().getEmpresaidempresa().getIdempresa() == null) {
            valido = false;
        }
        if (this.getOrientador().getSiape() == null) {
            valido = false;
        }
        if (this.getQtdHoras() == null) {
            valido = false;
        }
        if (this.getDefesa().getStatusdefesa() == null) {
            valido = false;
        } else if (this.getDefesa().getStatusdefesa() == true) {
            if (this.getTrabalho().getDatamatricula() == null) {
                valido = false;
            }
            if (this.getDataMinDefesa() == null) {
                valido = false;
            }
            if (this.getDataMaxDefesa() == null) {
                valido = false;
            }
        }
        return valido;
    }

    /**
     * função que cadastra trabalho
     *
     * @return pagina
     * @throws java.lang.Exception
     */
    public String cadastrar() throws Exception {

        if (this.validacaoCadastro() == true) {
            //-------------------------------------------------------------------
            //horas por dia de estagio
            this.getTrabalho().setQtdhorasdia(this.getQtdHoras());
            //-------------------------------------------------------------------
            //modalidade
            ModalidadeDAO mDAO = new ModalidadeDAO();
            Modalidade modalidade = new Modalidade();
            if (mDAO.getModalidadeByNome(this.getNomeModalidade().trim()) != null) {
                modalidade = mDAO.getModalidadeByNome(this.getNomeModalidade().trim());
                this.getTrabalho().setModalidadeidmodalidade(modalidade);
            } else {
                //se não existe insere nova modalidade
                modalidade.setNomemodalidade(this.getNomeModalidade());
                mDAO.insert(modalidade);
            }
            mDAO.closeSession();
            //-------------------------------------------------------------------
            //orientador
            this.getTrabalho().setProfessorsiape(this.getOrientador());
            //-------------------------------------------------------------------
            //status do trabalho "Pré-Aprovado";"Em andamento";"Finalizado"
//            TrabalhoStatusDAO sDAO = new TrabalhoStatusDAO();
//            Trabalhostatus status = sDAO.getStatusByTipo("Pré-Aprovado");
//            this.getTrabalho().setStatusidstatus(status);
//            sDAO.closeSession();
            //-------------------------------------------------------------------
            //inserir trabalho
            boolean realizado;
            TrabalhoDAO tDAO = new TrabalhoDAO();
            realizado = tDAO.insert(this.getTrabalho());
            tDAO.closeSession();
            //-------------------------------------------------------------------
            if (realizado == true) {
                //existe defesa?
                //feito assim porque nem sempre haverá defesa
                if (this.getDefesa().getStatusdefesa() == true) {
                    DefesaDAO dDAO = new DefesaDAO();
                    this.getDefesa().setTrabalhoidtrabalho(this.getTrabalho());
                    this.getDefesa().setMatriculadatadefesa(this.getTrabalho().getDatamatricula());
                    this.getDefesa().setMindatadefesa(this.getDataMinDefesa());
                    this.getDefesa().setMaxdatadefesa(this.getDataMaxDefesa());
                    dDAO.insert(this.getDefesa());
                    dDAO.closeSession();
                }
            }
            //-------------------------------------------------------------------
            if (this.getEnviarEmail().equals("sim") && realizado == true) {
                this.emailProfessor(this.getOrientador(), this.getTrabalho().getAlunomatricula(), this.getTrabalho());
                this.emailAluno(this.getOrientador(), this.getTrabalho().getAlunomatricula(), this.getTrabalho());
            } else {
                if (realizado == true) {
                    //limpar todos inputs do manager bean
                    sessao.destroyBean("TrabalhoBean");
                    FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));

                } else {
                    FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no cadastro!", ""));
                }
            }

        }
        return "cadastroTrabalho.xhtml?facesRedirect=true";
    }

    /**
     * Função para ouvir quando o status finalizado é selecionado
     *
     * @throws java.lang.Exception
     */
    public void statusListener() throws Exception {
        int idStatus;
        idStatus = this.getTrabalho().getStatusidstatus().getIdstatus();
        TrabalhoStatusDAO tsDAO = new TrabalhoStatusDAO();
        Trabalhostatus ts = tsDAO.getStatusById(idStatus);

        if (ts.getTipostatus().equalsIgnoreCase("Finalizado")) {
            this.setMostrarDataFinalizacao(true);
        } else {
            this.setMostrarDataFinalizacao(false);
        }
    }

    /**
     * função para ouvir quando o curso é selecionado e buscar os alunos do
     * curso
     *
     * @throws java.lang.Exception
     */
    public void cursoListener() throws Exception {
        this.getDatasDefesa();
        this.getListagemAlunos();

    }

    /**
     * função para ouvir quando a defesa é selecionado e mostrar as datas
     *
     * @throws java.lang.Exception
     */
    public void defesaListener() throws Exception {
        this.getDatasDefesa();
    }

    /**
     * função para alterar data min e max
     *
     * @throws java.lang.Exception
     */
    public void getDatasDefesa() throws Exception {
        if (this.getIdCurso() != null && this.getQtdHoras() != null) {
            int id = this.getIdCurso();
            CursoDAO cDAO = new CursoDAO();
            Curso cId = cDAO.getCursoByIdCurso(id);
            int horasPorDia = CalendarFormat.getHora(this.getQtdHoras());
            Date dateMin = CalendarFormat.getDiasDeEstagio(this.getTrabalho().getDatamatricula(), horasPorDia, cId.getHorasobrigatorias());
            this.setDataMinDefesa(dateMin);
            this.getDefesa().setMindatadefesa(dateMin);
            Date dateMax = CalendarFormat.getDataFinal(dateMin, 30);
            this.setDataMaxDefesa(dateMax);
            this.getDefesa().setMaxdatadefesa(dateMax);
            cDAO.closeSession();
        }
    }

    /**
     * função para alterar os dados do trabalho
     *
     * @throws java.lang.Exception
     */
    public void alterarTrabalho() throws Exception {
        boolean validaAlteracao = true;
        String statusAnterior = "";
        Trabalho tTemp = sessao.getTrabalhoAlterar();
        statusAnterior = tTemp.getStatusidstatus().getTipostatus();
        this.getTrabalho().setIdtrabalho(tTemp.getIdtrabalho());
        if (validacaoCadastro() == true) {
            //começo alteração defesa
            //feito assim porque nem sempre haverá defesa
            if (this.getDefesa().getStatusdefesa() == true) {
                DefesaDAO dDAO = new DefesaDAO();
                this.getDefesa().setTrabalhoidtrabalho(this.getTrabalho());
                this.getDefesa().setMatriculadatadefesa(this.getTrabalho().getDatamatricula());
                this.getDefesa().setMindatadefesa(this.getDataMinDefesa());
                this.getDefesa().setMaxdatadefesa(this.getDataMaxDefesa());
                Defesa dTemp = dDAO.getDefesaByIdTrabalho(this.getTrabalho().getIdtrabalho());
                if (dTemp != null) {
                    this.getDefesa().setIddefesa(dTemp.getIddefesa());
                    dDAO.update(this.getDefesa());
                } else {
                    dDAO.insert(this.getDefesa());
                }
                dDAO.closeSession();
            } else {
                //assim mesmo verifica porque se existir exclui
                //devido ter informado não há defesa no formulario
                //so podera excluir se não existir banca
                BancaDAO bDAO = new BancaDAO();
                if (bDAO.getExisteBancaByIdTrabalho(this.getTrabalho().getIdtrabalho()) == false) {
                    DefesaDAO dDAO = new DefesaDAO();
                    dDAO.delete(this.getDefesa());
                    dDAO.closeSession();
                } else {
                    validaAlteracao = false;
                    FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Não é possível excluir datas de defesa porque já existe banca cadastrada!", ""));
                }
            }
            //---------------------------------------------------------
            //começo da alteração do trabalho
            Boolean realizado = false;
            TrabalhoDAO aDAO = new TrabalhoDAO();
            //---------------------------------------------------------
            //pegar o status
            TrabalhoStatusDAO tsDAO = new TrabalhoStatusDAO();
            Trabalhostatus tsTemp = tsDAO.getStatusById(this.getTrabalho().getStatusidstatus().getIdstatus());
            this.getTrabalho().setStatusidstatus(tsTemp);
            tsDAO.closeSession();
            //---------------------------------------------------------
            //pegar o curso
            CursoDAO cDAO = new CursoDAO();
            Curso c = cDAO.getCursoByIdCurso(this.getIdCurso());
            this.getTrabalho().getAlunomatricula().setCursoidcurso(c);
            cDAO.closeSession();
            //---------------------------------------------------------
            //pegar modalidade
            ModalidadeDAO mDAO = new ModalidadeDAO();
            Modalidade modalidade = new Modalidade();
            if (mDAO.getModalidadeByNome(this.getNomeModalidade().trim()) != null) {
                modalidade = mDAO.getModalidadeByNome(this.getNomeModalidade().trim());
                this.getTrabalho().setModalidadeidmodalidade(modalidade);
            } else {
                //se não existe insere nova modalidade
                modalidade.setNomemodalidade(this.getNomeModalidade());
                mDAO.insert(modalidade);
            }
            mDAO.closeSession();
            //---------------------------------------------------------
            //pegar dados orientador
            ProfessorDAO professorOrientadorDAO = new ProfessorDAO();
            Professor newOrientador = professorOrientadorDAO.getProfessorBySiape(this.getOrientador().getSiape());
            this.getTrabalho().setProfessorsiape(newOrientador);
            this.setOrientador(newOrientador);
            professorOrientadorDAO.closeSession();
            //---------------------------------------------------------
            //pegar qtd horas
            this.getTrabalho().setQtdhorasdia(this.getQtdHoras());
            //---------------------------------------------------------
            BancaDAO bDAO = new BancaDAO();
            Banca bTemp = bDAO.getBancaByIdTrabalho(this.getTrabalho().getIdtrabalho());
            bDAO.closeSession();
            if (this.getTrabalho().getStatusidstatus().getTipostatus().equals("Finalizado")) {
                if (bTemp != null) {
                    //regra de verificação para poder finalizar o trabalho a sua banca
                    //deve estar com status "Finalizada"
                    if (bTemp.getStatusidstatus().getTipostatus().equals("Finalizada")) {
                        //verificar se data de finalização é maior que de matricula
                        if (this.getTrabalho().getDatamatricula().before(this.getTrabalho().getDatafinalizacao())) {
                            //verificar se data do trabalho de finalização é igual ou maior
                            //que a data da banca
                            if (this.getTrabalho().getDatafinalizacao().compareTo(bTemp.getDatabanca()) >= 0) {
                                realizado = aDAO.update(this.getTrabalho());
                                aDAO.closeSession();
                            } else {
                                FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Não é possível finalizar o trabalho porque a data de finalização tem que ser maior ou igual a data da apresentação a banca.", ""));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Não é possível finalizar o trabalho porque a data de finalização tem que ser maior que a data de matrícula.", ""));
                        }
                    } else {
                        Trabalho tTemp2 = sessao.getTrabalhoAlterar();
                        this.getTrabalho().setStatusidstatus(tTemp2.getStatusidstatus());
                        validaAlteracao = false;
                        FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Não é possível finalizar o trabalho porque a sua banca ainda não está finalizada.", ""));
                    }
                } else {
                    if (validaAlteracao == true) {
                        realizado = aDAO.update(this.getTrabalho());
                        aDAO.closeSession();
                    }
                }

            } else {
                if (bTemp != null) {
                    //-------------------------------------------------------------------
                    //regra para não voltar o status de "Em andamento" para "Pré-aprovado"
                    //se existir banca para o trabalho em questão
                    if (statusAnterior.equals("Em andamento")
                            && this.getTrabalho().getStatusidstatus().getTipostatus().equals("Pré-Aprovado")) {
                        validaAlteracao = false;
                        FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Não é possível voltar o status para Pré-Aprovado porque existe banca cadastrada para o trabalho.", ""));
                    }
                    //-------------------------------------------------------------------
                }

                if (validaAlteracao == true) {
                    realizado = aDAO.update(this.getTrabalho());
                    aDAO.closeSession();
                }
            }

            this.atualizaVisualizaTrabalhos();
            if (realizado == true && validaAlteracao == true) {
                FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
            } else if (realizado == false && validaAlteracao == true) {
                FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!\n Informe o admistrador.", ""));

            }
        }
    }

    /**
     * função que direciona para pagina de visualizar trabalho
     *
     * @return
     */
    public String direcionaVisualizarTrabalho() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("TrabalhoBean");
        return "visualizarTrabalho.xhtml?facesRedirect=true";
    }

    /**
     * função que direciona para pagina de visualizar orientandos
     *
     * @return
     */
    public String direcionaVisualizarOrientandos() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("TrabalhoBean");
        return "visualizarOrientandos.xhtml?facesRedirect=true";
    }

    /**
     * função que direciona para pagina de cadastro de trabalho
     *
     * @return
     */
    public String direcionaCadastroTrabalho() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("TrabalhoBean");
        return "cadastroTrabalho.xhtml?facesRedirect=true";
    }

    /**
     * função que direciona para pagina cadastro banca
     *
     * @return
     */
    public String direcionaCadastroBanca() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("TrabalhoBean");
        this.sessao.destroyBean("BancaBean");
        return "cadastroBanca.xhtml?facesRedirect=true";
    }

    /**
     * função que direciona para pagina de alteração de dados da banca
     *
     * @param t
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarBanca(Trabalho t) throws Exception {
        this.sessao.destroyBean("TrabalhoBean");
        this.sessao.destroyBean("BancaBean");
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("Trabalho", t);
        return "alteraBanca.xhtml?facesRedirect=true";
    }

    /**
     * função que direciona para pagina de alteração de dados do trabalho
     *
     * @param t
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarTrabalho(Trabalho t) throws Exception {
        this.sessao.destroyBean("TrabalhoBean");
        sessao.getSession().setAttribute("alterarTrabalho", t);
        return "alteraTrabalho.xhtml?facesRedirect=true";
    }

    /**
     * função que retorna o trabalho que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Trabalho getCarregarTrabalho() throws Exception {
        sessao.verificaAcesso();
        this.setTrabalho(sessao.getTrabalhoAlterar());
        //-------------------------------------------------------
        //buscar o curso e o aluno
        this.getListagemCursos();
        this.idCurso = this.getTrabalho().getAlunomatricula().getCursoidcurso().getIdcurso();
        this.getListagemAlunos();
        //-------------------------------------------------------
        //buscar modalidade
        this.setNomeModalidade(this.getTrabalho().getModalidadeidmodalidade().getNomemodalidade());
        //-------------------------------------------------------
        //busca orientador
        this.setOrientador(this.getTrabalho().getProfessorsiape());
        //-------------------------------------------------------
        //busca horas de estagio
        this.setQtdHoras(this.getTrabalho().getQtdhorasdia());
        //-------------------------------------------------------
        //busca data matricula
        this.getTrabalho().setDatamatricula(this.getTrabalho().getDatamatricula());
        //-------------------------------------------------------
        //buscar defesa
        DefesaDAO dDAO = new DefesaDAO();
        if (dDAO.getDefesaByIdTrabalho(this.getTrabalho().getIdtrabalho()) != null) {
            Defesa defesaConf = dDAO.getDefesaByIdTrabalho(this.getTrabalho().getIdtrabalho());
            dDAO.closeSession();
            this.getDefesa().setIddefesa(defesaConf.getIddefesa());
            this.getDefesa().setMatriculadatadefesa(defesaConf.getMatriculadatadefesa());
            this.getDefesa().setStatusdefesa(defesaConf.getStatusdefesa());
            this.getDefesa().setMindatadefesa(defesaConf.getMindatadefesa());
            this.getDefesa().setMaxdatadefesa(defesaConf.getMaxdatadefesa());
            this.getDefesa().setTrabalhoidtrabalho(this.getTrabalho());
            this.setDataMinDefesa(defesaConf.getMindatadefesa());
            this.setDataMaxDefesa(defesaConf.getMaxdatadefesa());
        }

        return this.getTrabalho();
    }

    /**
     * enviar email para professor e aluno
     *
     */
    @SuppressWarnings("unchecked")
    public void confirmacaoTrabalhoEmail() {

        if (this.getTrabalho().getIdtrabalho() == null) {
            Trabalho tTemp;
            try {
                tTemp = sessao.getTrabalhoAlterar();
                this.setTrabalho(tTemp);
            } catch (Exception ex) {
                Logger.getLogger(TrabalhoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (this.getTrabalho().getIdtrabalho() != null) {
            EmailConfigDAO ecDAO = new EmailConfigDAO();
            try {
                this.setEmailConfig(ecDAO.getEmailConfig());
                ecDAO.closeSession();
            } catch (Exception ex) {
                Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            StringBuilder destino = new StringBuilder();
            destino.append(this.getTrabalho().getProfessorsiape().getEmail()).append(";");
            destino.append(this.getTrabalho().getAlunomatricula().getEmail());

            this.setDestinatarios(destino.toString());
            this.setTitulo("FACOM/UFU - Coordenacao de Estagio Supervisionado");

            StringBuilder msgEmail = new StringBuilder();
            msgEmail.append("<html><body>");
            msgEmail.append("Bom dia Prof. ").append(this.getTrabalho().getProfessorsiape().getNome()).append(",");
            msgEmail.append("<br/><br/>");
            msgEmail.append("Gostariamos de informar que lhe foi conferida a orientacao de ")
                    .append(this.getTrabalho().getEstagioidestagio().getTipoestagio())
                    .append(" do aluno ").append(this.getTrabalho().getAlunomatricula().getNome())
                    .append(" do curso de ").append(this.getTrabalho().getAlunomatricula().getCursoidcurso().getNomecurso());
            msgEmail.append("<br/><br/>");
            msgEmail.append("O plano de atividades do aluno lhe sera entregue em breve (em seu escaninho).");
            msgEmail.append("<br/><br/>");
            msgEmail.append("O email do aluno é ").append(this.getTrabalho().getAlunomatricula().getEmail());
            msgEmail.append("<br/><br/>");
            msgEmail.append("Estamos a disposição para quaisquer esclarecimentos.");
            msgEmail.append("<br/><br/>");
            msgEmail.append("Antecipando agradecimentos,");

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

    public String getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(String enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = trabalho;
    }

    public boolean isMostrarDataFinalizacao() {
        return mostrarDataFinalizacao;
    }

    public void setMostrarDataFinalizacao(boolean mostrarDataFinalizacao) {
        this.mostrarDataFinalizacao = mostrarDataFinalizacao;
    }
}
