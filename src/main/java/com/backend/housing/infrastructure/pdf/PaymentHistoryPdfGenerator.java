package com.backend.housing.infrastructure.pdf;

import com.backend.housing.application.dto.response.payments.PaymentHistoryResponse;
import com.backend.housing.application.dto.response.payments.PaymentHistoryItem;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class PaymentHistoryPdfGenerator {

    private static final DeviceRgb BLACK = new DeviceRgb(0, 0, 0);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(45, 45, 45);
    private static final DeviceRgb GRAY = new DeviceRgb(80, 80, 80);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(245, 245, 245);
    private static final DeviceRgb BORDER_GRAY = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb SUCCESS_GREEN = new DeviceRgb(34, 139, 34);
    private static final DeviceRgb PENDING_ORANGE = new DeviceRgb(255, 165, 0);
    private static final DeviceRgb FAILED_RED = new DeviceRgb(220, 53, 69);

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

    public byte[] generate(PaymentHistoryResponse history) {
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

        document.add(createHeader());
        document.add(new Paragraph(" ").setMarginBottom(10));

        document.add(createTitle(history));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createContractInfoSection(history));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createPaymentsTable(history));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createSummarySection(history));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createLegalFooter());

        document.close();
        return baos.toByteArray();
    }

    private Table createHeader() {
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

    private Div createTitle(PaymentHistoryResponse history) {
        Div titleDiv = new Div();

        Paragraph mainTitle = new Paragraph("HISTORIAL DE PAGOS")
                .setFont(boldFont).setFontSize(18).setFontColor(BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph contractRef = new Paragraph("Contrato: " + history.contractId().toString().substring(0, 12).toUpperCase())
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        titleDiv.add(mainTitle);
        titleDiv.add(contractRef);
        titleDiv.setMarginBottom(5);

        return titleDiv;
    }

    private Table createContractInfoSection(PaymentHistoryResponse history) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        infoTable.setBorder(new SolidBorder(BORDER_GRAY, 1));

        String nextPaymentDate = history.nextPaymentDueDate() != null
                ? history.nextPaymentDueDate().format(DATE_FORMATTER)
                : "No hay pagos pendientes";

        String monthlyRentFormatted = formatCurrency(history.monthlyRent());

        Cell infoCell = new Cell()
                .add(new Paragraph("PROPIEDAD: " + history.propertyTitle())
                        .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY))
                .add(new Paragraph("CANON MENSUAL: " + monthlyRentFormatted)
                        .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY))
                .add(new Paragraph("PRÓXIMO PAGO: " + nextPaymentDate)
                        .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY))
                .setPadding(12);
        infoTable.addCell(infoCell);

        return infoTable;
    }

    private Table createPaymentsTable(PaymentHistoryResponse history) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 20, 15, 25, 25}));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setBorder(new SolidBorder(BORDER_GRAY, 1));

        String[] headers = {"PERÍODO", "FECHA DE PAGO", "HORA", "MONTO", "ESTADO"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(9).setFontColor(BLACK))
                    .setBackgroundColor(LIGHT_GRAY)
                    .setBorderBottom(new SolidBorder(BORDER_GRAY, 1))
                    .setPadding(8);
            table.addCell(headerCell);
        }

        List<PaymentHistoryItem> payments = history.payments();
        if (payments != null && !payments.isEmpty()) {
            for (PaymentHistoryItem payment : payments) {
                String period = formatPeriod(payment.period());
                String date = payment.paidAt() != null ? payment.paidAt().format(DATE_FORMATTER) : "—";
                String time = payment.paidAt() != null ? payment.paidAt().format(TIME_FORMATTER) : "—";
                String amount = formatCurrency(payment.amount());
                String status = translateStatus(payment.status());
                DeviceRgb statusColor = getStatusColor(payment.status());

                table.addCell(new Cell().add(new Paragraph(period).setFont(regularFont).setFontSize(9)).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(date).setFont(regularFont).setFontSize(9)).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(time).setFont(regularFont).setFontSize(9)).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(amount).setFont(regularFont).setFontSize(9)).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(status).setFont(boldFont).setFontSize(9).setFontColor(statusColor)).setPadding(6));
            }
        } else {
            Cell emptyCell = new Cell(1, 5)
                    .add(new Paragraph("No hay pagos registrados para este contrato")
                            .setFont(regularFont).setFontSize(9).setFontColor(GRAY))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(20);
            table.addCell(emptyCell);
        }

        return table;
    }

    private Div createSummarySection(PaymentHistoryResponse history) {
        Div summaryDiv = new Div();

        Paragraph title = new Paragraph("RESUMEN")
                .setFont(boldFont).setFontSize(11).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        summaryDiv.add(title);
        summaryDiv.add(new Paragraph(" ").setMarginBottom(5));

        List<PaymentHistoryItem> payments = history.payments();
        int totalPayments = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        int succeededCount = 0;

        if (payments != null) {
            totalPayments = payments.size();
            for (PaymentHistoryItem payment : payments) {
                totalAmount = totalAmount.add(payment.amount());
                if ("SUCCEEDED".equals(payment.status())) {
                    succeededCount++;
                }
            }
        }

        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
        summaryTable.setBorder(new SolidBorder(BORDER_GRAY, 1));

        addSummaryRow(summaryTable, "TOTAL DE PAGOS REALIZADOS:", String.valueOf(succeededCount));
        addSummaryRow(summaryTable, "TOTAL DE TRANSACCIONES:", String.valueOf(totalPayments));
        addSummaryRow(summaryTable, "MONTO TOTAL PAGADO:", formatCurrency(totalAmount));

        summaryDiv.add(summaryTable);

        return summaryDiv;
    }

    private void addSummaryRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(boldFont).setFontSize(9).setFontColor(DARK_GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(regularFont).setFontSize(9).setFontColor(BLACK))
                .setBorder(Border.NO_BORDER)
                .setPadding(10)
                .setTextAlignment(TextAlignment.RIGHT);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private Div createLegalFooter() {
        Div footer = new Div();
        footer.setBorderTop(new SolidBorder(BORDER_GRAY, 0.5f));
        footer.setMarginTop(20);
        footer.setPaddingTop(10);

        Paragraph legalText = new Paragraph(
                "Este documento es un historial de pagos oficial. " +
                        "Conserve este documento como respaldo de sus transacciones.")
                .setFont(regularFont).setFontSize(7).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(1.3f);
        footer.add(legalText);

        Paragraph companyInfo = new Paragraph("VOLTRIX HOUSE - www.voltrixhouse.com - Soporte: soporte@voltrixhouse.com")
                .setFont(regularFont).setFontSize(6).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        footer.add(companyInfo);

        return footer;
    }

    private String formatPeriod(String period) {
        if (period == null || period.isEmpty()) {
            return "—";
        }

        if (period.matches("\\d{4}-\\d{2}")) {
            String[] parts = period.split("-");
            String monthName = getMonthName(parts[1]);
            return monthName + " " + parts[0];
        }

        if (period.contains("-W")) {
            String[] parts = period.split("-W");
            int quincenaNumber = Integer.parseInt(parts[1]);
            int mesNumero = ((quincenaNumber - 1) / 2) + 1;
            boolean esPrimeraQuincena = quincenaNumber % 2 != 0;
            String mesNombre = getMonthName(String.format("%02d", mesNumero));
            String tipoQuincena = esPrimeraQuincena ? "Primera quincena" : "Segunda quincena";
            return tipoQuincena + " de " + mesNombre + " " + parts[0];
        }

        if (period.contains("_semana") || period.contains("_week")) {
            String date = period.replace("_semana", "").replace("_week", "");
            try {
                LocalDate localDate = LocalDate.parse(date);
                return "Semana del " + localDate.format(DATE_FORMATTER);
            } catch (Exception e) {
                return "Semana del " + date;
            }
        }

        return period;
    }

    private String getMonthName(String month) {
        return switch (month) {
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
    }

    private String translateStatus(String status) {
        if (status == null) return "DESCONOCIDO";
        return switch (status) {
            case "SUCCEEDED" -> "PAGADO";
            case "PENDING" -> "PENDIENTE";
            case "FAILED" -> "FALLIDO";
            default -> status;
        };
    }

    private DeviceRgb getStatusColor(String status) {
        if (status == null) return GRAY;
        return switch (status) {
            case "SUCCEEDED" -> SUCCESS_GREEN;
            case "PENDING" -> PENDING_ORANGE;
            case "FAILED" -> FAILED_RED;
            default -> GRAY;
        };
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "—";
        return "$ " + String.format(Locale.forLanguageTag("es-CO"), "%,.0f", amount) + " COP";
    }
}