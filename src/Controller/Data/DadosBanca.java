/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Dao.TrabalhoDAO;
import Model.Convidado;
import Model.EmailConfig;
import Model.Trabalho;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alessandro
 */
public class DadosBanca implements Serializable {

    protected String tituloBanca = "";
    @SuppressWarnings("unchecked")
    protected List<Convidado> listaConvidados = new ArrayList();
    protected List<Convidado> filtroConvidados;
    @SuppressWarnings("unchecked")
    protected List<Convidado> listaBancaPresente = new ArrayList();
    protected List<Convidado> filtroBancaPresente;

    EmailConfig emailConfig = new EmailConfig();
    String destinatarios = "";
    String mensagem = "";
    String titulo = "";

    public DadosBanca() {

    }

    /**
     * função para retornar todos os trabalhos em andamento que possuem defesa
     * que ainda não foi cadastrado banca
     *
     * @return List
     * @throws java.lang.Exception
     */
    public List<SelectItem> getListaTrabalho() throws Exception {
        TrabalhoDAO tDAO = new TrabalhoDAO();
        List<Trabalho> allTrabalho = tDAO.getAllTrabalhosHaveDefesa();
        List<SelectItem> itens = new ArrayList<>(allTrabalho.size());

        for (Trabalho e : allTrabalho) {
            //value,label
            SelectItem s = new SelectItem(e.getIdtrabalho(), e.toString());
            itens.add(s);
        }
        tDAO.closeSession();
        return itens;
    }

    public String getTituloBanca() {
        return tituloBanca;
    }

    public void setTituloBanca(String tituloBanca) {
        this.tituloBanca = tituloBanca;
    }

    public List<Convidado> getListaConvidados() {
        return listaConvidados;
    }

    public void setListaConvidados(List<Convidado> listaConvidados) {
        this.listaConvidados = listaConvidados;
    }

    public List<Convidado> getFiltroConvidados() {
        return filtroConvidados;
    }

    public void setFiltroConvidados(List<Convidado> filtroConvidados) {
        this.filtroConvidados = filtroConvidados;
    }

    public List<Convidado> getListaBancaPresente() {
        return listaBancaPresente;
    }

    public void setListaBancaPresente(List<Convidado> listaBancaPresente) {
        this.listaBancaPresente = listaBancaPresente;
    }

    public List<Convidado> getFiltroBancaPresente() {
        return filtroBancaPresente;
    }

    public void setFiltroBancaPresente(List<Convidado> filtroBancaPresente) {
        this.filtroBancaPresente = filtroBancaPresente;
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
