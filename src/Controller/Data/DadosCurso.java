/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Curso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class DadosCurso implements Serializable {

    //lista que forma a tabela com todos alunos
    @SuppressWarnings("unchecked")
    protected List<Curso> listaCursos = new ArrayList();
    protected List<Curso> fitroCursos;

    public DadosCurso() {
    }

    public List<Curso> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List<Curso> getFitroCursos() {
        return fitroCursos;
    }

    public void setFitroCursos(List<Curso> fitroCursos) {
        this.fitroCursos = fitroCursos;
    }

}
