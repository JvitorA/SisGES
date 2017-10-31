/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Professordisponibilidade;
import Model.Professor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class DadosProfessor implements Serializable {

    protected List<Professor> listaProfessores;
    protected List<Professor> filtroProfessores;
    protected List<Professordisponibilidade> listaDisponibilidade;
    protected String assinatura;

    public DadosProfessor() {
        this.listaProfessores = new ArrayList<>();
        this.initDisponibilidade();
    }

    @SuppressWarnings("unchecked")
    public final List<Professordisponibilidade> initDisponibilidade() {
        this.listaDisponibilidade = new ArrayList();
        Professordisponibilidade disp1 = new Professordisponibilidade();
        disp1.setDiasemana("Segunda");
        disp1.setManha(false);
        disp1.setTarde(false);
        disp1.setNoite(false);
        listaDisponibilidade.add(disp1);
        Professordisponibilidade disp2 = new Professordisponibilidade();
        disp2.setDiasemana("Terça");
        disp2.setManha(false);
        disp2.setTarde(false);
        disp2.setNoite(false);
        listaDisponibilidade.add(disp2);
        Professordisponibilidade disp3 = new Professordisponibilidade();
        disp3.setDiasemana("Quarta");
        disp3.setManha(false);
        disp3.setTarde(false);
        disp3.setNoite(false);
        listaDisponibilidade.add(disp3);
        Professordisponibilidade disp4 = new Professordisponibilidade();
        disp4.setDiasemana("Quinta");
        disp4.setManha(false);
        disp4.setTarde(false);
        disp4.setNoite(false);
        listaDisponibilidade.add(disp4);
        Professordisponibilidade disp5 = new Professordisponibilidade();
        disp5.setDiasemana("Sexta");
        disp5.setManha(false);
        disp5.setTarde(false);
        disp5.setNoite(false);
        listaDisponibilidade.add(disp5);
        return this.listaDisponibilidade;
    }

    public List<Professor> getListaProfessores() {
        return listaProfessores;
    }

    public void setListaProfessores(List<Professor> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public List<Professor> getFiltroProfessores() {
        return filtroProfessores;
    }

    public void setFiltroProfessores(List<Professor> filtroProfessores) {
        this.filtroProfessores = filtroProfessores;
    }

    public List<Professordisponibilidade> getListaDisponibilidade() {
        return listaDisponibilidade;
    }

    public void setListaDisponibilidade(List<Professordisponibilidade> listaDisponibilidade) {
        this.listaDisponibilidade = listaDisponibilidade;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

}
