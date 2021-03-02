/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.helpers.DateHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import javax.swing.JPanel;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/**
 *
 * @author santos
 */
public abstract class Report {

    public static final String DEST_PATH = "resources/reports/";
    private static final String RESOURCE_IMG = "/img";

    protected static final PdfNumber PORTRAIT = new PdfNumber(0);
    protected static final PdfNumber LANDSCAPE = new PdfNumber(90);

    private PdfDocument pdf;
    private PdfFont helvetica;
    private PdfFont helveticaBold;

    private Image brasaoImage;
    private Image ifmsImage;

    protected Document document;
    protected JPanel viewer;
    private String filename;

    private PdfNumber orietation;

    public Report() throws IOException {
        this("hello.pdf", PORTRAIT);
    }

    public Report(String fileName, PdfNumber orientagion) throws FileNotFoundException, IOException {
        this.filename = DEST_PATH + fileName;
        this.orietation = orientagion;
        File file = new File(this.filename);
        File parentpath = file.getParentFile();
        if (!parentpath.exists()) {
            parentpath.mkdirs();
        }

        PdfWriter pdfWriter = new PdfWriter(this.filename);
        pdf = new PdfDocument(pdfWriter);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderFooterHandler());

        helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA);
        helveticaBold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        URL urlBrasao = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "brasao-do-brasil-republica.50px.png"));
        URL urlIfms = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "marcaifms.50px.png"));
        
        brasaoImage = new Image(ImageDataFactory.create(urlBrasao));
        ifmsImage = new Image(ImageDataFactory.create(urlIfms));
    }

    public abstract void createReport(Document document);

    public void initReport() {
        if (orietation == LANDSCAPE) {
            document = new Document(pdf, PageSize.A4.rotate());
        } else {
            document = new Document(pdf, PageSize.A4);
        }
        document.setMargins(100, 20, 60, 20);

        createReport(document);
        document.flush();
        document.close();
    }

    private Table createReportHeader() {
        String sHeader[] = new String[]{
            "MINISTÉRIO DA EDUCAÇÃO",
            "Secretaria de Educação Profissional e Tecnológica",
            "Instituto Federal de Educação, Ciência e Tecnologia de Mato Grosso do Sul",
            "Campus Naviraí"
        };
        Integer fontSize = 9;

        Color green = new DeviceRgb(33, 176, 113);
        Border dottedBorder = new DottedBorder(green, 1);

        Table table = new Table(new float[]{1, 3, 1});
        table.setWidth(UnitValue.createPercentValue(100))
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        int rowSpan = sHeader.length;
        table.addCell(new Cell(rowSpan, 1).add(brasaoImage
                .setHorizontalAlignment(HorizontalAlignment.LEFT))
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(dottedBorder));

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER)
                .setBorderBottom(dottedBorder);
        for (int i = 0; i < rowSpan; i++) {
            cell.add(new Paragraph(sHeader[i]).setFontSize(fontSize));
        }
        table.addCell(cell);

        table.addCell(new Cell(rowSpan, 1).add(ifmsImage
                .setHorizontalAlignment(HorizontalAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(dottedBorder));

        return table;
    }

    private Table createReportFooter(int pageNumber, int totalPages) {
        String sData = String.format("Impresso em %s",
                DateHelper.dateToString(new Date(), "dd/MM/yyyy")),
                sPagina = String.format("Página %d de %d", pageNumber, totalPages);
        Integer fontSize = 8;

        Table table = new Table(new float[]{1, 1});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell(new Cell().add(new Paragraph(sData).setFontSize(fontSize))
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new SolidBorder(1)));
        table.addCell(new Cell().add(new Paragraph(sPagina)
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new SolidBorder(1)));

        return table;
    }

    private class HeaderFooterHandler implements IEventHandler {

        private PdfNumber rotation = PORTRAIT;

        public void setRotation(PdfNumber orientation) {
            this.rotation = orientation;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();

            page.put(PdfName.Rotate, rotation);

            int pageNumber = pdfDoc.getPageNumber(page),
                    total = pdfDoc.getNumberOfPages();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.newContentStreamBefore(), page.getResources(), pdfDoc);

            // Adicionando o rodapé
            pdfCanvas.beginText()
                    .setFontAndSize(helvetica, 9)
                    .moveText(60, -pageSize.getTop() + 30)
                    .showText(String.valueOf(pageNumber))
                    .endText();

            // Adicionando o cabeçalho
            Canvas canvasCabeca = new Canvas(pdfCanvas, pdfDoc,
                    new Rectangle(pageSize.getLeft() + 20,
                            pageSize.getBottom(),
                            pageSize.getWidth() - 40,
                            pageSize.getHeight() - 20
                    ));

            canvasCabeca.add(createReportHeader());

            // Adicionando o rodape
            Canvas canvasRodape = new Canvas(pdfCanvas, pdfDoc,
                    new Rectangle(pageSize.getLeft() + 20,
                            -pageSize.getTop() + 40,
                            pageSize.getWidth() - 40,
                            pageSize.getHeight()
                    ));

            canvasRodape.add(createReportFooter(pageNumber, total));
            pdfCanvas.release();
        }

    }

    public JPanel getViewer() {
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        JPanel panel = factory.buildViewerPanel();

        ComponentKeyBinding.install(controller, panel);
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));
        controller.openDocument(this.filename);
        return panel;
    }

//    public static void main(String args[]) throws IOException {
//        new Report();
//
//        System.out.println("Awesome PDF just got created.");
//    }
}
