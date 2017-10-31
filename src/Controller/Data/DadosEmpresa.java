/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Empresa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class DadosEmpresa implements Serializable {

    //lista que forma a tabela com todos alunos
    @SuppressWarnings("unchecked")
    protected List<Empresa> listaEmpresas = new ArrayList();
    protected List<Empresa> filtroEmpresas;

    public DadosEmpresa() {
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresa> getFiltroEmpresas() {
        return filtroEmpresas;
    }

    public void setFiltroEmpresas(List<Empresa> filtroEmpresas) {
        this.filtroEmpresas = filtroEmpresas;
    }

}
