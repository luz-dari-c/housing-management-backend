package com.backend.housing.infrastructure.pdf;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class PaymentReceiptPdfGenerator {

    private static final DeviceRgb BLACK = new DeviceRgb(0, 0, 0);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(45, 45, 45);
    private static final DeviceRgb GRAY = new DeviceRgb(80, 80, 80);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(245, 245, 245);
    private static final DeviceRgb BORDER_GRAY = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb SUCCESS_GREEN = new DeviceRgb(34, 139, 34);

    private static final float MARGIN_TOP = 72f;
    private static final float MARGIN_BOTTOM = 72f;
    private static final float MARGIN_LEFT = 72f;
    private static final float MARGIN_RIGHT = 72f;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm 'hrs'", new Locale("es", "CO"));

    private PdfFont regularFont;
    private PdfFont boldFont;

    public byte[] generate(PaymentReceiptResponse receipt) {
        try {
            regularFont = PdfFontFactory.createFont("Helvetica");
            boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        } catch (IOException e) {
            throw new RuntimeException("Error loading fonts", e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT);

        document.add(createHeader(receipt));
        document.add(new Paragraph(" ").setMarginBottom(10));

        document.add(createTitle());
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createStatusSection(receipt));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createAmountSection(receipt));
        document.add(new Paragraph(" ").setMarginBottom(25));

        document.add(createPartiesSection(receipt));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createPaymentDetailsSection(receipt));
        document.add(new Paragraph(" ").setMarginBottom(25));

        document.add(createLegalFooter());

        document.close();
        return baos.toByteArray();
    }

    private Table createHeader(PaymentReceiptResponse receipt) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{100})).useAllAvailableWidth();

        Paragraph title = new Paragraph("VOLTRIX HOUSE")
                .setFont(boldFont).setFontSize(22).setFontColor(BLACK)
                .setTextAlignment(TextAlignment.CENTER);
        Paragraph subtitle = new Paragraph("Sistema de Gestión de Arriendos")
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerCell = new Cell()
                .add(title)
                .add(subtitle)
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(10);
        header.addCell(headerCell);

        SolidLine solidLine = new SolidLine(0.8f);
        solidLine.setColor(BLACK);
        LineSeparator line = new LineSeparator(solidLine);
        header.addCell(new Cell().add(line).setBorder(Border.NO_BORDER).setPadding(0));

        return header;
    }

    private Div createTitle() {
        Div titleDiv = new Div();

        Paragraph mainTitle = new Paragraph("COMPROBANTE DE PAGO")
                .setFont(boldFont).setFontSize(18).setFontColor(BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph receiptNumber = new Paragraph("RECIBO N°")
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        titleDiv.add(mainTitle);
        titleDiv.add(receiptNumber);
        titleDiv.setMarginBottom(5);

        return titleDiv;
    }

    private Table createStatusSection(PaymentReceiptResponse receipt) {
        Table statusTable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();

        String paidAtStr = receipt.paidAt() != null ? receipt.paidAt().format(DATE_FORMATTER) : "—";
        String paidTimeStr = receipt.paidAt() != null ? receipt.paidAt().format(TIME_FORMATTER) : "—";

        Paragraph status = new Paragraph("PAGO CONFIRMADO")
                .setFont(boldFont).setFontSize(11).setFontColor(SUCCESS_GREEN)
                .setTextAlignment(TextAlignment.CENTER);
        Paragraph date = new Paragraph(paidAtStr + " - " + paidTimeStr)
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        Cell statusCell = new Cell()
                .add(status)
                .add(date)
                .setBorder(new SolidBorder(BORDER_GRAY, 1))
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(10);
        statusTable.addCell(statusCell);

        return statusTable;
    }

    private Div createAmountSection(PaymentReceiptResponse receipt) {
        Div amountDiv = new Div();

        String formattedAmount = String.format(Locale.forLanguageTag("es-CO"), "%,.0f", receipt.amount());

        Paragraph amountLabel = new Paragraph("MONTO PAGADO")
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph amountValue = new Paragraph("$ " + formattedAmount + " " + receipt.currency())
                .setFont(boldFont).setFontSize(28).setFontColor(BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph paymentMethod = new Paragraph("Método: " + translateMethod(receipt.method().name()))
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        amountDiv.add(amountLabel);
        amountDiv.add(amountValue);
        amountDiv.add(paymentMethod);
        amountDiv.setMarginBottom(10);

        return amountDiv;
    }

    private Table createPartiesSection(PaymentReceiptResponse receipt) {
        Table partiesTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
        partiesTable.setBorder(new SolidBorder(BORDER_GRAY, 1));

        Cell headerLeft = new Cell()
                .add(new Paragraph("ARRENDATARIO").setFont(boldFont).setFontSize(9).setFontColor(BLACK))
                .setBackgroundColor(LIGHT_GRAY)
                .setBorderBottom(new SolidBorder(BORDER_GRAY, 1))
                .setPadding(8);
        Cell headerRight = new Cell()
                .add(new Paragraph("ARRENDADOR").setFont(boldFont).setFontSize(9).setFontColor(BLACK))
                .setBackgroundColor(LIGHT_GRAY)
                .setBorderBottom(new SolidBorder(BORDER_GRAY, 1))
                .setPadding(8);
        partiesTable.addCell(headerLeft);
        partiesTable.addCell(headerRight);

        Cell tenantCell = new Cell()
                .add(new Paragraph(receipt.tenantName() != null ? receipt.tenantName() : "—")
                        .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY))
                .setPadding(10);
        Cell ownerCell = new Cell()
                .add(new Paragraph(receipt.ownerName() != null ? receipt.ownerName() : "—")
                        .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY))
                .setPadding(10);
        partiesTable.addCell(tenantCell);
        partiesTable.addCell(ownerCell);

        return partiesTable;
    }

    private Div createPaymentDetailsSection(PaymentReceiptResponse receipt) {
        Div detailsDiv = new Div();

        Paragraph title = new Paragraph("DETALLES DEL PAGO")
                .setFont(boldFont).setFontSize(11).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        detailsDiv.add(title);
        detailsDiv.add(new Paragraph(" ").setMarginBottom(5));

        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{35, 65})).useAllAvailableWidth();
        detailsTable.setBorder(new SolidBorder(BORDER_GRAY, 1));

        addDetailRow(detailsTable, "PROPIEDAD", receipt.propertyTitle() != null ? receipt.propertyTitle() : "—");
        addDetailRow(detailsTable, "ID DE CONTRATO", receipt.contractId().toString());
        addDetailRow(detailsTable, "ID DE TRANSACCIÓN", receipt.paymentId().toString());
        addDetailRow(detailsTable, "FECHA DE PAGO", receipt.paidAt() != null ? receipt.paidAt().format(DATE_FORMATTER) : "—");
        addDetailRow(detailsTable, "HORA DE PAGO", receipt.paidAt() != null ? receipt.paidAt().format(TIME_FORMATTER) : "—");
        addDetailRow(detailsTable, "MÉTODO DE PAGO", translateMethod(receipt.method().name()));
        addDetailRow(detailsTable, "MONEDA", receipt.currency());
        addDetailRow(detailsTable, "PERÍODO PAGADO", extractPeriodDescription(receipt.period()));

        detailsDiv.add(detailsTable);

        return detailsDiv;
    }

    private String extractPeriodDescription(String period) {
        if (period == null || period.isEmpty()) {
            return "—";
        }

        if (period.matches("\\d{4}-\\d{2}")) {
            String[] parts = period.split("-");
            String year = parts[0];
            String month = parts[1];
            String monthName = switch (month) {
                case "01" -> "Enero";
                case "02" -> "Febrero";
                case "03" -> "Marzo";
                case "04" -> "Abril";
                case "05" -> "Mayo";
                case "06" -> "Junio";
                case "07" -> "Julio";
                case "08" -> "Agosto";
                case "09" -> "Septiembre";
                case "10" -> "Octubre";
                case "11" -> "Noviembre";
                case "12" -> "Diciembre";
                default -> month;
            };
            return monthName + " " + year;
        }

        if (period.matches("\\d{4}-W\\d{1,2}")) {
            String[] parts = period.split("-W");
            String year = parts[0];
            String week = parts[1];
            return "Semana " + week + " del " + year;
        }

        if (period.contains("quincena")) {
            return period.replace("_", " - ").toUpperCase();
        }

        return period;
    }
    private void addDetailRow(Table table, String concept, String detail) {
        Cell conceptCell = new Cell()
                .add(new Paragraph(concept).setFont(boldFont).setFontSize(9).setFontColor(DARK_GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        Cell detailCell = new Cell()
                .add(new Paragraph(detail).setFont(regularFont).setFontSize(9).setFontColor(GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        table.addCell(conceptCell);
        table.addCell(detailCell);
    }

    private Div createLegalFooter() {
        Div footer = new Div();
        footer.setBorderTop(new SolidBorder(BORDER_GRAY, 0.5f));
        footer.setMarginTop(20);
        footer.setPaddingTop(10);

        Paragraph legalText = new Paragraph(
                "Este documento es un comprobante de pago válido. " +
                        "Conserve este recibo como respaldo de su transacción.")
                .setFont(regularFont).setFontSize(7).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(1.3f);
        footer.add(legalText);

        Paragraph companyInfo = new Paragraph("VOLTRIX HOUSE - www.voltrixhouse.com - Soporte: soporte@voltrixhouse.com")
                .setFont(regularFont).setFontSize(6).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        footer.add(companyInfo);

        return footer;
    }

    private String translateMethod(String method) {
        return switch (method) {
            case "CARD" -> "Tarjeta de crédito / débito";
            case "WALLET" -> "Billetera digital";
            case "BANK_TRANSFER" -> "Transferencia bancaria";
            default -> method;
        };
    }
}