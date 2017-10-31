/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Dao.AlunoDAO;
import Model.Aluno;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
public class RelatorioAluno {

    String realPath;

    /**
     * funcao para gerar o stream do relatorio
     *
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream relatorioAlunosMatriculados() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        // pega o contexto da aplicacao
        realPath = faces.getExternalContext().getRealPath("/");
        //realPath = "C:/Users/Alessandro/Desktop/TCC2/SisGES/build/web";

        try {
            BaseFont fHelvetica = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);

            //----------------------------------------------------------------------
            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            Document document = new Document(PageSize.A4, 40, 40, 20, 50);
            document.addAuthor("SisGES"); // optional
            document.addSubject("Relatório"); // opcional
            document.addKeywords("SisGES");
            document.addCreator("iText");
            //----------------------------------------------------------------------
            // creation of the different writers
            //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("web/resources/report/alunosMatriculados.pdf"));
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setBoxSize("header", new Rectangle(36, 54, 559, 788));
            //----------------------------------------------------------------------
            // ADICIONA HEADER E FOOTER
            //----------------------------------------------------------------------
            HeaderFooter headerFooter = new HeaderFooter();
            writer.setPageEvent(headerFooter);
            //----------------------------------------------------------------------
            // ABRE DOCUMENTO PARA ESCRITA
            //----------------------------------------------------------------------
            document.open();
            //----------------------------------------------------------------------
            //ADICIONAR LOGO NO DOCUMENTO
            //----------------------------------------------------------------------
            Image logoUfu = Image.getInstance(realPath + "/resources/images/logoUFU.png");
            logoUfu.scaleAbsolute(57, 56);//(largura,altura)
            logoUfu.isImgTemplate(); //add no template
            //logoUfu.setAbsolutePosition(30, 745); //x ,y por referencia do rodape
            document.add(logoUfu);

            Image logoFacom = Image.getInstance(realPath + "/resources/images/logoFacom.png");
            logoFacom.scaleAbsolute(62, 55);//(largura,altura)
            logoFacom.setAbsolutePosition(92, 767); //x ,y por referencia do rodape
            //logoFacom.isImgTemplate(); //add no template            
            document.add(logoFacom);

            //----------------------------------------------------------------------
            //ADICIONAR CABEÇALHO
            //----------------------------------------------------------------------
            PdfContentByte univ = writer.getDirectContentUnder();
            univ.beginText();
            univ.setFontAndSize(fHelvetica, 10);
            univ.setTextMatrix(349, 805); // x e y
            univ.showText("UNIVERSIDADE FEDERAL DE UBERLÂNDIA");
            univ.setTextMatrix(405, 790); // x e y
            univ.showText("FACULDADE DE COMPUTAÇÃO");
            univ.setTextMatrix(403, 775); // x e y
            univ.showText("COORDENADORIA DE ESTÁGIO ");
            univ.endText();

            PdfContentByte univEnd = writer.getDirectContentUnder();
            univEnd.beginText();
            univEnd.setFontAndSize(fHelvetica, 7);
            univEnd.setTextMatrix(326, 761); // x e y
            univEnd.showText("Campus Universitário - Santa Mônica - CEP 38408-100 - Uberlândia - MG");
            univEnd.setTextMatrix(432, 750); // x e y
            univEnd.showText("Telefone: (34) 3239-4144 ou 3239-4393");
            univEnd.endText();

            //----------------------------------------------------------------------
            //ADICIONAR TITULO
            //----------------------------------------------------------------------
            PdfContentByte titulo = writer.getDirectContentUnder();
            titulo.beginText();
            titulo.setFontAndSize(fHelvetica, 16);
            titulo.setTextMatrix(210, 700); // x e y
            titulo.showText("Alunos Matrículados");
            titulo.endText();
            //----------------------------------------------------------------------

            //----------------------------------------------------------------------
            AlunoDAO aDAO = new AlunoDAO();
            List<Aluno> allAlunos = aDAO.getAllAlunos();
            aDAO.closeSession();

            PdfPTable table = new PdfPTable(5);

            table.setTotalWidth(100f);
            table.setWidthPercentage(100);
            float[] widths = {10, 30, 30, 13, 17};//largura das colunas
            table.setWidths(widths);
            table.setHeaderRows(1);

            Paragraph cabecalho = new Paragraph("Matricula");
            PdfPCell cellMatricula = new PdfPCell(cabecalho); // celula
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            cellMatricula.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellMatricula.setBorderColor(BaseColor.LIGHT_GRAY);
            cellMatricula.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellMatricula);

            cabecalho = new Paragraph("Nome");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cellNome = new PdfPCell(cabecalho); // celula
            cellNome.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellNome.setBorderColor(BaseColor.LIGHT_GRAY);
            cellNome.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellNome);

            cabecalho = new Paragraph("Email");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cellEmail = new PdfPCell(cabecalho); // celula
            cellEmail.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
            cellEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEmail);

            cabecalho = new Paragraph("Telefone");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cellTelefone = new PdfPCell(cabecalho); // celula
            cellTelefone.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellTelefone.setBorderColor(BaseColor.LIGHT_GRAY);
            cellTelefone.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellTelefone);

            cabecalho = new Paragraph("Curso");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cellCurso = new PdfPCell(cabecalho); // celula
            cellCurso.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellCurso.setBorderColor(BaseColor.LIGHT_GRAY);
            cellCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellCurso);

            for (int i = 0; i < allAlunos.size(); i++) {
                Aluno aluno = allAlunos.get(i);
                Paragraph texto = new Paragraph(aluno.getMatricula());
                cellMatricula = new PdfPCell(texto); // celula
                cellMatricula.setBorderColor(BaseColor.LIGHT_GRAY);
                texto.getFont().setSize(8);
                table.addCell(cellMatricula);
                texto = new Paragraph(aluno.getNome());
                texto.getFont().setSize(8);
                cellNome = new PdfPCell(texto); // celula
                cellNome.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cellNome);
                texto = new Paragraph(aluno.getEmail());
                texto.getFont().setSize(8);
                cellEmail = new PdfPCell(texto); // celula
                cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cellEmail);
                texto = new Paragraph(aluno.getTelefone());
                texto.getFont().setSize(8);
                cellTelefone = new PdfPCell(texto); // celula
                cellTelefone.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cellTelefone);
                texto = new Paragraph(aluno.getCursoidcurso().getNomecurso());
                texto.getFont().setSize(8);
                cellCurso = new PdfPCell(texto); // celula
                cellCurso.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cellCurso);

            }
            table.setSpacingBefore(100);
            table.setSpacingAfter(10);
            table.completeRow();

            document.add(table);
            //add nova pagina
            document.newPage();
            //close document
            document.close();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(RelatorioAluno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RelatorioAluno.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return stream
        return baos;
    }
}
