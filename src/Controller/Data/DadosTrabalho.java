/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Dao.AlunoDAO;
import Dao.AssuntoDAO;
import Dao.CampusDAO;
import Dao.CursoDAO;
import Dao.EmpresaDAO;
import Dao.EstagioDAO;
import Dao.ModalidadeDAO;
import Dao.OrientacaoFilaDAO;
import Dao.ProfessorDAO;
import Dao.TrabalhoStatusDAO;
import Email.CommonsEmail;
import Model.Aluno;
import Model.Assunto;
import Model.Campus;
import Model.Curso;
import Model.Defesa;
import Model.EmailConfig;
import Model.Empresa;
import Model.Estagio;
import Model.Modalidade;
import Model.Orientacaofila;
import Model.Professor;
import Model.Trabalho;
import Model.Trabalhostatus;
import Util.CalendarFormat;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Alessandro
 */
public class DadosTrabalho implements Serializable {

    //lista que forma a tabela com todos alunos
    protected List<Trabalho> listaTrabalhos;
    //lista que forma a tabela com todos orientandos de um professor
    protected List<Trabalho> listaOrientandos;
    //filtro da tabela visualizar trabalhos
    protected List<Trabalho> filtroTrabalhos;
    //filtro da tabela visualizar orientandos
    protected List<Trabalho> filtroOrientandos;
    protected Professor orientador;
    protected Defesa defesa;
    protected Date qtdHoras;
    protected Date dataMinDefesa;
    protected Date dataMaxDefesa;
    protected Date dataAtual;
    protected Integer idCurso;
    protected String nomeModalidade;
    protected String statusDefesa;
    protected List<SelectItem> listaAlunos;
    protected List<SelectItem> listaCursos;
    protected List<SelectItem> listaCoordenadores;

    EmailConfig emailConfig = new EmailConfig();
    String destinatarios = "";
    String mensagem = "";
    String titulo = "";

    @SuppressWarnings("unchecked")
    public DadosTrabalho() {
        this.dataAtual = CalendarFormat.getDataSO();
        this.nomeModalidade = "";
        this.orientador = new Professor();
        this.defesa = new Defesa();
        this.listaTrabalhos = new ArrayList();
        this.listaAlunos = new ArrayList<>();
        this.listaCursos = new ArrayList<>();
        this.listaCoordenadores = new ArrayList<>();
    }

    public void emailProfessor(Professor p, Aluno a, Trabalho t) throws EmailException, MalformedURLException, Exception {
        CommonsEmail email = new CommonsEmail();
        //----------------------------------------------------------------------------
        StringBuilder msgEmail = new StringBuilder();
        msgEmail.append("<html><body>");
        msgEmail.append("<h1>SisGES - Sistema para Gestão de Estágios Supervisionados</h1>");
        msgEmail.append("<br/>");
        msgEmail.append("<h3>Bom dia, ").append(p.getNome()).append("! </h3>");
        msgEmail.append("<h4>Realizado o cadastro no SisGES com sucesso!</h4>");
        msgEmail.append("<h4>Orientador: ").append(p.getNome()).append("! </h4>");
        msgEmail.append("<h4>Aluno: ").append(a.getNome()).append("! </h4>");
        msgEmail.append("<h4>Título do trabalho: ").append(t.getTitulo()).append("! </h4>");
        msgEmail.append("<br/>");
        msgEmail.append("</body></html>");
        String titulo = "Estágio FACOM-UFU";
        boolean enviado = email.enviarEmail(titulo, msgEmail.toString(), p.getEmail());
        //----------------------------------------------------------------------------
        if (enviado == true) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + p.getEmail(), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
        }
        //----------------------------------------------------------------------------
        FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!\nEmail enviado para " + p.getEmail(), ""));
    }

    public void emailAluno(Professor p, Aluno a, Trabalho t) throws EmailException, MalformedURLException, Exception {
        CommonsEmail email = new CommonsEmail();
        //----------------------------------------------------------------------------
        StringBuilder msgEmail = new StringBuilder();
        msgEmail.append("<html><body>");
        msgEmail.append("<h1>SisGES - Sistema para Gestão de Estágios Supervisionados</h1>");
        msgEmail.append("<br/>");
        msgEmail.append("<h3>Bom dia, ").append(a.getNome()).append("! </h3>");
        msgEmail.append("<h4>Realizado o cadastro no SisGES com sucesso!</h4>");
        msgEmail.append("<h4>Orientador de estágio: ").append(p.getNome()).append("! </h4>");
        msgEmail.append("<h4>Título do trabalho: ").append(t.getTitulo()).append("! </h4>");
        msgEmail.append("<br/>");
        msgEmail.append("</body></html>");
        String titulo = "Estágio FACOM-UFU";
        boolean enviado = email.enviarEmail(titulo, msgEmail.toString(), a.getEmail());
        //----------------------------------------------------------------------------
        if (enviado == true) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso!\nEmail enviado para " + p.getEmail(), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar email!", ""));
        }

        //----------------------------------------------------------------------------
        FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!\nEmail enviado para " + a.getEmail(), ""));
    }

    /**
     * função para retornar todos os campus
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaCampus() throws Exception {
        CampusDAO eDAO = new CampusDAO();
        List<Campus> all = eDAO.getAllCampus();
        List<SelectItem> itens = new ArrayList<>(all.size());

        for (Campus e : all) {
            //value,label
            String campi = e.getNomecampus() + " - " + e.getCidade();
            SelectItem s = new SelectItem(e.getIdcampus(), campi);
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os coordenadores do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaCoordenador() throws Exception {
        ProfessorDAO eDAO = new ProfessorDAO();
        List<Professor> allCoordenadores = eDAO.getAllCoordenadores();
        List<SelectItem> itens = new ArrayList<>(allCoordenadores.size());

        for (Professor e : allCoordenadores) {
            //value,label
            SelectItem s = new SelectItem(e.getSiape(), e.getNome());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os professores de acordo com o calculo para
     * gerar a lista de orientacao calculo = intervalo de meses que participou
     * da ultima orientacao / numero de orientacoes que partipou
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaProfessorParaOrientacao() throws Exception {
        OrientacaoFilaDAO eDAO = new OrientacaoFilaDAO();
        List<Orientacaofila> allProfessor = eDAO.getFila();
        List<SelectItem> itens = new ArrayList<>(allProfessor.size());

        for (Orientacaofila e : allProfessor) {
            //value,label
            SelectItem s = new SelectItem(e.getSiape(), e.getNome());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os professores do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaProfessor() throws Exception {
        ProfessorDAO eDAO = new ProfessorDAO();
        List<Professor> allProfessor = eDAO.getAllProfessores();
        List<SelectItem> itens = new ArrayList<>(allProfessor.size());

        for (Professor e : allProfessor) {
            //value,label
            SelectItem s = new SelectItem(e.getSiape(), e.getNome());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos as empresas do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaEmpresa() throws Exception {
        EmpresaDAO eDAO = new EmpresaDAO();
        List<Empresa> allEmpresa = eDAO.getAllEmpresas();
        List<SelectItem> itens = new ArrayList<>(allEmpresa.size());

        for (Empresa e : allEmpresa) {
            //value,label
            SelectItem s = new SelectItem(e.getIdempresa(), e.getNome());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos as modalidades de estagio do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaModalidade() throws Exception {
        ModalidadeDAO eDAO = new ModalidadeDAO();
        List<Modalidade> allModalidade = eDAO.getAllModalidades();
        List<SelectItem> itens = new ArrayList<>(allModalidade.size());

        for (Modalidade e : allModalidade) {
            //value,label
            SelectItem s = new SelectItem(e.getIdmodalidade(), e.getNomemodalidade());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os tipos de estagio do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaTipoEstagio() throws Exception {
        EstagioDAO eDAO = new EstagioDAO();
        List<Estagio> allEstagio = eDAO.getAllEstagios();
        List<SelectItem> itens = new ArrayList<>(allEstagio.size());

        for (Estagio e : allEstagio) {
            //value,label
            SelectItem s = new SelectItem(e.getIdestagio(), e.getTipoestagio());
            itens.add(s);
        }
        eDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os status do trabalho
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaTrabalhoStatus() throws Exception {
        TrabalhoStatusDAO tsDAO = new TrabalhoStatusDAO();
        List<Trabalhostatus> allStatus = tsDAO.getAllTrabalhoStatus();
        List<SelectItem> itens = new ArrayList<>(allStatus.size());

        for (Trabalhostatus ts : allStatus) {
            //value,label
            SelectItem s = new SelectItem(ts.getIdstatus(), ts.getTipostatus());
            itens.add(s);
        }
        tsDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os assuntos do banco de dados
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaAssuntos() throws Exception {

        AssuntoDAO aDAO = new AssuntoDAO();
        List<Assunto> allAssunto = aDAO.getAllAssuntos();
        List<SelectItem> itens = new ArrayList<>(allAssunto.size());

        for (Assunto a : allAssunto) {
            //value,label
            SelectItem s = new SelectItem(a.getIdassunto(), a.getNome());
            itens.add(s);
        }
        aDAO.closeSession();
        return itens;
    }

    /**
     * função para retornar todos os alunos do banco de um curso
     *
     * @return
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListagemAlunos() throws Exception {
        if (this.getIdCurso() != null) {
            listaAlunos.clear();
            AlunoDAO aDAO = new AlunoDAO();
            int idCursoTemp = this.getIdCurso();
            List<Aluno> alunos = aDAO.getAlunoByCurso(idCursoTemp);
            for (Aluno a : alunos) {
                //value,label
                SelectItem s = new SelectItem(a.getMatricula(), a.getNome());
                listaAlunos.add(s);
            }
            aDAO.closeSession();
        }
        return listaAlunos;
    }

    /**
     * função para retornar o número de anos em comparação com o ano da data
     * atual
     *
     * @return
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListagemAno() throws Exception {

        List<SelectItem> listaAno = new ArrayList<>();
        String ano = CalendarFormat.getAnoToString();
        Integer minAno = Integer.parseInt(ano) - 5;
        Integer maxAno = Integer.parseInt(ano) + 5;

        for (int i = minAno; i < maxAno; i++) {
            //value,label
            SelectItem s = new SelectItem(i, String.valueOf(i));
            listaAno.add(s);
        }
        return listaAno;
    }

    /**
     * função para retornar todos os cursos do banco de dados
     *
     * @return
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListagemCursos() throws Exception {
        CursoDAO cDAO = new CursoDAO();
        List<Curso> allCursos = cDAO.getAllCursos();
        listaCursos.clear();
        for (Curso curso : allCursos) {
            //value,label
            SelectItem s = new SelectItem(curso.getIdcurso(), curso.getNomecurso());
            listaCursos.add(s);
        }
        cDAO.closeSession();
        return listaCursos;
    }

    /**
     * função para retornar todos nomes de alunos com possiblidade de ser o
     * procurado
     *
     * @param query
     * @return List
     * @throws java.lang.Exception
     */
    public List<String> completeText(String query) throws Exception {
        List<String> results = new ArrayList<>();
        AlunoDAO aDAO = new AlunoDAO();
        int idCursoTemp = this.getIdCurso();
        List<Aluno> alunos = aDAO.getAlunoByNomeAndCurso(query, idCursoTemp);
        for (Aluno a : alunos) {
            results.add(a.getNome());
        }
        aDAO.closeSession();
        return results;
    }

    /**
     * função para verificar se foi selecionado que haverá defesa assim permitir
     * selecionar as datas
     *
     * @return boolean
     */
    public boolean isExibirDefesa() {
        return this.getDefesa().getStatusdefesa() == true;
    }

    /**
     * função para verificar se exibe as datas de defesa
     *
     * @return boolean
     */
    public boolean isExibirDatasDefesa() {
        return this.getQtdHoras() != null;
    }

    //-------------------------------------------------------------------------------
    public List<Trabalho> getListaTrabalhos() {
        return listaTrabalhos;
    }

    public void setListaTrabalhos(List<Trabalho> listaTrabalhos) {
        this.listaTrabalhos = listaTrabalhos;
    }

    public List<Trabalho> getFiltroTrabalhos() {
        return filtroTrabalhos;
    }

    public void setFiltroTrabalhos(List<Trabalho> filtroTrabalhos) {
        this.filtroTrabalhos = filtroTrabalhos;
    }

    public String getNomeModalidade() {
        return nomeModalidade;
    }

    public void setNomeModalidade(String nomeModalidade) {
        this.nomeModalidade = nomeModalidade;
    }

    public Professor getOrientador() {
        return orientador;
    }

    public void setOrientador(Professor orientador) {
        this.orientador = orientador;
    }

    public Defesa getDefesa() {
        return defesa;
    }

    public void setDefesa(Defesa defesa) {
        this.defesa = defesa;
    }

    public Date getQtdHoras() {
        return qtdHoras;
    }

    public void setQtdHoras(Date qtdHoras) {
        this.qtdHoras = qtdHoras;
    }

    public Date getDataMinDefesa() {
        return dataMinDefesa;
    }

    public void setDataMinDefesa(Date dataMinDefesa) {
        this.dataMinDefesa = dataMinDefesa;
    }

    public Date getDataMaxDefesa() {
        return dataMaxDefesa;
    }

    public void setDataMaxDefesa(Date dataMaxDefesa) {
        this.dataMaxDefesa = dataMaxDefesa;
    }

    public Date getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(Date dataAtual) {
        this.dataAtual = dataAtual;
    }

    public String getStatusDefesa() {
        return statusDefesa;
    }

    public void setStatusDefesa(String statusDefesa) {
        this.statusDefesa = statusDefesa;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public List<SelectItem> getListaAlunos() {
        return listaAlunos;
    }

    public void setListaAlunos(List<SelectItem> listaAlunos) {
        this.listaAlunos = listaAlunos;
    }

    public List<SelectItem> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<SelectItem> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List<SelectItem> getListaCoordenadores() {
        return listaCoordenadores;
    }

    public void setListaCoordenadores(List<SelectItem> listaCoordenadores) {
        this.listaCoordenadores = listaCoordenadores;
    }

    public List<Trabalho> getListaOrientandos() {
        return listaOrientandos;
    }

    public void setListaOrientandos(List<Trabalho> listaOrientandos) {
        this.listaOrientandos = listaOrientandos;
    }

    public List<Trabalho> getFiltroOrientandos() {
        return filtroOrientandos;
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
