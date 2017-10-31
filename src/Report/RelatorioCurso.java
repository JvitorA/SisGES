/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Model.Alunoaprovado;
import Model.Alunomatriculado;
import Model.Campus;
import Model.Professor;
import Model.Professororientacaoseparada;
import Model.Sumarioaprovado;
import Model.Sumariomatricula;
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
import com.itextpdf.text.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
public class RelatorioCurso {

    String realPath;

    /**
     * Função para gerar o stream do relatório anual de atividades
     *
     * @param proforiseparada
     * @param sm
     * @param sa
     * @param am
     * @param aa
     * @param dtinicial
     * @param dtfinal
     * @param ano
     * @param cursoNome
     * @param coordenador
     * @param campus
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream relatorioAnualDeAtividades(ArrayList<Professororientacaoseparada> proforiseparada, ArrayList<Sumariomatricula> sm, ArrayList<Sumarioaprovado> sa, ArrayList<Alunomatriculado> am, ArrayList<Alunoaprovado> aa, Date dtinicial, Date dtfinal, String ano, String cursoNome, Professor coordenador, Campus campus) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacesContext faces = FacesContext.getCurrentInstance();
        // pega o contexto da aplicacao
        realPath = faces.getExternalContext().getRealPath("/");
        //realPath = "C:/Users/Alessandro/Desktop/TCC2/SisGES/build/web";

        try {
            BaseFont fHelvetica = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
            BaseFont fHelveticaBOLD = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);

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

            //----------------------------------------------------------------------
            //ADICIONAR TITULO
            //----------------------------------------------------------------------
            PdfContentByte t1 = writer.getDirectContentUnder();
            t1.beginText();
            t1.setFontAndSize(fHelveticaBOLD, 12);
            t1.setTextMatrix(175, 700); // x e y
            t1.showText("RELATÓRIO ANUAL DE ATIVIDADES - " + ano);
            t1.endText();

            PdfContentByte t2 = writer.getDirectContentUnder();
            t2.beginText();
            t2.setFontAndSize(fHelveticaBOLD, 12);
            t2.setTextMatrix(160, 680); // x e y
            if (cursoNome.contains("sistema") || cursoNome.contains("Sistemas") || cursoNome.contains("Sistema")) {
                t2.showText("BACHARELADO EM SISTEMAS DE INFORMAÇÃO");
                cursoNome = "Sistemas de Informação";
            } else if (cursoNome.contains("ciência") || cursoNome.contains("Ciências") || cursoNome.contains("Ciência")) {
                t2.showText("BACHARELADO EM CIÊNCIA DA COMPUTAÇÃO");
                cursoNome = "Ciência da Computação";
            } else {
                t2.showText("BACHARELADO EM " + cursoNome.toUpperCase());
            }
            t2.endText();

            Paragraph p0 = new Paragraph("1. Introdução");
            p0.setSpacingBefore(95);
            p0.getFont().setStyle(Font.BOLD);
            p0.getFont().setSize(12);
            document.add(p0);

            //----------------------------------------------------------------------
            String textoIntro = "Este documento tem o objetivo de apresentar as atividades da coordenação de estágio "
                    + "supervisionado da Faculdade de Computação da Universidade Federal de Uberlândia. As principais "
                    + "atividades desenvolvidas foram:";

            Chunk chunk1 = new Chunk(textoIntro);
            Paragraph paragraph = new Paragraph();
            paragraph.setSpacingBefore(20);
            paragraph.setSpacingAfter(10);
            paragraph.setFont(new Font(fHelvetica, 12));
            paragraph.add(chunk1);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph);

            //lista
            List overview = new List(false, 10);
            overview.setAutoindent(true);
            overview.add("Aprovação dos documentos de matricula do estágio supervisionado;");
            overview.add("Alocação de professores orientadores de estágio;");
            overview.add("Matrícula de alunos do curso de " + cursoNome + " na disciplina de estágio supervisionado;");
            overview.add("Acompanhamento das atividades do estagiário na empresa;");
            overview.add("Aprovação do Relatório parcial/final do estagiário.");
            document.add(overview);

            String textoIntro2 = "Os resultados das atividades listadas são apresentados nas próximas seções.";

            Chunk chunk2 = new Chunk(textoIntro2);
            Paragraph paragraph2 = new Paragraph();
            paragraph2.setSpacingBefore(10);
            paragraph2.setSpacingAfter(10);
            paragraph2.setFont(new Font(fHelvetica, 12));
            paragraph2.add(chunk2);
            paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph2);

            Paragraph p01 = new Paragraph("2. Sumário das Atividades");
            p01.setSpacingBefore(5);
            p01.getFont().setStyle(Font.BOLD);
            p01.getFont().setSize(12);
            document.add(p01);

            //----------------------------------------------------------------------
            //FAZER SUMARIO
            //----------------------------------------------------------------------
            PdfPTable tabelaSumario = new PdfPTable(4);
            tabelaSumario.setTotalWidth(100f);
            tabelaSumario.setWidthPercentage(100);
            float[] widths = {40, 20, 20, 20};//largura das colunas
            tabelaSumario.setWidths(widths);
            tabelaSumario.setHeaderRows(1);

            Paragraph l1 = new Paragraph(" ");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            PdfPCell cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.WHITE);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaSumario.addCell(cell1);

            l1 = new Paragraph("Matrículas");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            PdfPCell cell2 = new PdfPCell(l1); // celula
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setBorderColor(BaseColor.LIGHT_GRAY);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaSumario.addCell(cell2);

            l1 = new Paragraph("Aprovações");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            PdfPCell cell3 = new PdfPCell(l1); // celula
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setBorderColor(BaseColor.LIGHT_GRAY);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaSumario.addCell(cell3);

            l1 = new Paragraph("Reprovações");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            PdfPCell cell4 = new PdfPCell(l1); // celula
            cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell4.setBorderColor(BaseColor.LIGHT_GRAY);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaSumario.addCell(cell4);

            int i = 0;
            for (Sumariomatricula m : sm) {
                Sumarioaprovado a = sa.get(i);

                Paragraph texto = new Paragraph(m.getTipoestagio());
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                texto.getFont().setSize(8);
                tabelaSumario.addCell(cell1);

                if (m.getMatriculas() != null) {
                    texto = new Paragraph(String.valueOf(m.getMatriculas()));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaSumario.addCell(cell1);
                } else {
                    texto = new Paragraph(String.valueOf(0));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaSumario.addCell(cell1);
                }

                if (m.getTipoestagio().equalsIgnoreCase("Estágio Obrigatório")
                        && a.getTipoestagio().equalsIgnoreCase("Estágio Obrigatório")) {
                    if (a.getAprovados() != null) {
                        //aprovados
                        texto = new Paragraph(String.valueOf(a.getAprovados()));
                        cell1 = new PdfPCell(texto); // celula
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        texto.getFont().setSize(8);
                        tabelaSumario.addCell(cell1);
                    } else {
                        texto = new Paragraph(String.valueOf(0));
                        cell1 = new PdfPCell(texto); // celula
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        texto.getFont().setSize(8);
                        tabelaSumario.addCell(cell1);
                    }

                    //reprovados
                    texto = new Paragraph(String.valueOf(0));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaSumario.addCell(cell1);

                } else if (m.getTipoestagio().equalsIgnoreCase("Estágio Não Obrigatório")
                        && a.getTipoestagio().equalsIgnoreCase("Estágio Não Obrigatório")) {

                    if (a.getAprovados() != null) {
                        //aprovados
                        texto = new Paragraph(String.valueOf(a.getAprovados()));
                        cell1 = new PdfPCell(texto); // celula
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        texto.getFont().setSize(8);
                        tabelaSumario.addCell(cell1);
                    } else {
                        texto = new Paragraph(String.valueOf(0));
                        cell1 = new PdfPCell(texto); // celula
                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        texto.getFont().setSize(8);
                        tabelaSumario.addCell(cell1);
                    }

                    //reprovados
                    texto = new Paragraph(String.valueOf(0));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaSumario.addCell(cell1);
                }
                i++;
            }
            tabelaSumario.setSpacingBefore(20);
            tabelaSumario.setSpacingAfter(20);
            tabelaSumario.completeRow();

            document.add(tabelaSumario);

            //----------------------------------------------------------------------
            //ALUNOS MATRICULADOS
            //----------------------------------------------------------------------
            Paragraph p1 = new Paragraph("3. Alunos Matriculados");
            p1.setSpacingBefore(5);
            p1.getFont().setStyle(Font.BOLD);
            p1.getFont().setSize(12);
            document.add(p1);

            p01 = new Paragraph("Estágio Obrigatório");
            p01.setSpacingBefore(5);
            p01.getFont().setStyle(Font.BOLD);
            p01.getFont().setSize(10);
            document.add(p01);

            //----------------------------------------------------------------------
            //FAZER TABELA ALUNOS MATRICULADOS ESTAGIO OBRIGATORIO
            //----------------------------------------------------------------------
            PdfPTable tabelaAlunoMatriculadoEO = new PdfPTable(4);
            tabelaAlunoMatriculadoEO.setTotalWidth(100f);
            tabelaAlunoMatriculadoEO.setWidthPercentage(100);
            float[] widthsEO = {20, 30, 30, 20};//largura das colunas
            tabelaAlunoMatriculadoEO.setWidths(widthsEO);
            tabelaAlunoMatriculadoEO.setHeaderRows(1);

            l1 = new Paragraph("Matrículas");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoEO.addCell(cell1);

            l1 = new Paragraph("Nome");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoEO.addCell(cell1);

            l1 = new Paragraph("Orientador");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoEO.addCell(cell1);

            l1 = new Paragraph("Data de Matrícula");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoEO.addCell(cell1);

            for (int j = 0; j < am.size(); j++) {
                Alunomatriculado amat = am.get(j);
                if (amat.getTipoestagio().equalsIgnoreCase("Estágio Obrigatório")
                        || amat.getTipoestagio().equalsIgnoreCase("Estágio Obrigatorio")
                        || amat.getTipoestagio().equalsIgnoreCase("Estagio Obrigatorio")) {

                    Paragraph texto = new Paragraph(amat.getMatricula());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoEO.addCell(cell1);

                    texto = new Paragraph(amat.getNome());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoEO.addCell(cell1);

                    texto = new Paragraph(amat.getOrientador());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoEO.addCell(cell1);

                    texto = new Paragraph(CalendarFormat.getDataBRtoDate(amat.getDatamatricula()));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoEO.addCell(cell1);
                }
            }

            tabelaAlunoMatriculadoEO.setSpacingBefore(20);
            tabelaAlunoMatriculadoEO.setSpacingAfter(20);
            tabelaAlunoMatriculadoEO.completeRow();

            document.add(tabelaAlunoMatriculadoEO);

            //----------------------------------------------------------------------
            //FAZER TABELA ALUNOS MATRICULADOS ESTAGIO NÃO OBRIGATORIO
            //----------------------------------------------------------------------
            p01 = new Paragraph("Estágio Não Obrigatório");
            p01.setSpacingBefore(5);
            p01.getFont().setStyle(Font.BOLD);
            p01.getFont().setSize(10);
            document.add(p01);

            PdfPTable tabelaAlunoMatriculadoENO = new PdfPTable(4);
            tabelaAlunoMatriculadoENO.setTotalWidth(100f);
            tabelaAlunoMatriculadoENO.setWidthPercentage(100);
            float[] widthsENO = {20, 30, 30, 20};//largura das colunas
            tabelaAlunoMatriculadoENO.setWidths(widthsENO);
            tabelaAlunoMatriculadoENO.setHeaderRows(1);

            l1 = new Paragraph("Matrículas");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoENO.addCell(cell1);

            l1 = new Paragraph("Nome");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoENO.addCell(cell1);

            l1 = new Paragraph("Orientador");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoENO.addCell(cell1);

            l1 = new Paragraph("Data de Matrícula");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoMatriculadoENO.addCell(cell1);

            for (int k = 0; k < am.size(); k++) {
                Alunomatriculado amat = am.get(k);

                if (amat.getTipoestagio().equalsIgnoreCase("Estágio Não Obrigatório")
                        || amat.getTipoestagio().equalsIgnoreCase("Estágio Não Obrigatorio")
                        || amat.getTipoestagio().equalsIgnoreCase("Estagio Não Obrigatorio")) {

                    Paragraph texto = new Paragraph(amat.getMatricula());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoENO.addCell(cell1);

                    texto = new Paragraph(amat.getNome());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoENO.addCell(cell1);

                    texto = new Paragraph(amat.getOrientador());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoENO.addCell(cell1);

                    texto = new Paragraph(CalendarFormat.getDataBRtoDate(amat.getDatamatricula()));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoMatriculadoENO.addCell(cell1);
                }

            }

            tabelaAlunoMatriculadoENO.setSpacingBefore(20);
            tabelaAlunoMatriculadoENO.setSpacingAfter(20);
            tabelaAlunoMatriculadoENO.completeRow();

            document.add(tabelaAlunoMatriculadoENO);

            //add nova pagina
            //document.newPage();
            //----------------------------------------------------------------------
            //ALUNOS APROVADOS EM ESTAGIO OBRIGATORIO
            //----------------------------------------------------------------------
            p1 = new Paragraph("4. Alunos Aprovados em Estágio Obrigatório");
            p1.setSpacingBefore(20);
            p1.getFont().setStyle(Font.BOLD);
            p1.getFont().setSize(12);
            document.add(p1);

            PdfPTable tabelaAlunoAprovadoEO = new PdfPTable(5);
            tabelaAlunoAprovadoEO.setTotalWidth(100f);
            tabelaAlunoAprovadoEO.setWidthPercentage(100);
            float[] widthsAprovadoEO = {20, 30, 30, 10, 10};//largura das colunas
            tabelaAlunoAprovadoEO.setWidths(widthsAprovadoEO);
            tabelaAlunoAprovadoEO.setHeaderRows(1);

            l1 = new Paragraph("Matrículas");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoAprovadoEO.addCell(cell1);

            l1 = new Paragraph("Nome");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoAprovadoEO.addCell(cell1);

            l1 = new Paragraph("Orientador");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoAprovadoEO.addCell(cell1);

            l1 = new Paragraph("Data de Matrícula");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoAprovadoEO.addCell(cell1);

            l1 = new Paragraph("Data de Finalização");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaAlunoAprovadoEO.addCell(cell1);

            for (int t = 0; t < aa.size(); t++) {
                Alunoaprovado aprov = aa.get(t);

                if (aprov.getTipoestagio().equalsIgnoreCase("Estágio Obrigatório")
                        || aprov.getTipoestagio().equalsIgnoreCase("Estágio Obrigatorio")
                        || aprov.getTipoestagio().equalsIgnoreCase("Estagio Obrigatorio")) {

                    Paragraph texto = new Paragraph(aprov.getMatricula());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoAprovadoEO.addCell(cell1);

                    texto = new Paragraph(aprov.getNome());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoAprovadoEO.addCell(cell1);

                    texto = new Paragraph(aprov.getOrientador());
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoAprovadoEO.addCell(cell1);

                    texto = new Paragraph(CalendarFormat.getDataBRtoDate(aprov.getDatamatricula()));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoAprovadoEO.addCell(cell1);

                    texto = new Paragraph(CalendarFormat.getDataBRtoDate(aprov.getDatafinalizacao()));
                    cell1 = new PdfPCell(texto); // celula
                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    texto.getFont().setSize(8);
                    tabelaAlunoAprovadoEO.addCell(cell1);
                }
            }

            tabelaAlunoAprovadoEO.setSpacingBefore(20);
            tabelaAlunoAprovadoEO.setSpacingAfter(20);
            tabelaAlunoAprovadoEO.completeRow();

            document.add(tabelaAlunoAprovadoEO);

            //----------------------------------------------------------------------
            //ALUNOS APROVADOS EM ESTAGIO OBRIGATORIO
            //----------------------------------------------------------------------
            p1 = new Paragraph("5. Participação dos professores FACOM");
            p1.setSpacingBefore(20);
            p1.getFont().setStyle(Font.BOLD);
            p1.getFont().setSize(12);
            document.add(p1);

            //
            PdfPTable tabelaProfOriSeparada = new PdfPTable(3);
            tabelaProfOriSeparada.setTotalWidth(100f);
            tabelaProfOriSeparada.setWidthPercentage(100);
            float[] widths0 = {60, 20, 20};//largura das colunas
            tabelaProfOriSeparada.setWidths(widths0);
            tabelaProfOriSeparada.setHeaderRows(1);

            l1 = new Paragraph("Professor");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaProfOriSeparada.addCell(cell1);

            l1 = new Paragraph("Orientações Estágio Obrigatório");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaProfOriSeparada.addCell(cell1);

            l1 = new Paragraph("Orientações Estágio Não Obrigatório");
            l1.getFont().setStyle(Font.BOLD);
            l1.getFont().setSize(8);
            cell1 = new PdfPCell(l1); // celula
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setBorderColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaProfOriSeparada.addCell(cell1);

            for (int ii = 0; ii < proforiseparada.size(); ii++) {

                Professororientacaoseparada profoe = proforiseparada.get(ii);

                Paragraph texto = new Paragraph(profoe.getNome());
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                texto.getFont().setSize(8);
                tabelaProfOriSeparada.addCell(cell1);

                texto = new Paragraph(profoe.getOrientacoesestobrigatorio().toString());
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                texto.getFont().setSize(8);
                tabelaProfOriSeparada.addCell(cell1);

                texto = new Paragraph(profoe.getOrientacoesestnaoobrigatorio().toString());
                cell1 = new PdfPCell(texto); // celula
                cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                texto.getFont().setSize(8);
                tabelaProfOriSeparada.addCell(cell1);

            }

            tabelaProfOriSeparada.setSpacingBefore(20);
            tabelaProfOriSeparada.setSpacingAfter(20);
            tabelaProfOriSeparada.completeRow();
            document.add(tabelaProfOriSeparada);

            //----------------------------------------------------------------------
            p1 = new Paragraph("6. Observações e Considerações Finais");
            p1.setSpacingBefore(20);
            p1.getFont().setStyle(Font.BOLD);
            p1.getFont().setSize(12);
            document.add(p1);

            String text6 = "O processo de matrícula, acompanhamento, composição de bancas e "
                    + "defesas ocorreu sem nenhum problema ou acontecimento excepcional.";

            chunk1 = new Chunk(text6);
            paragraph = new Paragraph();
            paragraph.setSpacingBefore(20);
            paragraph.setSpacingAfter(10);
            paragraph.setFont(new Font(fHelvetica, 12));
            paragraph.add(chunk1);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            //-----------------------------------------------------
            Paragraph paragraphCab = new Paragraph();
            paragraphCab.setSpacingBefore(25);
            paragraphCab.add("Uberlândia, " + CalendarFormat.getDataPorExtenso(CalendarFormat.getDataSO()) + ".");
            paragraphCab.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraphCab);
            document.add(Chunk.NEWLINE);
            //-----------------------------------------------------
            // distancia do fim da pagina
            float y = 120f;
            float x = 160f;
            //-----------------------------------------------------
            //linha
            PdfContentByte linha = writer.getDirectContentUnder();
            linha.setLineWidth(1f); // mostrar linha
            linha.setGrayStroke(0.5f); // 0 = preto, 1 = branco
            linha.moveTo(x, y);
            linha.lineTo(450f, y); // ate onde a linha vai
            linha.stroke();
            //assinatura
            PdfContentByte a0 = writer.getDirectContentUnder();
            a0.beginText();
            a0.setFontAndSize(fHelvetica, 10);
            a0.setTextMatrix(x + 50, y - 15); // x e y
            a0.showText("Prof. " + coordenador.getNome());
            a0.endText();
            PdfContentByte a1 = writer.getDirectContentUnder();
            a1.beginText();
            a1.setFontAndSize(fHelvetica, 10);
            a1.setTextMatrix(x + 50, y - 30); // x e y
            a1.showText("Coordenação de Estágio Supervisionado");
            a1.endText();
            //assinatura
            PdfContentByte a2 = writer.getDirectContentUnder();
            a2.beginText();
            a2.setFontAndSize(fHelvetica, 10);
            a2.setTextMatrix(x + 50, y - 45); // x e y
            a2.showText("SIAPE: " + coordenador.getSiape());
            a2.endText();
            //----------------------------------------------------------------------
            //FIM DO DOCUMENTO
            //add nova pagina
            document.newPage();
            //close document
            document.close();
            //----------------------------------------------------------------------
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(RelatorioAluno.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return stream
        return baos;
    }
}
