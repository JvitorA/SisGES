/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Controller.SistemaSession;
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
import Report.RelatorioAluno;
import Report.RelatorioBanca;
import Report.RelatorioCurso;
import Report.RelatorioProfessor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Alessandro
 */
public class DadosRelatorio implements Serializable {

    protected String nomeRelatorio;
    protected String ano;
    protected Date dataInicial;
    protected Date dataFinal;
    protected boolean mostrarCoordenador;
    protected boolean mostrarCampus;
    protected boolean mostrarProfessor;
    protected boolean mostrarPeriodo;
    protected boolean mostrarCurso;
    protected boolean mostrarAno;
    protected Professor professor;
    protected Professor coordenador;
    protected Curso curso;
    protected Campus campus;
    protected SistemaSession sessao;
    protected RelatorioBanca relBanca;
    protected RelatorioAluno relAluno;
    protected RelatorioProfessor relProfesssor;
    protected RelatorioCurso relCurso;

    public DadosRelatorio() {
        this.sessao = SistemaSession.getInstance();
        this.relBanca = new RelatorioBanca();
        this.relAluno = new RelatorioAluno();
        this.relProfesssor = new RelatorioProfessor();
        this.relCurso = new RelatorioCurso();
        this.professor = new Professor();
        this.coordenador = new Professor();
        this.campus = new Campus();
        this.curso = new Curso();
        this.nomeRelatorio = "";
        this.mostrarCoordenador = false;
        this.mostrarProfessor = false;
        this.mostrarPeriodo = false;
        this.mostrarCurso = false;
        this.mostrarCampus = false;
        this.dataInicial = null;
        this.dataFinal = null;
    }

    /**
     * Função para gerar ata de defesa
     *
     * @param banca
     * @param listaConvidados
     * @param coordenador
     * @param campus
     */
    public void gerarRelatorioAtaDeDefesa(Banca banca, List<Convidado> listaConvidados, Professor coordenador, Campus campus) {

        RelatorioBanca rb = new RelatorioBanca();
        ByteArrayOutputStream baos = rb.relatorioAtaDeDefesa(banca, listaConvidados, coordenador, campus);

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        if (baos.size() > 0) {
            response.setContentLength(baos.size());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + "AtaDeDefesa.pdf" + "\"");
            try (ServletOutputStream out = response.getOutputStream()) {
                baos.writeTo(out);
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
            }
            faces.responseComplete();
        }
    }

    /**
     * Função para gerar relatório com todos alunos chamada na view
     *
     * @throws Exception
     */
    public void gerarRelatorioAluno() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            //instacia da classe do relatório
            baos = relAluno.relatorioAlunosMatriculados();
        } catch (Exception ex) {
            Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (baos.size() > 0) {
            response.setContentLength(baos.size());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + "RelatorioAluno.pdf" + "\"");
            try (ServletOutputStream out = response.getOutputStream()) {
                baos.writeTo(out);
                out.flush();
            }
            faces.addMessage("growlVisualzarAluno",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exportado com sucesso!", ""));
            faces.responseComplete();
        } else {
            faces.addMessage("growlVisualzarAluno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar arquivo PDF. Informe ao desenvolvedor.", ""));
        }
    }

    /**
     * Função para gerar R1 - Visualizar as bancas/orientações,de um professor
     * (Nome, matrícula do aluno, título, nome, papel do professor e data de
     * defesa) em um período
     *
     * @param professor
     * @param lOrientacao
     * @param lConvidado
     * @param dtinicial
     * @param dtfinal
     * @param coordenador
     * @param campus
     * @throws Exception
     */
    public void gerarRelatorioParticipacaoEmBanca(Professor professor, List<Banca> lOrientacao, List<Convidado> lConvidado, Date dtinicial, Date dtfinal, Professor coordenador, Campus campus) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            //instacia da classe do relatório
            baos = relBanca.relatorioParticipacaoEmBanca(professor, lOrientacao, lConvidado, dtinicial, dtfinal, coordenador, campus);
        } catch (Exception ex) {
            Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (baos.size() > 0) {
            response.setContentLength(baos.size());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + "RelatorioParticipacoesEmBanca.pdf" + "\"");
            try (ServletOutputStream out = response.getOutputStream()) {
                baos.writeTo(out);
                out.flush();
            }
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exportado com sucesso!", ""));
            faces.responseComplete();
        } else {
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar arquivo PDF. Informe ao desenvolvedor.", ""));
        }
    }

    /**
     * Função para gerar R2 - Visualizar os professores e número de orientados
     * atuais e passados;
     *
     * @param lOrientacao
     * @param dtinicial
     * @param dtfinal
     * @param coordenador
     * @param campus
     * @throws java.lang.Exception
     */
    public void gerarRelatorioProfessorOrientacao(List<Professororientacao> lOrientacao, Date dtinicial, Date dtfinal, Professor coordenador, Campus campus) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            //instacia da classe do relatório
            baos = relProfesssor.relatorioProfessorOrientacao(lOrientacao, dtinicial, dtfinal, coordenador, campus);
        } catch (Exception ex) {
            Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (baos.size() > 0) {
            response.setContentLength(baos.size());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + "RelatorioProfessorOrientações.pdf" + "\"");
            try (ServletOutputStream out = response.getOutputStream()) {
                baos.writeTo(out);
                out.flush();
            }
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exportado com sucesso!", ""));
            faces.responseComplete();
        } else {
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar arquivo PDF. Informe ao desenvolvedor.", ""));
        }
    }

    /**
     * Função para gerar R2 - Visualizar os professores e número de orientados
     * atuais e passados;
     *
     * @param proforiseparada
     * @param sm
     * @param sa
     * @param am
     * @param aa
     * @param dtinicial
     * @param dtfinal
     * @param ano
     * @param cursoNome
     * @param coordenador
     * @param campus
     */
    public void gerarRelatorioAnualDeAtividades(ArrayList<Professororientacaoseparada> proforiseparada, ArrayList<Sumariomatricula> sm, ArrayList<Sumarioaprovado> sa, ArrayList<Alunomatriculado> am, ArrayList<Alunoaprovado> aa, Date dtinicial, Date dtfinal, String ano, String cursoNome, Professor coordenador, Campus campus) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            //instacia da classe do relatório
            baos = relCurso.relatorioAnualDeAtividades(proforiseparada, sm, sa, am, aa, dtinicial, dtfinal, ano, cursoNome, coordenador, campus);
        } catch (Exception ex) {
            Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (baos.size() > 0) {
            response.setContentLength(baos.size());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + "RelatorioAnual.pdf" + "\"");
            try (ServletOutputStream out = response.getOutputStream()) {
                baos.writeTo(out);
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
            }
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exportado com sucesso!", ""));
            faces.responseComplete();
        } else {
            faces.addMessage("growlRelatorio",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar arquivo PDF. Informe ao desenvolvedor.", ""));
        }
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public boolean isMostrarProfessor() {
        return mostrarProfessor;
    }

    public boolean isMostrarCoordenador() {
        return mostrarCoordenador;
    }

    public boolean isMostrarCampus() {
        return mostrarCampus;
    }

    public void setMostrarCoordenador(boolean mostrarCoordenador) {
        this.mostrarCoordenador = mostrarCoordenador;
    }

    public void setMostrarProfessor(boolean mostrarProfessor) {
        this.mostrarProfessor = mostrarProfessor;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public boolean isMostrarPeriodo() {
        return mostrarPeriodo;
    }

    public void setMostrarPeriodo(boolean mostrarPeriodo) {
        this.mostrarPeriodo = mostrarPeriodo;
    }

    public boolean isMostrarCurso() {
        return mostrarCurso;
    }

    public void setMostrarCurso(boolean mostrarCurso) {
        this.mostrarCurso = mostrarCurso;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public boolean isMostrarAno() {
        return mostrarAno;
    }

    public void setMostrarAno(boolean mostrarAno) {
        this.mostrarAno = mostrarAno;
    }

    public Professor getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Professor coordenador) {
        this.coordenador = coordenador;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public void setMostrarCampus(boolean mostrarCampus) {
        this.mostrarCampus = mostrarCampus;
    }

}
