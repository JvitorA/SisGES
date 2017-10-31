/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "SistemaBean")
@SessionScoped
public final class SistemaController implements Serializable {

    public String template;

    public SistemaController() {
        this.template = this.getTemplate();
    }

    /**
     * Função para mudar template de acordo com o perfil
     *
     * @return
     */
    public String getTemplate() {
        SistemaSession instance = SistemaSession.getInstance();

        if (instance.getTemplateAtual() != null) {
            return instance.getTemplateAtual();
        }
        return "./../templates/template.xhtml";
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
