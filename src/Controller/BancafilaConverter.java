/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.BancaFilaDAO;
import Model.Bancafila;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Alessandro
 */
@FacesConverter(value = "filaConverter", forClass = Bancafila.class)
public class BancafilaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        BancaFilaDAO bfDAO = new BancaFilaDAO();
        Bancafila banca = null;
        if ((value != null) && (!value.equals(""))) {
            try {
                banca = bfDAO.getFilaBancaBySiape(Integer.valueOf(value));
            } catch (Exception ex) {
                Logger.getLogger(BancafilaConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        bfDAO.closeSession();
        return banca;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Integer retorno = null;
        if (!(value == null)) {
            Bancafila fila;
            fila = (Bancafila) value;
            retorno = fila.getSiape();
        }
        return String.valueOf(retorno);
    }
}
