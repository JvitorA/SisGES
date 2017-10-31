/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Data;

import Model.Campus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.html.HtmlDataTable;

/**
 *
 * @author Alessandro
 */
public class DadosCampus implements Serializable {

    //representa a tabela com todos campus
    protected HtmlDataTable dataTableTodosCampus;
    //lista que forma a tabela com todos campus
    @SuppressWarnings("unchecked")
    protected List<Campus> listaCampus = new ArrayList();
    protected List<Campus> filtroCampus;

    public DadosCampus() {
    }

    public HtmlDataTable getDataTableTodosCampus() {
        return dataTableTodosCampus;
    }

    public void setDataTableTodosCampus(HtmlDataTable dataTableTodosCampus) {
        this.dataTableTodosCampus = dataTableTodosCampus;
    }

    public List<Campus> getListaCampus() {
        return listaCampus;
    }

    public void setListaCampus(List<Campus> listaCampus) {
        this.listaCampus = listaCampus;
    }

    public List<Campus> getFiltroCampus() {
        return filtroCampus;
    }

    public void setFiltroCampus(List<Campus> filtroCampus) {
        this.filtroCampus = filtroCampus;
    }

}
