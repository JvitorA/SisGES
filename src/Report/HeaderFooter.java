/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Util.CalendarFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Inner class to add a header and a footer.
 *
 * @author Alessandro
 */
public class HeaderFooter extends PdfPageEventHelper {

    float y = 742f; // distancia do fim da pagina para inserir linha
    float x = 38f;

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        Rectangle rect = writer.getBoxSize("header");
        //--------------------------------------------------------------
        // linha em baixo do header
        PdfContentByte cb = writer.getDirectContent();
        cb.setLineWidth(1f); // mostrar linha
        cb.setGrayStroke(0.5f); // 0 = preto, 1 = branco
        cb.moveTo(x, y);
        cb.lineTo(555f, y); // ate onde a linha vai
        cb.stroke();
        //--------------------------------------------------------------
        // linha em cima do foother
        cb.setLineWidth(1f); // mostrar linha
        cb.setGrayStroke(0.5f); // 0 = preto, 1 = branco
        x = 38f;
        y = 30f; // distancia do fim da pagina
        cb.moveTo(x, y);
        cb.lineTo(555f, y); // ate onde a linha vai
        cb.stroke();
        //--------------------------------------------------------------
        switch (writer.getPageNumber() % 2) {
            case 0:
                /*
                 ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_RIGHT,  new Phrase("odd header"),
                 rect.getRight(), rect.getTop(), 0);
                 */
                break;
            case 1:
                /*
                 ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_LEFT, new Phrase("odd header"),
                 rect.getLeft(), rect.getTop(), 0);
                 */
                break;
        }
        /*
         ColumnText.showTextAligned(writer.getDirectContent(),
         Element.ALIGN_CENTER, new Phrase(String.format("página %d", writer.getPageNumber())),
         (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
         */
        Phrase data = new Phrase(String.format("Gerado em: %s", CalendarFormat.getDataHorarioSOToString()));
        //Font fontbold = FontFactory.getFont("Times-Roman",4, Font.NORMAL);
        //data.setFont(fontbold);
        data.getFont().setSize(8);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, data, rect.getLeft() + 5, rect.getBottom() - 38, 0);

    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

}
