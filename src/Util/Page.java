/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.Serializable;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
public class Page implements Serializable {

    public void refresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    /*
     * função para aguardar 1 minuto
     */
    public void wait1Min() {

        long start = System.currentTimeMillis();
        //1 minuto = 60000 milessegundo
        long finish = start + 60000;
        while (start < finish) {
            start = System.currentTimeMillis();
        }
    }

}
