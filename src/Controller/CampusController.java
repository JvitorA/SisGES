/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosCampus;
import Dao.CampusDAO;
import Model.Campus;
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
@ManagedBean(name = "CampusBean")
@SessionScoped
public class CampusController extends DadosCampus {

    private String mensagemErro = "";
    private Campus campus = new Campus();
    private final SistemaSession sessao = SistemaSession.getInstance();
    private String enviarEmail = "";

    public CampusController() {
    }

    /**
     * retornar todos campi cadastrados
     *
     *
     */
    @PostConstruct
    public void init() {
        try {
            this.getListaCampus().clear();
            CampusDAO aDAO = new CampusDAO();
            List<Campus> all = aDAO.getAllCampus();
            this.setListaCampus(all);
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
    public String direcionaVisualizarCampus() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("CampusBean");
        return "visualizarCampus.xhtml?facesRedirect=true";
    }

    /**
     * validacao de um cadastro de campus
     *
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValido() throws Exception {
        boolean isValido = true;

        if (campus.getNomecampus().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome do campus", ""));
            isValido = false;
        }
        if (campus.getCep().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o cep.", ""));
            isValido = false;
        }

        if (campus.getCidade().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite a cidade", ""));
            isValido = false;
        }
        if (campus.getEstado().equals("")) {
            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o estado", ""));
            isValido = false;
        }
        return isValido;
    }

    /**
     * cadastra um campus
     *
     * @throws java.lang.Exception
     */
    public void cadastrar() throws Exception {

        if (isValido()) {
            CampusDAO aDAO = new CampusDAO();

            if (aDAO.getCampusByNome(campus.getNomecampus()) != null) {
                FacesContext.getCurrentInstance().addMessage("growlMessage",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campus já cadastrado.", ""));
            } else {
                boolean realizado;
                String estadosigla;

                String nome = campus.getNomecampus();
                nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
                campus.setNomecampus(nome);

                String estado = campus.getEstado();
                String[] split = estado.split("-");
                estado = split[0];
                estadosigla = split[1];

                campus.setEstado(estado);
                campus.setEstadosigla(estadosigla);

                realizado = aDAO.insert(campus);
                aDAO.closeSession();

                if (realizado == true) {
                    FacesContext.getCurrentInstance().addMessage("growlMessage",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage("growlMessage",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro não realizado!", ""));
                }
            }
        }
    }

    /**
     * alterar dados de um campus
     *
     * @param a
     * @throws java.lang.Exception
     */
    public void alterarCampus(Campus cp) throws Exception {
        Boolean realizado;
        String nome;
        String estadosigla;

        CampusDAO aDAO = new CampusDAO();
        this.setCampus(this.getCampusAlterar());
        Campus campusAntesAlteracao = aDAO.getCampusByNome(this.getCampus().getNomecampus());

        cp.setIdcampus(campusAntesAlteracao.getIdcampus());

        nome = cp.getNomecampus();
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
        cp.setNomecampus(nome);

        String estado = cp.getEstado();
        String[] split = estado.split("-");
        estado = split[0];
        estadosigla = split[1];

        cp.setEstado(estado);
        cp.setEstadosigla(estadosigla);

        realizado = aDAO.update(cp);
        aDAO.closeSession();
        if (realizado == true) {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            session.setAttribute("alterarCampus", cp);

            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlCampus",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!\n Informe o admistrador.", ""));
        }
    }

    /**
     * direciona para pagina de alteração de dados do campus
     *
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarCampus(Campus p) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

        session.setAttribute("alterarCampus", p);
        return "alteraCampus.xhtml?facesRedirect=true";
    }

    /**
     * retorna o campus que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Campus getCampusAlterar() throws Exception {
        sessao.verificaAcesso();
        this.campus = sessao.getCampusAlterar();
        return campus;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

}
