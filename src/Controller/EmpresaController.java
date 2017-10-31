/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosEmpresa;
import Dao.EmpresaDAO;
import Model.Empresa;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "EmpresaBean")
@SessionScoped
public class EmpresaController extends DadosEmpresa {

    private String mensagemErro = "";
    private String enviarEmail = "";
    private Empresa empresa = new Empresa();
    private final SistemaSession sessao = SistemaSession.getInstance();

    public EmpresaController() {
    }

    /**
     * retornar todas empresas cadastrados
     *
     */
    @PostConstruct
    public void init() {
        try {
            this.getListaEmpresas().clear();
            EmpresaDAO eDAO = new EmpresaDAO();
            List<Empresa> allEmpresas = eDAO.getAllEmpresas();
            this.setListaEmpresas(allEmpresas);
            eDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * função que direciona para pagina de visualizar
     *
     * @return
     */
    public String direcionaVisualizarEmpresa() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("EmpresaBean");
        return "visualizarEmpresa.xhtml?facesRedirect=true";
    }

    /**
     * valida um cadastro de uma empresa
     *
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValido() throws Exception {
        boolean isValido = true;

        if (empresa.getNome().equals("") || empresa.getNome() == null) {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o nome da empresa.", ""));
            isValido = false;
        }

        if (empresa.getEndereco().equals("") || empresa.getEndereco() == null) {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o endereço da empresa.", ""));
            isValido = false;
        }

        if (empresa.getArea().equals("") || empresa.getArea() == null) {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite a área da empresa.", ""));
            isValido = false;
        }

        if (empresa.getTelefone().equals("") || empresa.getTelefone() == null) {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite o telefone da empresa.", ""));
            isValido = false;
        }

        return isValido;
    }

    /**
     * cadastra uma empresa
     *
     * @throws java.lang.Exception
     */
    public void cadastrar() throws Exception {
        if (isValido()) {
            EmpresaDAO eDAO = new EmpresaDAO();
            if (eDAO.getEmpresaByNome(empresa.getNome()) != null) {
                FacesContext.getCurrentInstance().addMessage("growlMessage",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Empresa já cadastrado.", ""));
            } else {
                String nome = empresa.getNome();
                nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
                empresa.setNome(nome);
                eDAO.insert(empresa);
                eDAO.closeSession();
                FacesContext.getCurrentInstance().addMessage("growlMessage",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
            }
        }
    }

    /**
     * altera os dados de um professor
     *
     * @throws java.lang.Exception
     */
    public void alterarEmpresa() throws Exception {
        Boolean realizado;
        EmpresaDAO eDAO = new EmpresaDAO();
        Empresa eTemp = sessao.getEmpresaAlterar();
        this.getEmpresa().setIdempresa(eTemp.getIdempresa());
        realizado = eDAO.update(this.getEmpresa());
        eDAO.closeSession();
        if (realizado == true) {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Alteração Realizada com Sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlEmpresa",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao atualizar dados!", "Informe o admistrador."));
        }
    }

    /**
     * direciona para pagina de alteração de dados de um curso
     *
     * @param e
     * @return
     * @throws java.lang.Exception
     */
    public String direcionaAlterarEmpresa(Empresa e) throws Exception {
        sessao.getSession().setAttribute("alterarEmpresa", e);
        return "alteraEmpresa.xhtml?facesRedirect=true";
    }

    /**
     * retorna o curso que será alterado
     *
     * @return
     * @throws java.lang.Exception
     */
    public Empresa getEmpresaAlterar() throws Exception {
        sessao.verificaAcesso();
        this.empresa = sessao.getEmpresaAlterar();
        return empresa;
    }

    /**
     *
     *
     * @return
     */
    public boolean isErro() {
        return mensagemErro == null || (mensagemErro.equals(""));
    }

    /**
     *
     *
     * @return
     */
    public String validaDadosCurso() {
        int erro = 0;
        if (empresa.getNome().equals("")) {
            this.setMensagemErro("Preencher campo nome empresa.");
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}
