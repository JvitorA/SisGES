/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosAluno;
import Dao.AlunoDAO;
import Email.CommonsEmail;
import Model.Aluno;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "AlunoBean")
@SessionScoped
public class AlunoController extends DadosAluno {

    private String mensagemErro = "";
    private Aluno aluno = new Aluno();
    private final SistemaSession sessao = SistemaSession.getInstance();
    private String enviarEmail = "";

    public AlunoController() {
    }

    /**
     * retornar todos alunos cadastradosas
     *
     *
     */
    @PostConstruct
    public void init() {
        try {
            this.getListaAlunos().clear();
            AlunoDAO aDAO = new AlunoDAO();
            List<Aluno> allAlunos = aDAO.getAllAlunos();
            this.setListaAlunos(allAlunos);
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
    public String direcionaVisualizarAluno() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("AlunoBean");
        return "visualizarAluno.xhtml?facesRedirect=true";
    }

    /**
     * validacao de um cadastro de aluno
     *
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValido() throws Exception {
        boolean isValido = true;

        if (aluno.getNome().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome", ""));
            isValido = false;
        }
        if (aluno.getMatricula().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite a matricula.", ""));
            isValido = false;
        }

        if (aluno.getEmail().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o email", ""));
            isValido = false;
        }
        if (aluno.getTelefone().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o telefone", ""));
            isValido = false;
        }

        if (isValido == true) {
            sessao.getSession().setAttribute("aluno", aluno);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgEmail').show()");
        }
        return isValido;
    }

    /**
     * cadastra um aluno
     *
     * @throws java.lang.Exception
     */
    public void cadastrar() throws Exception {
        aluno = (Aluno) sessao.getSession().getAttribute("aluno");

        AlunoDAO aDAO = new AlunoDAO();
        if (aDAO.getAlunoByMatricula(aluno.getMatricula()) != null) {
            FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aluno já cadastrado.", ""));
        } else {
            boolean realizado;
            String nome = aluno.getNome();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
            aluno.setNome(nome);
            realizado = aDAO.insert(aluno);
            aDAO.closeSession();

            if (this.getEnviarEmail().equals("sim")) {
                CommonsEmail email = new CommonsEmail();
                //----------------------------------------------------------------------------
                StringBuilder msgEmail = new StringBuilder();
                msgEmail.append("<html><body>");
                msgEmail.append("<h1>SisGES - Sistema para Gestão de Estágios Supervisionados</h1>");
                msgEmail.append("<br/>");
                msgEmail.append("<h3>Bem vindo, ").append(aluno.getNome()).append("! </h3>");
                msgEmail.append("<h4>Cadastro no SisGES Realizado com sucesso!</h4>");
                msgEmail.append("<br/>");
                msgEmail.append("</body></html>");
                String titulo = "Login Sistema SisGES";
                boolean enviado = email.enviarEmail(titulo, msgEmail.toString(), aluno.getEmail());
                //----------------------------------------------------------------------------
                if (enviado == true) {
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + aluno.getEmail(), ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
                }

                if (realizado == true) {
                    sessao.getSession().removeAttribute("aluno");
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!\nEmail enviado para " + aluno.getEmail(), ""));

                } else {
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro não realizado!", ""));
                }
            } else {
                if (realizado == true) {
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage("growlAlunoDialog",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro não realizado!", ""));
                }
            }
        }
    }

    /**
     * alterar dados de um aluno
     *
     * @param a
     * @throws java.lang.Exception
     */
    public void alterarAluno(Aluno a) throws Exception {
        Boolean realizado;
        String matricula;
        matricula = this.getAlunoAlterar().getMatricula();
        a.setMatricula(matricula);
        AlunoDAO aDAO = new AlunoDAO();
        realizado = aDAO.update(a);
        aDAO.closeSession();
        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlAluno",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!\n Informe o admistrador.", ""));
        }
    }

    /**
     * direciona para pagina de alteração de dados de um professor
     *
     * @param aluno
     * @return
     */
    public String direcionaAlterarAluno(Aluno aluno) {
        sessao.getSession().setAttribute("alterarAluno", aluno);
        return "alteraAluno.xhtml?facesRedirect=true";

    }

    public void onCheckBoxSelect(SelectEvent event) {
        DataTable objDataTable = (DataTable) event.getSource();
        System.out.println("Selected checkbox row index: " + objDataTable.getRowIndex());
        //objDataTable.getRowData();

    }

    /**
     * retorna o professor que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Aluno getAlunoAlterar() throws Exception {
        sessao.verificaAcesso();
        this.aluno = sessao.getAlunoAlterar();
        return aluno;
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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

}
