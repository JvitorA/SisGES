/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Aluno;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class DadosAluno implements Serializable {

    //lista que forma a tabela com todos alunos
    @SuppressWarnings("unchecked")
    protected List<Aluno> listaAlunos = new ArrayList();
    protected List<Aluno> filtroAlunos;

    public DadosAluno() {
    }

    public List<Aluno> getListaAlunos() {
        return listaAlunos;
    }

    public void setListaAlunos(List<Aluno> listaAlunos) {
        this.listaAlunos = listaAlunos;
    }

    public List<Aluno> getFiltroAlunos() {
        return filtroAlunos;
    }

    public void setFiltroAlunos(List<Aluno> filtroAlunos) {
        this.filtroAlunos = filtroAlunos;
    }
}
