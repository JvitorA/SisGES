/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Assunto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class DadosAssunto implements Serializable {

    //lista que forma a tabela com todos alunos
    @SuppressWarnings("unchecked")
    protected List<Assunto> listaAssunto = new ArrayList();
    protected List<Assunto> filtroAssunto;

    public DadosAssunto() {
    }

    public List<Assunto> getListaAssuntos() {
        return listaAssunto;
    }

    public void setListaAssuntos(List<Assunto> listaAssunto) {
        this.listaAssunto = listaAssunto;
    }

    public List<Assunto> getFiltroAssuntos() {
        return filtroAssunto;
    }

    public void setFiltroAssuntos(List<Assunto> filtroAssunto) {
        this.filtroAssunto = filtroAssunto;
    }

}
