package com.backend.housing.infrastructure.pdf;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class PaymentReceiptPdfGenerator {

    private static final DeviceRgb NAVY      = new DeviceRgb(26,  26,  46);
    private static final DeviceRgb SUCCESS   = new DeviceRgb(34, 197, 94);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(45, 55, 72);
    private static final DeviceRgb MID_GRAY  = new DeviceRgb(113, 128, 150);
    private static final DeviceRgb LIGHT_BG  = new DeviceRgb(248, 250, 252);
    private static final DeviceRgb BORDER    = new DeviceRgb(226, 232, 240);
    private static final DeviceRgb GOLD      = new DeviceRgb(212, 175, 55);

    private static final float LEFT_MARGIN   = 56.7f;
    private static final float RIGHT_MARGIN  = 56.7f;
    private static final float TOP_MARGIN    = 45.4f;
    private static final float BOTTOM_MARGIN = 45.4f;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm 'hrs'", new Locale("es", "CO"));

    public byte[] generate(PaymentReceiptResponse receipt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(TOP_MARGIN, RIGHT_MARGIN, BOTTOM_MARGIN, LEFT_MARGIN);

        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new SideBarDrawer(NAVY, 6f));

        DottedLine dottedLine = new DottedLine(0.8f);
        dottedLine.setColor(BORDER);
        LineSeparator dashedSeparator = new LineSeparator(dottedLine);
        dashedSeparator.setMarginTop(5);
        dashedSeparator.setMarginBottom(10);
        document.add(dashedSeparator);

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{65, 35}))
                .useAllAvailableWidth();

        Paragraph brandName = new Paragraph("NESTLY")
                .setFontSize(24)
                .setBold()
                .setFontColor(NAVY)
                .setMargin(0);
        Paragraph brandSubtitle = new Paragraph("Plataforma de gestión inmobiliaria")
                .setFontSize(8)
                .setFontColor(MID_GRAY)
                .setMargin(0);
        Cell brandCell = new Cell().add(brandName).add(brandSubtitle)
                .setBorder(Border.NO_BORDER)
                .setPadding(0);
        headerTable.addCell(brandCell);

        String receiptNumber = receipt.paymentId().toString().substring(0, 12).toUpperCase();
        Paragraph receiptLabel = new Paragraph("COMPROBANTE N°")
                .setFontSize(8)
                .setFontColor(MID_GRAY)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMargin(0);
        Paragraph receiptValue = new Paragraph(receiptNumber)
                .setFontSize(11)
                .setBold()
                .setFontColor(NAVY)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMargin(0);
        Cell receiptCell = new Cell().add(receiptLabel).add(receiptValue)
                .setBorder(Border.NO_BORDER)
                .setPadding(0);
        headerTable.addCell(receiptCell);
        document.add(headerTable);

        SolidLine solidLine = new SolidLine(0.5f);
        solidLine.setColor(BORDER);
        LineSeparator solidSeparator = new LineSeparator(solidLine);
        solidSeparator.setMarginTop(10);
        solidSeparator.setMarginBottom(20);
        document.add(solidSeparator);

        Table titleRow = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                .useAllAvailableWidth();

        Cell titleCell = new Cell()
                .add(new Paragraph("RECIBO DE PAGO")
                        .setFontSize(26)
                        .setBold()
                        .setFontColor(NAVY))
                .setBorder(Border.NO_BORDER);
        titleRow.addCell(titleCell);

        String paidAtStr = receipt.paidAt() != null ? receipt.paidAt().format(DATE_FORMATTER) : "—";
        String paidTimeStr = receipt.paidAt() != null ? receipt.paidAt().format(TIME_FORMATTER) : "—";

        Cell statusCell = new Cell()
                .add(new Paragraph("● CONFIRMADO")
                        .setFontSize(11)
                        .setBold()
                        .setFontColor(SUCCESS)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMargin(0))
                .add(new Paragraph(paidAtStr)
                        .setFontSize(9)
                        .setFontColor(MID_GRAY)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMargin(0))
                .add(new Paragraph(paidTimeStr)
                        .setFontSize(9)
                        .setFontColor(MID_GRAY)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMargin(0))
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        titleRow.addCell(statusCell);
        document.add(titleRow);

        document.add(new Paragraph(" ").setMarginBottom(20));

        // --- MONTO DESTACADO ---
        Table amountBox = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        String formattedAmount = String.format(Locale.forLanguageTag("es-CO"), "%,.0f", receipt.amount());

        Paragraph amountLabel = new Paragraph("MONTO TOTAL PAGADO")
                .setFontSize(10)
                .setFontColor(MID_GRAY);

        Paragraph amountValue = new Paragraph("$ " + formattedAmount)
                .setFontSize(42)
                .setBold()
                .setFontColor(NAVY)
                .setMarginTop(8)
                .setMarginBottom(8);

        Paragraph methodText = new Paragraph(translateMethod(receipt.method().name()) + " · " + receipt.currency())
                .setFontSize(11)
                .setFontColor(MID_GRAY);

        Cell amountCell = new Cell()
                .add(amountLabel)
                .add(amountValue)
                .add(methodText)
                .setBackgroundColor(LIGHT_BG)
                .setBorder(new SolidBorder(BORDER, 1))
                .setPadding(25)
                .setTextAlignment(TextAlignment.CENTER);
        amountBox.addCell(amountCell);
        document.add(amountBox);

        document.add(new Paragraph(" ").setMarginBottom(25));

        Table partiesTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth();

        partiesTable.addCell(createInfoBlock("ARRENDATARIO",
                receipt.tenantName() != null ? receipt.tenantName() : "—"));
        partiesTable.addCell(createInfoBlock("PROPIETARIO",
                receipt.ownerName() != null ? receipt.ownerName() : "—"));
        document.add(partiesTable);

        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(new Paragraph("PROPIEDAD")
                .setFontSize(9)
                .setFontColor(MID_GRAY));
        document.add(new Paragraph(receipt.propertyTitle() != null ? receipt.propertyTitle() : "—")
                .setFontSize(12)
                .setBold()
                .setFontColor(DARK_GRAY)
                .setMarginTop(3));

        document.add(new Paragraph(" ").setMarginBottom(20));

        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
                .useAllAvailableWidth()
                .setBorder(new SolidBorder(BORDER, 1));

        Cell headerConcept = new Cell()
                .add(new Paragraph("CONCEPTO").setBold().setFontSize(9))
                .setBackgroundColor(LIGHT_BG)
                .setBorderBottom(new SolidBorder(BORDER, 1))
                .setPadding(10);
        Cell headerDetail = new Cell()
                .add(new Paragraph("DETALLE").setBold().setFontSize(9))
                .setBackgroundColor(LIGHT_BG)
                .setBorderBottom(new SolidBorder(BORDER, 1))
                .setPadding(10);
        detailsTable.addCell(headerConcept);
        detailsTable.addCell(headerDetail);

        // Filas de información
        addDetailRow(detailsTable, "FECHA DE SOLICITUD",
                receipt.createdAt() != null ? receipt.createdAt().format(DATE_FORMATTER) : "—");
        addDetailRow(detailsTable, "FECHA DE CONFIRMACIÓN", paidAtStr);
        addDetailRow(detailsTable, "ID DE TRANSACCIÓN", receipt.paymentId().toString());
        addDetailRow(detailsTable, "ID DE CONTRATO", receipt.contractId().toString());
        addDetailRow(detailsTable, "MÉTODO DE PAGO", translateMethod(receipt.method().name()));
        addDetailRow(detailsTable, "MONEDA", receipt.currency());

        document.add(detailsTable);
        document.add(new Paragraph(" ").setMarginBottom(25));

        String legalNote = "Este comprobante certifica que el pago correspondiente al contrato de arriendo " +
                "fue procesado y confirmado satisfactoriamente a través de la plataforma Nestly. " +
                "Constituye un documento oficial de valor probatorio. Conserve este comprobante como respaldo.";

        Div noteDiv = new Div()
                .add(new Paragraph(legalNote)
                        .setFontSize(8)
                        .setFontColor(MID_GRAY)
                        .setMultipliedLeading(1.4f))
                .setBackgroundColor(LIGHT_BG)
                .setPadding(12)
                .setBorderLeft(new SolidBorder(GOLD, 3));
        document.add(noteDiv);

        document.add(new Paragraph(" ").setMarginBottom(20));

        SolidLine footerLine = new SolidLine(0.5f);
        footerLine.setColor(BORDER);
        LineSeparator footerSeparator = new LineSeparator(footerLine);
        footerSeparator.setMarginBottom(10);
        document.add(footerSeparator);

        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                .useAllAvailableWidth();

        String verifyCode = "NST-" + receipt.paymentId().toString().substring(0, 8).toUpperCase();
        Paragraph footerLeft = new Paragraph("Documento generado automáticamente · Código: " + verifyCode)
                .setFontSize(7)
                .setFontColor(MID_GRAY);
        footerTable.addCell(new Cell().add(footerLeft).setBorder(Border.NO_BORDER));

        Paragraph footerRight = new Paragraph("nestly.com · Soporte inmobiliario")
                .setFontSize(7)
                .setFontColor(MID_GRAY)
                .setTextAlignment(TextAlignment.RIGHT);
        footerTable.addCell(new Cell().add(footerRight).setBorder(Border.NO_BORDER));

        document.add(footerTable);

        document.close();
        return baos.toByteArray();
    }

    // --- Métodos auxiliares ---

    private Cell createInfoBlock(String label, String value) {
        Paragraph labelP = new Paragraph(label)
                .setFontSize(8)
                .setFontColor(MID_GRAY)
                .setMargin(0);
        Paragraph valueP = new Paragraph(value)
                .setFontSize(12)
                .setBold()
                .setFontColor(DARK_GRAY)
                .setMarginTop(4)
                .setMarginBottom(0);
        return new Cell()
                .add(labelP)
                .add(valueP)
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(12);
    }

    private void addDetailRow(Table table, String concept, String detail) {
        Cell conceptCell = new Cell()
                .add(new Paragraph(concept).setFontSize(9).setFontColor(DARK_GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        Cell detailCell = new Cell()
                .add(new Paragraph(detail).setFontSize(9).setFontColor(MID_GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        table.addCell(conceptCell);
        table.addCell(detailCell);
    }

    private String translateMethod(String method) {
        return switch (method) {
            case "CARD" -> "Tarjeta de crédito / débito";
            case "WALLET" -> "Billetera digital";
            case "BANK_TRANSFER" -> "Transferencia bancaria";
            default -> method;
        };
    }

    private static class SideBarDrawer implements IEventHandler {
        private final DeviceRgb color;
        private final float width;

        SideBarDrawer(DeviceRgb color, float width) {
            this.color = color;
            this.width = width;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle size = page.getPageSize();
            canvas.setFillColor(color)
                    .rectangle(0, 0, width, size.getHeight())
                    .fill()
                    .release();
        }
    }
}