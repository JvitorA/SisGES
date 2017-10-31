/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Model.Campus;
import Model.Professor;
import Model.Professororientacao;
import Util.CalendarFormat;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
public class RelatorioProfessor {

    /**
     * Função para gerar o stream do relatorio de orientações por professor
     *
     * @param lOrientacao
     * @param dtinicial
     * @param dtfinal
     * @param coordenador
     * @param campus
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream relatorioProfessorOrientacao(List<Professororientacao> lOrientacao, Date dtinicial, Date dtfinal, Professor coordenador, Campus campus) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        //pega o contexto da aplicacao
        String realPath = faces.getExternalContext().getRealPath("/");

        try {
            BaseFont fHelvetica = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
            //----------------------------------------------------------------------
            //creation of the document with a certain size and certain margins
            //may want to use PageSize.LETTER instead
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
            headerFooter.setY(760f);
            writer.setPageEvent(headerFooter);
            //----------------------------------------------------------------------
            //ABRE DOCUMENTO PARA ESCRITA
            //----------------------------------------------------------------------
            document.open();
            //----------------------------------------------------------------------
            //ADICIONAR LOGO NO DOCUMENTO
            //----------------------------------------------------------------------
            Image logoUfu = Image.getInstance(realPath + "/resources/images/logoUFUAta.png");
            logoUfu.scaleAbsolute(155, 39);//(largura,altura)
            logoUfu.isImgTemplate(); //add no template
            document.add(logoUfu);

            //----------------------------------------------------------------------
            //ADICIONAR CABEÇALHO
            //----------------------------------------------------------------------
            PdfContentByte univ = writer.getDirectContentUnder();
            univ.beginText();
            univ.setFontAndSize(fHelvetica, 10);
            univ.setTextMatrix(349, 816); // x e y
            univ.showText("UNIVERSIDADE FEDERAL DE UBERLÂNDIA");
            univ.setTextMatrix(405, 802); // x e y
            univ.showText("FACULDADE DE COMPUTAÇÃO");
            univ.setTextMatrix(403, 788); // x e y
            univ.showText("COORDENADORIA DE ESTÁGIO ");
            univ.endText();

            String info = "Campus Universitário - " + campus.getNomecampus() + " - CEP " + campus.getCep() + " - " + campus.getCidade() + " - " + campus.getEstadosigla().toUpperCase();

            Chunk txtCampus = new Chunk(info);
            Paragraph paragraph0 = new Paragraph();
            paragraph0.setSpacingBefore(-10);
            paragraph0.setFont(new Font(fHelvetica, 7));
            paragraph0.add(txtCampus);
            paragraph0.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph0);

            info = "Telefone: " + coordenador.getTelefone() + "   Email: " + coordenador.getEmail();
            Chunk txtCoordenador = new Chunk(info);
            Paragraph paragraph1 = new Paragraph();
            paragraph1.setSpacingBefore(-5);
            paragraph1.setSpacingAfter(10);
            paragraph1.setFont(new Font(fHelvetica, 7));
            paragraph1.add(txtCoordenador);
            paragraph1.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph1);

//            PdfContentByte univ = writer.getDirectContentUnder();
//            univ.beginText();
//            univ.setFontAndSize(fHelvetica, 12);
//            univ.setTextMatrix(200, 815); // x e y
//            univ.showText("UNIVERSIDADE FEDERAL DE UBERLÂNDIA");
//            univ.setTextMatrix(235, 800); // x e y
//            univ.showText("FACULDADE DE COMPUTAÇÃO");
//            univ.endText();
//
//            PdfContentByte univEnd = writer.getDirectContentUnder();
//            univEnd.beginText();
//            univEnd.setFontAndSize(fHelvetica, 7);
//            univEnd.setTextMatrix(210, 785); // x e y
//            univEnd.showText("Campus Universitário - Santa Mônica - CEP 38408-100 - Uberlândia - MG");
//            univEnd.setTextMatrix(260, 775); // x e y
//            univEnd.showText("Telefone: (34)3239-4144 ou 3239-4393");
//            univEnd.endText();
            //----------------------------------------------------------------------
            //ADICIONAR TITULO
            //----------------------------------------------------------------------
            PdfContentByte t1 = writer.getDirectContentUnder();
            t1.beginText();
            t1.setFontAndSize(fHelvetica, 14);
            t1.setTextMatrix(180, 730); // x e y
            t1.showText("Participação dos professores FACOM");
            t1.endText();

            PdfContentByte t2 = writer.getDirectContentUnder();
            t2.beginText();
            t2.setFontAndSize(fHelvetica, 12);
            t2.setTextMatrix(195, 705); // x e y
            String periodo = CalendarFormat.getDataBRtoDate(dtinicial) + " até " + CalendarFormat.getDataBRtoDate(dtfinal);
            t2.showText("Período: " + periodo);
            t2.endText();

            //----------------------------------------------------------------------
            //TABELA
            PdfPTable table = new PdfPTable(4);
            //espaço do inicio da pagina
            table.setSpacingBefore(100f);
            table.setTotalWidth(100f);
            table.setWidthPercentage(100);
            float[] widths = {20, 30, 35, 15};//largura das colunas
            table.setWidths(widths);
            table.setHeaderRows(1);

            //CABEÇALHO DA TABELA
            Paragraph cabecalho = new Paragraph("Siape");
            PdfPCell cell1 = new PdfPCell(cabecalho); // celula
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            cabecalho = new Paragraph("Professor");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell2 = new PdfPCell(cabecalho); // celula
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setBorderColor(BaseColor.LIGHT_GRAY);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            cabecalho = new Paragraph("Email do Professor");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell4 = new PdfPCell(cabecalho); // celula
            cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell4.setBorderColor(BaseColor.LIGHT_GRAY);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell4);

            cabecalho = new Paragraph("Orientações");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell3 = new PdfPCell(cabecalho); // celula
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setBorderColor(BaseColor.LIGHT_GRAY);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);

            for (int i = 0; i < lOrientacao.size(); i++) {
                Professororientacao po = lOrientacao.get(i);

                Paragraph texto = new Paragraph(String.valueOf(po.getSiape()));
                texto.getFont().setSize(8);
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                texto = new Paragraph(po.getNome());
                texto.getFont().setSize(8);
                cell2 = new PdfPCell(texto); // celula
                cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell2);

                texto = new Paragraph(po.getEmail());
                texto.getFont().setSize(8);
                cell4 = new PdfPCell(texto); // celula
                cell4.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell4);

                texto = new Paragraph(String.valueOf(po.getOrientacoes()));
                texto.getFont().setSize(8);
                cell3 = new PdfPCell(texto); // celula
                cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell3);

                switch (i % 2) {
                    case 0:
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        break;
                    case 1:

                        break;
                }
            }

            //add a tabela
            document.add(table);
            //-----------------------------------------------------
            // distancia do fim da pagina
            float y = 70f;
            float x = 160f;
            //-----------------------------------------------------
            //linha
            PdfContentByte linha = writer.getDirectContentUnder();
            linha.setLineWidth(1f); // mostrar linha
            linha.setGrayStroke(0.5f); // 0 = preto, 1 = branco
            linha.moveTo(x, y);
            linha.lineTo(450f, y); // ate onde a linha vai
            linha.stroke();
            //------------------------------------------------------
            //assinatura
            PdfContentByte a1 = writer.getDirectContentUnder();
            a1.beginText();
            a1.setFontAndSize(fHelvetica, 10);
            a1.setTextMatrix(x + 50, y - 15); // x e y
            a1.showText("Coordenação de Estágio Supervisionado");
            a1.endText();
            //------------------------------------------------------
            //assinatura
            PdfContentByte a2 = writer.getDirectContentUnder();
            a2.beginText();
            a2.setFontAndSize(fHelvetica, 10);
            a2.setTextMatrix(x + 50, y - 27); // x e y
            a2.showText("FACOM/UFU");
            a2.endText();
            //------------------------------------------------------

            //add nova pagina
            document.newPage();
            //close document
            document.close();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(RelatorioProfessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return stream com dados
        return baos;
    }
}
