/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Controller.Data.DadosRelatorio;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Alessandro
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        RelatorioBanca relBanca = new RelatorioBanca();
        RelatorioAluno relAluno = new RelatorioAluno();
        RelatorioProfessor relProfesssor = new RelatorioProfessor();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //instacia da classe do relatório
            baos = relAluno.relatorioAlunosMatriculados();
        } catch (Exception ex) {
            Logger.getLogger(DadosRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
