/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Model.Banca;
import Model.Campus;
import Model.Convidado;
import Model.Professor;
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
public class RelatorioBanca {

    /**
     * funcao para gerar o stream do relatorio
     *
     * @param banca
     * @param listaConvidados
     * @param coordenador
     * @param campus
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream relatorioAtaDeDefesa(Banca banca, List<Convidado> listaConvidados, Professor coordenador, Campus campus) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        //pega o contexto da aplicacao
        String realPath = faces.getExternalContext().getRealPath("/");

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
            headerFooter.setY(760f);
            writer.setPageEvent(headerFooter);
            //----------------------------------------------------------------------
            // ABRE DOCUMENTO PARA ESCRITA
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
//            univEnd.showText("Telefone: (34) 3239-4144 ou 3239-4393");
//            univEnd.endText();

            //----------------------------------------------------------------------
            //ADICIONAR TITULO
            //----------------------------------------------------------------------
            PdfContentByte curso = writer.getDirectContentUnder();
            curso.beginText();
            curso.setFontAndSize(fHelvetica, 14);
            curso.setTextMatrix(140, 720); // x e y
            String cursoAluno;
            //curso.showText("Curso de Bacharelado em Ciência da Computação");
            cursoAluno = banca.getTrabalhoidtrabalho().getAlunomatricula().getCursoidcurso().getNomecurso();
            if (cursoAluno.contains("sistema")) {
                cursoAluno = "Bacharelado em Sistemas de Informação";
            } else {
                cursoAluno = "Bacharelado em Ciência da Computação";
            }

            curso.showText("Curso de " + cursoAluno);
            curso.endText();

            PdfContentByte titulo = writer.getDirectContentUnder();
            titulo.beginText();
            titulo.setFontAndSize(fHelvetica, 12);
            titulo.setTextMatrix(160, 660); // x e y
            titulo.showText("Ata de Defesa de Trabalho de Estágio Supervisionado");
            titulo.endText();
            //----------------------------------------------------------------------]
            String texto = "Ata da ___________ de defesa de Trabalho de Estágio Supervisionado, do Curso de Bacharelado "
                    + "em Ciência da Computação, realizada em " + CalendarFormat.getDataBRtoDate(banca.getDatabanca()) + ", na " + banca.getLocalbanca()
                    + ", às " + CalendarFormat.parseDateToTimeString(banca.getHorario()) + " horas, pelo aluno " + banca.getTrabalhoidtrabalho().getAlunomatricula().getNome()
                    + " (" + banca.getTrabalhoidtrabalho().getAlunomatricula().getMatricula() + "). O trabalho "
                    + "intitulado \"" + banca.getTrabalhoidtrabalho().getTitulo() + "\" foi apresentado pelo aluno em sessão pública "
                    + "com cerca de 1 hora de duração. Na ocasião, o aluno foi arguido oralmente pelos membros "
                    + "da banca, sendo considerado ________________________ (aprovado/reprovado).";

            Chunk chunk1 = new Chunk(texto);
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(new Font(fHelvetica, 12));
            paragraph.setSpacingBefore(170);
            paragraph.add(chunk1);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph);

            document.add(Chunk.NEWLINE);
            texto = "Carga horária total do estágio: ____________ horas (preenchido pela coordenação de estágios)";
            Paragraph paragraph2 = new Paragraph();
            paragraph2.setSpacingBefore(15);
            paragraph2.setFont(new Font(fHelvetica, 10));
            paragraph2.add(texto);
            paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph2);
            document.add(Chunk.NEWLINE);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.setSpacingBefore(25);
            paragraph3.add("Uberlândia, " + CalendarFormat.getDataPorExtenso(banca.getDatabanca()) + ".");
            paragraph3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph3);
            document.add(Chunk.NEWLINE);
            //----------------------------------------------------------------------
            PdfContentByte tituloMembros = writer.getDirectContentUnder();
            tituloMembros.beginText();
            tituloMembros.setFontAndSize(fHelvetica, 12);
            tituloMembros.setTextMatrix(220, 350); // x e y
            tituloMembros.showText("Membros da Banca Examinadora");
            tituloMembros.endText();
            //----------------------------------------------------------------------
            float x = 160f;
            float y = 300f; // distancia do fim da pagina
            //retorna orientador
            //-----------------------------------------------------
            //linha
            PdfContentByte linhaOrientador = writer.getDirectContentUnder();
            linhaOrientador.setLineWidth(1f); // mostrar linha
            linhaOrientador.setGrayStroke(0.5f); // 0 = preto, 1 = branco
            linhaOrientador.moveTo(x, y);
            linhaOrientador.lineTo(450f, y); // ate onde a linha vai
            linhaOrientador.stroke();
            //------------------------------------------------------
            //nome orientador
            PdfContentByte orientador = writer.getDirectContentUnder();
            orientador.beginText();
            orientador.setFontAndSize(fHelvetica, 12);
            orientador.setTextMatrix(x, y - 20); // x e y
            orientador.showText("Prof.Orientador - " + banca.getTrabalhoidtrabalho().getProfessorsiape().getNome());
            orientador.setTextMatrix(x, y - 35); // x e y
            orientador.showText("Presidente da banca");
            orientador.endText();

            y = y - 100;
            //retorna convidados
            for (int i = 0; i < listaConvidados.size(); i++) {
                Convidado convidado = listaConvidados.get(i);
                if (convidado.getConfirmado() == true) {
                    //-----------------------------------------------------
                    //linha
                    PdfContentByte linha = writer.getDirectContentUnder();
                    linha.setLineWidth(1f); // mostrar linha
                    linha.setGrayStroke(0.5f); // 0 = preto, 1 = branco
                    linha.moveTo(x, y);
                    linha.lineTo(450f, y); // ate onde a linha vai
                    linha.stroke();
                    //------------------------------------------------------
                    //nome convidado

                    PdfContentByte conv = writer.getDirectContentUnder();
                    conv.beginText();
                    conv.setFontAndSize(fHelvetica, 12);
                    conv.setTextMatrix(x, y - 20); // x e y
                    conv.showText("Prof.Convidado - " + convidado.getProfessorsiape().getNome());
                    conv.endText();
                    //------------------------------------------------------
                    y = y - 80;
                }
            }
            //add nova pagina
            document.newPage();
            //close document
            document.close();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(RelatorioBanca.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return stream
        return baos;
    }

    /**
     * funcao para gerar o stream do relatorio de participações em banca
     *
     * @param prof
     * @param listaOrientador
     * @param listaConvidado
     * @param dtinicial
     * @param dtfinal
     * @param coordenador
     * @param campus
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream relatorioParticipacaoEmBanca(Professor prof, List<Banca> listaOrientador, List<Convidado> listaConvidado, Date dtinicial, Date dtfinal, Professor coordenador, Campus campus) {

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
//            univEnd.showText("Telefone: (34) 3239-4144 ou 3239-4393");
//            univEnd.endText();
            //----------------------------------------------------------------------
            //ADICIONAR TITULO
            //----------------------------------------------------------------------
            PdfContentByte t1 = writer.getDirectContentUnder();
            t1.beginText();
            t1.setFontAndSize(fHelvetica, 14);
            t1.setTextMatrix(210, 735); // x e y
            t1.showText("Participação em Bancas");
            t1.endText();

            PdfContentByte t2 = writer.getDirectContentUnder();
            t2.beginText();
            t2.setFontAndSize(fHelvetica, 12);
            t2.setTextMatrix(190, 710); // x e y
            String periodo = CalendarFormat.getDataBRtoDate(dtinicial) + " até " + CalendarFormat.getDataBRtoDate(dtfinal);
            t2.showText("Período: " + periodo);
            t2.endText();

            PdfContentByte t3 = writer.getDirectContentUnder();
            t3.beginText();
            t3.setFontAndSize(fHelvetica, 12);
            t3.setTextMatrix(40, 675); // x e y
            t3.showText("Professor: " + prof.getNome());
            t3.endText();
            //----------------------------------------------------------------------
            //TABELA
            PdfPTable table = new PdfPTable(6);
            //espaço do inicio da pagina
            table.setSpacingBefore(100f);
            table.setTotalWidth(110f);
            table.setWidthPercentage(100);
            float[] widths = {13, 25, 22, 18, 12, 10};//largura das 2 colunas
            table.setWidths(widths);
            table.setHeaderRows(1);

            //CABEÇALHO DA TABELA
            Paragraph cabecalho = new Paragraph("Matricula do Aluno");
            PdfPCell cell1 = new PdfPCell(cabecalho); // celula
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            cabecalho = new Paragraph("Nome do Aluno");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell2 = new PdfPCell(cabecalho); // celula
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setBorderColor(BaseColor.LIGHT_GRAY);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            cabecalho = new Paragraph("Título do Trabalho");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell3 = new PdfPCell(cabecalho); // celula
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setBorderColor(BaseColor.LIGHT_GRAY);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);

            cabecalho = new Paragraph("Nome do Orientador");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell4 = new PdfPCell(cabecalho); // celula
            cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell4.setBorderColor(BaseColor.LIGHT_GRAY);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell4);

            cabecalho = new Paragraph("Papel do Professor");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell5 = new PdfPCell(cabecalho); // celula
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell5.setBorderColor(BaseColor.LIGHT_GRAY);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell5);

            cabecalho = new Paragraph("Data Defesa");
            cabecalho.getFont().setStyle(Font.BOLD);
            cabecalho.getFont().setSize(8);
            PdfPCell cell6 = new PdfPCell(cabecalho); // celula
            cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell6.setBorderColor(BaseColor.LIGHT_GRAY);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell6);

            for (int i = 0; i < listaOrientador.size(); i++) {
                Banca b = listaOrientador.get(i);

                Paragraph texto = new Paragraph(b.getTrabalhoidtrabalho().getAlunomatricula().getMatricula());
                texto.getFont().setSize(8);
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell1);

                texto = new Paragraph(b.getTrabalhoidtrabalho().getAlunomatricula().getNome());
                texto.getFont().setSize(8);
                cell2 = new PdfPCell(texto); // celula
                cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell2);

                texto = new Paragraph(b.getTrabalhoidtrabalho().getTitulo());
                texto.getFont().setSize(8);
                cell3 = new PdfPCell(texto); // celula
                cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell3);

                texto = new Paragraph(b.getTrabalhoidtrabalho().getProfessorsiape().getNome());
                texto.getFont().setSize(8);
                cell4 = new PdfPCell(texto); // celula
                cell4.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell4);

                texto = new Paragraph("Orientador");
                texto.getFont().setSize(8);
                cell5 = new PdfPCell(texto); // celula
                cell5.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell5);

                texto = new Paragraph(CalendarFormat.getDataBRtoDate(b.getDatabanca()));
                texto.getFont().setSize(8);
                cell6 = new PdfPCell(texto); // celula
                cell6.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell6);

                switch (i % 2) {
                    case 0:
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        break;
                    case 1:

                        break;
                }

            }

            for (int i = 0; i < listaConvidado.size(); i++) {
                Convidado c = listaConvidado.get(i);

                Paragraph texto = new Paragraph(c.getBancaidbanca().getTrabalhoidtrabalho().getAlunomatricula().getMatricula());
                texto.getFont().setSize(8);
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell1);

                texto = new Paragraph(c.getBancaidbanca().getTrabalhoidtrabalho().getAlunomatricula().getNome());
                texto.getFont().setSize(8);
                cell2 = new PdfPCell(texto); // celula
                cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell2);

                texto = new Paragraph(c.getBancaidbanca().getTrabalhoidtrabalho().getTitulo());
                texto.getFont().setSize(8);
                cell3 = new PdfPCell(texto); // celula
                cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell3);

                texto = new Paragraph(c.getBancaidbanca().getTrabalhoidtrabalho().getProfessorsiape().getNome());
                texto.getFont().setSize(8);
                cell4 = new PdfPCell(texto); // celula
                cell4.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell4);

                texto = new Paragraph("Convidado");
                texto.getFont().setSize(8);
                cell5 = new PdfPCell(texto); // celula
                cell5.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell5);

                texto = new Paragraph(CalendarFormat.getDataBRtoDate(c.getBancaidbanca().getDatabanca()));
                texto.getFont().setSize(8);
                cell6 = new PdfPCell(texto); // celula
                cell6.setBorderColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell6);

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

            //----------------------------------------------------------------------
            /*String texto = "Ata da defesa de Trabalho de Estágio Supervisionado, do Curso de Bacharelado"
             + "em Ciência da Computação, realizada em " + CalendarFormat.getDataBRtoDate(banca.getDatabanca()) + ", na " + banca.getLocalbanca()
             + ", às " + CalendarFormat.parseDateToTimeString(banca.getHorario()) + " horas, pelo aluno " + banca.getTrabalhoidtrabalho().getAlunomatricula().getNome()
             + " (" + banca.getTrabalhoidtrabalho().getAlunomatricula().getMatricula() + "). O trabalho "
             + "intitulado \"" + banca.getTrabalhoidtrabalho().getTitulo() + "\" foi apresentado pelo aluno em sessão pública "
             + "com cerca de 1 hora de duração. Na ocasião, o aluno foi arguido oralmente pelos membros "
             + "da banca, sendo considerado ________________________ (aprovado/reprovado).";
             */
            //Chunk chunk1 = new Chunk(texto);
            //Paragraph paragraph = new Paragraph();
            // paragraph.setSpacingBefore(180);
            //paragraph.add(chunk1);
            //paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            //document.add(paragraph);
            //document.add(Chunk.NEWLINE);
            //Paragraph paragraph2 = new Paragraph();
            //paragraph2.setSpacingBefore(25);
            //paragraph2.add("Uberlândia, " + CalendarFormat.getDataPorExtenso(banca.getDatabanca()) + ".");
            //paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
            ///document.add(paragraph2);
            //document.add(Chunk.NEWLINE);
            //----------------------------------------------------------------------
            //----------------------------------------------------------------------
            //float x = 160f;
            //float y = 340f; // distancia do fim da pagina
            //retorna orientador
            //-----------------------------------------------------
            //linha
            //PdfContentByte linhaOrientador = writer.getDirectContentUnder();
            //linhaOrientador.setLineWidth(1f); // mostrar linha
            //linhaOrientador.setGrayStroke(0.5f); // 0 = preto, 1 = branco
            //linhaOrientador.moveTo(x, y);
            //linhaOrientador.lineTo(450f, y); // ate onde a linha vai
            //linhaOrientador.stroke();
            //------------------------------------------------------
            //nome orientador
            //PdfContentByte orientador = writer.getDirectContentUnder();
            //orientador.beginText();
            //orientador.setFontAndSize(fHelvetica, 12);
            //orientador.setTextMatrix(x, y - 20); // x e y
            //orientador.showText("Prof.Orientador - " + banca.getTrabalhoidtrabalho().getProfessorsiape().getNome());
            //orientador.setTextMatrix(x, y - 35); // x e y
            //orientador.showText("Presidente da banca");
            //orientador.endText();
            /*
             y = y - 100;
             //retorna convidados
             for (int i = 0; i < listaConvidados.size(); i++) {
             Convidado convidado = listaConvidados.get(i);
             if (convidado.getConfirmado() == true) {
             //-----------------------------------------------------
             //linha
             PdfContentByte linha = writer.getDirectContentUnder();
             linha.setLineWidth(1f); // mostrar linha
             linha.setGrayStroke(0.5f); // 0 = preto, 1 = branco
             linha.moveTo(x, y);
             linha.lineTo(450f, y); // ate onde a linha vai
             linha.stroke();
             //------------------------------------------------------
             //nome convidado

             PdfContentByte conv = writer.getDirectContentUnder();
             conv.beginText();
             conv.setFontAndSize(fHelvetica, 12);
             conv.setTextMatrix(x, y - 20); // x e y
             conv.showText("Prof.Convidado - " + convidado.getProfessorsiape().getNome());
             conv.endText();
             //------------------------------------------------------
             y = y - 80;
             }
             }
             */
            //----------------------------------------------------------------------
            //AlunoDAO aDAO = new AlunoDAO();
            //List<Aluno> allAlunos = aDAO.getAllAlunos();
            //aDAO.closeSession();
//            PdfPTable table = new PdfPTable(5);
//            table.setSpacingBefore(35f);
//            table.setTotalWidth(100f);
//            table.setWidthPercentage(100);
//            float[] widths = {10, 30, 30, 13, 17};//largura das 2 colunas
//            table.setWidths(widths);
//            table.setHeaderRows(1);
//
//            Paragraph cabecalho = new Paragraph("Matricula");
//            PdfPCell cellMatricula = new PdfPCell(cabecalho); // celula
//            cabecalho.getFont().setStyle(Font.BOLD);
//            cabecalho.getFont().setSize(8);
//            cellMatricula.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cellMatricula.setBorderColor(BaseColor.LIGHT_GRAY);
//            cellMatricula.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cellMatricula);
//
//            cabecalho = new Paragraph("Nome");
//            cabecalho.getFont().setStyle(Font.BOLD);
//            cabecalho.getFont().setSize(8);
//            PdfPCell cellNome = new PdfPCell(cabecalho); // celula
//            cellNome.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cellNome.setBorderColor(BaseColor.LIGHT_GRAY);
//            cellNome.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cellNome);
//
//            cabecalho = new Paragraph("Email");
//            cabecalho.getFont().setStyle(Font.BOLD);
//            cabecalho.getFont().setSize(8);
//            PdfPCell cellEmail = new PdfPCell(cabecalho); // celula
//            cellEmail.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
//            cellEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cellEmail);
//
//            cabecalho = new Paragraph("Telefone");
//            cabecalho.getFont().setStyle(Font.BOLD);
//            cabecalho.getFont().setSize(8);
//            PdfPCell cellTelefone = new PdfPCell(cabecalho); // celula
//            cellTelefone.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cellTelefone.setBorderColor(BaseColor.LIGHT_GRAY);
//            cellTelefone.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cellTelefone);
//
//            cabecalho = new Paragraph("Curso");
//            cabecalho.getFont().setStyle(Font.BOLD);
//            cabecalho.getFont().setSize(8);
//            PdfPCell cellCurso = new PdfPCell(cabecalho); // celula
//            cellCurso.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cellCurso.setBorderColor(BaseColor.LIGHT_GRAY);
//            cellCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cellCurso);
//            for (int i = 0; i < allAlunos.size(); i++) {
//                Aluno aluno = allAlunos.get(i);
//                Paragraph texto = new Paragraph(aluno.getMatricula());
//                cellMatricula = new PdfPCell(texto); // celula
//                cellMatricula.setBorderColor(BaseColor.LIGHT_GRAY);
//                texto.getFont().setSize(8);
//                table.addCell(cellMatricula);
//                texto = new Paragraph(aluno.getNome());
//                texto.getFont().setSize(8);
//                cellNome = new PdfPCell(texto); // celula
//                cellNome.setBorderColor(BaseColor.LIGHT_GRAY);
//                table.addCell(cellNome);
//                texto = new Paragraph(aluno.getEmail());
//                texto.getFont().setSize(8);
//                cellEmail = new PdfPCell(texto); // celula
//                cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
//                table.addCell(cellEmail);
//                texto = new Paragraph(aluno.getTelefone());
//                texto.getFont().setSize(8);
//                cellTelefone = new PdfPCell(texto); // celula
//                cellTelefone.setBorderColor(BaseColor.LIGHT_GRAY);
//                table.addCell(cellTelefone);
//                texto = new Paragraph(aluno.getCursoidcurso().getNomecurso());
//                texto.getFont().setSize(8);
//                cellCurso = new PdfPCell(texto); // celula
//                cellCurso.setBorderColor(BaseColor.LIGHT_GRAY);
//                table.addCell(cellCurso);
//
//                switch (i % 2) {
//                    case 0:
//                        cellMatricula.setBorderColor(BaseColor.LIGHT_GRAY);
//                        cellMatricula.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                        break;
//                    case 1:
//
//                        break;
//                }
//
//            }
            //add nova pagina
            document.newPage();
            //close document
            document.close();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(RelatorioBanca.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return stream com dados
        return baos;
    }
}
