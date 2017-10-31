/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package Controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "FormatarBean")
@ViewScoped
public class FormatViewController implements Serializable {

    public FormatViewController() {

    }

    /**
     * função para converte string "yyyy-MM-dd" para "dd/MM/yyyy"
     *
     * @param hora
     * @return String
     */
    public String parseDataBR(String hora) {
        Date horario;
        String format = "";
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            horario = (Date) formato.parse(hora);
            DateFormat writeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format = writeFormat.format(horario);

        } catch (ParseException e) {
            e.getMessage();
        }
        return format;
    }
}
