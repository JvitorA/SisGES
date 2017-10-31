/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Data.DadosRelatorio;
import Dao.AlunoDAO;
import Dao.BancaDAO;
import Dao.CampusDAO;
import Dao.ConvidadoDAO;
import Dao.CursoDAO;
import Dao.ProfessorDAO;
import Model.Alunoaprovado;
import Model.Alunomatriculado;
import Model.Banca;
import Model.Campus;
import Model.Convidado;
import Model.Curso;
import Model.Professor;
import Model.Professororientacao;
import Model.Professororientacaoseparada;
import Model.Sumarioaprovado;
import Model.Sumariomatricula;
import Util.CalendarFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "RelatorioBean")
@SessionScoped
public class RelatorioController extends DadosRelatorio {

    /**
     * função que direciona para pagina de visualizar trabalho
     *
     * @return
     */
    public String direcionaVisualizarRelatorio() {
        //limpar todos inputs do manager bean
        this.sessao.destroyBean("RelatorioBean");
        return "visualizarRelatorio.xhtml?facesRedirect=true";
    }

    /**
     * Função para validação dos inputs para gerar relatório Nº de Orientações
     * por Professor
     *
     * @return valido
     */
    public boolean isValidoOrientacoes() {

        boolean valido = true;

        if (this.getDataInicial() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data inícial.", ""));
            valido = false;
        }

        if (this.getDataFinal() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data final.", ""));
            valido = false;
        }

        if (this.getDataInicial() != null && this.getDataFinal() != null) {
            if (this.getDataInicial().after(this.getDataFinal())) {
                FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dada Final deve ser maior que a Data Inícial.", ""));
                valido = false;
            }

        }

        return valido;
    }

    /**
     * Função para validação dos inputs para gerar relatório Participação em
     * banca
     *
     * @return valido
     */
    public boolean isValidoParticipacaoEmBanca() {
        boolean valido = true;
        if (this.getProfessor().getSiape() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o professor.", ""));
            valido = false;
        }

        if (this.getDataInicial() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data inícial.", ""));
            valido = false;
        }

        if (this.getDataFinal() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a data final.", ""));
            valido = false;
        }

        if (this.getDataInicial() != null && this.getDataFinal() != null) {
            if (this.getDataInicial().after(this.getDataFinal())) {
                FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dada Final deve ser maior que a Data Inícial.", ""));
                valido = false;
            }

        }

        return valido;
    }

    /**
     * Função para validação dos inputs para gerar relatório Anual de atividades
     *
     *
     * @return valido
     */
    public boolean isValidoDadosAtividadesAnual() {
        boolean valido = true;
        if (this.getCurso().getIdcurso() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o curso.", ""));
            valido = false;
        }
        if (this.getAno() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o ano.", ""));
            valido = false;
        }

        if (this.getCoordenador().getSiape() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o coordenador.", ""));
            valido = false;
        }

        if (this.getCampus().getIdcampus() == null) {
            FacesContext.getCurrentInstance().addMessage("growlTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o campus.", ""));
            valido = false;
        }

        return valido;
    }

    /**
     * Função para gerar relatórios
     */
    public void gerarRelatorio() {

        Date dtInicial;
        Date dtFinal;
        //String ano;
        switch (this.getNomeRelatorio()) {
            case "participacaoEmBanca":
                if (this.isValidoParticipacaoEmBanca() == true) {
                    Professor p = this.getProfessor();
                    dtInicial = this.getDataInicial();
                    dtFinal = this.getDataFinal();

                    try {
                        BancaDAO bDAO = new BancaDAO();
                        List<Banca> lOrientacao = bDAO.getAllBancasByOrientador(p.getSiape(), dtInicial, dtFinal);
                        bDAO.closeSession();

                        ConvidadoDAO cDAO = new ConvidadoDAO();
                        List<Convidado> lConvidado = cDAO.getConvidadoFromBancaBySiapeConfirmadoInPeriodo(p.getSiape(), dtInicial, dtFinal);
                        cDAO.closeSession();
                        if (lOrientacao.size() > 0 || lConvidado.size() > 0) {
                            //chamar método para gerar relatório
                            this.gerarRelatorioParticipacaoEmBanca(p, lOrientacao, lConvidado, dtInicial, dtFinal, this.getCoordenador(), this.getCampus());
                        } else {
                            FacesContext.getCurrentInstance().addMessage("growlRelatorio",
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não há dados a serem exportados no período informado.", ""));
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "professorOrientacoes":

                if (this.isValidoOrientacoes() == true) {
                    try {
                        dtInicial = this.getDataInicial();
                        dtFinal = this.getDataFinal();
                        ProfessorDAO pDAO = new ProfessorDAO();
                        List<Professororientacao> lOrientacao = pDAO.getAllProfessorOrientacoes(dtInicial, dtFinal);
                        pDAO.closeSession();
                        if (lOrientacao.size() > 0) {
                            //chamar método para gerar relatório
                            this.gerarRelatorioProfessorOrientacao(lOrientacao, dtInicial, dtFinal, this.getCoordenador(), this.getCampus());
                        } else {
                            FacesContext.getCurrentInstance().addMessage("growlRelatorio",
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não há dados a serem exportados.", ""));
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "anualAtividades":
                if (this.isValidoDadosAtividadesAnual() == true) {
                    try {

                        //-----------------------------------------------------------
                        //retorna dados do curso
                        int idCurso = this.getCurso().getIdcurso();
                        CursoDAO cDAO = new CursoDAO();
                        this.curso = cDAO.getCursoByIdCurso(idCurso);
                        String cursoNome = this.getCurso().getNomecurso();
                        cDAO.closeSession();
                        //-----------------------------------------------------------
                        //yyyy-MM-dd
                        String dInicial = this.getAno() + "-01-01";
                        String dFinal = this.getAno() + "-12-31";
                        dtInicial = CalendarFormat.getDataBR(dInicial);
                        dtFinal = CalendarFormat.getDataBR(dFinal);
                        //-----------------------------------------------------------
                        //retornar dados do coordenador
                        int siapeCoordenador = this.getCoordenador().getSiape();
                        ProfessorDAO pDAO = new ProfessorDAO();
                        this.coordenador = pDAO.getProfessorBySiape(siapeCoordenador);
                        ArrayList<Professororientacaoseparada> proforiseparada = pDAO.getAllProfessorOrientacaoSeparada(dtInicial, dtFinal);
                        pDAO.closeSession();
                        //-----------------------------------------------------------

                        AlunoDAO aDAO = new AlunoDAO();
                        ArrayList<Sumariomatricula> sm = aDAO.getSumarioMatricula(cursoNome, dtInicial, dtFinal);
                        ArrayList<Sumarioaprovado> sa = aDAO.getSumarioAprovado(cursoNome, dtInicial, dtFinal);
                        ArrayList<Alunomatriculado> am = aDAO.getAlunoMatriculado(cursoNome, dtInicial, dtFinal);
                        ArrayList<Alunoaprovado> aa = aDAO.getAlunoAprovadosEstagioObrigatorio(cursoNome, dtInicial, dtFinal);
                        aDAO.closeSession();

                        if (sm.size() > 0) {
                            //chamar método para gerar relatório
                            this.gerarRelatorioAnualDeAtividades(proforiseparada, sm, sa, am, aa, dtInicial, dtFinal, this.getAno(), cursoNome, this.getCoordenador(), this.getCampus());
                        } else {
                            FacesContext.getCurrentInstance().addMessage("growlRelatorio",
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não há dados a serem exportados.", ""));
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            default:
                FacesContext.getCurrentInstance().addMessage("growlRelatorio",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione o relatório.", ""));
        }
    }

    /**
     * Função para saber o valor do input do curso
     *
     * @throws java.lang.Exception
     */
    public void cursoListener() throws Exception {
        if (this.getProfessor().getSiape() != null) {
            CursoDAO cDAO = new CursoDAO();
            Curso c = cDAO.getCursoByIdCurso(this.getCurso().getIdcurso());
            this.setCurso(c);
            cDAO.closeSession();
        }
    }

    /**
     * Função para saber o valor do input com o campus
     *
     * @throws java.lang.Exception
     */
    public void campusListener() throws Exception {

        if (this.getCampus().getIdcampus() != null) {
            CampusDAO cDAO = new CampusDAO();
            Campus c = cDAO.getCampusById(this.getCampus().getIdcampus());
            this.setCampus(c);
            cDAO.closeSession();
        }

    }

    /**
     * Função para saber o valor do input com o coordenador
     *
     */
    public void coordenadorListener() {
        Professor p = null;
        if (this.getCoordenador().getSiape() != null) {
            ProfessorDAO pDAO = new ProfessorDAO();
            try {
                p = pDAO.getProfessorBySiape(this.getCoordenador().getSiape());
            } catch (Exception ex) {
                Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setCoordenador(p);
            pDAO.closeSession();
        }

    }

    /**
     * Função para saber o valor do input com professor
     *
     * @throws java.lang.Exception
     */
    public void professorListener() throws Exception {

        if (this.getProfessor().getSiape() != null) {
            ProfessorDAO pDAO = new ProfessorDAO();
            Professor p = pDAO.getProfessorBySiape(this.getProfessor().getSiape());
            this.setProfessor(p);
            pDAO.closeSession();
        }

    }

    /**
     * Função para saber qual controle deve ser mostrado para gerar o relatório
     *
     * @throws java.lang.Exception
     */
    public void relatorioListener() throws Exception {

        if (this.getNomeRelatorio().equalsIgnoreCase("")) {
            this.setMostrarProfessor(false);
            this.setMostrarPeriodo(false);
            this.setMostrarCurso(false);
            this.setMostrarAno(false);
            this.setMostrarCoordenador(false);
            this.setMostrarCampus(false);
        } else {
            if (this.getNomeRelatorio().equalsIgnoreCase("participacaoEmBanca")) {
                this.setMostrarProfessor(true);
                this.setMostrarPeriodo(true);
                this.setMostrarCoordenador(true);
                this.setMostrarCampus(true);
            } else if (this.getNomeRelatorio().equalsIgnoreCase("professorOrientacoes")) {
                this.setMostrarProfessor(false);
                this.setMostrarPeriodo(true);
                this.setMostrarCoordenador(true);
                this.setMostrarCampus(true);
            } else {
                this.setMostrarProfessor(false);
                this.setMostrarPeriodo(false);
            }

            if (this.getNomeRelatorio().equalsIgnoreCase("anualAtividades")) {
                this.setMostrarCurso(true);
                this.setMostrarAno(true);
                this.setMostrarCoordenador(true);
                this.setMostrarCampus(true);
            } else {
                this.setMostrarCurso(false);
                this.setMostrarAno(false);
            }
        }
    }

    /**
     * Função para saber se mostra o ano
     *
     * @return boolean
     */
    public boolean isExibirAno() {
        return this.isMostrarAno() == true;
    }

    /**
     * Função para saber se mostra os cursos
     *
     * @return boolean
     */
    public boolean isExibirCurso() {
        return this.isMostrarCurso() == true;
    }

    /**
     * Função para saber se mostra os coordenadores
     *
     * @return boolean
     */
    public boolean isExibirCoordenador() {
        return this.isMostrarCoordenador() == true;
    }

    /**
     * Função para saber se mostra os campi
     *
     * @return boolean
     */
    public boolean isExibirCampus() {
        return this.isMostrarCampus() == true;
    }

    /**
     * Função para saber se mostra os professores
     *
     * @return boolean
     */
    public boolean isExibirProfessor() {
        return this.isMostrarProfessor() == true;
    }

    /**
     * Função para saber se mostra as datas do periodo
     *
     * @return boolean
     */
    public boolean isExibirPeriodo() {
        return this.isMostrarPeriodo() == true;
    }
}
