package com.backend.housing.infrastructure.pdf;

import com.backend.housing.application.dto.response.rentalcontracts.ContractResponse;
import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
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
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Component
public class RentalContractPdfGenerator {

    private static final DeviceRgb BLACK = new DeviceRgb(0, 0, 0);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(45, 45, 45);
    private static final DeviceRgb GRAY = new DeviceRgb(80, 80, 80);
    private static final DeviceRgb BORDER_GRAY = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb GREEN = new DeviceRgb(0, 100, 0);

    private static final float MARGIN_TOP = 72f;
    private static final float MARGIN_BOTTOM = 72f;
    private static final float MARGIN_LEFT = 72f;
    private static final float MARGIN_RIGHT = 72f;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));

    private PdfFont regularFont;
    private PdfFont boldFont;

    public byte[] generate(ContractResponse contract) {
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

        document.add(createHeader(contract));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createTitle());
        document.add(new Paragraph(" ").setMarginBottom(10));

        document.add(createCelebrationDate(contract));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createPartiesSection(contract));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createRecitalsSection(contract));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createPropertySection(contract));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createTermsSection(contract));
        document.add(new Paragraph(" ").setMarginBottom(15));

        document.add(createClausesSection(contract));
        document.add(new Paragraph(" ").setMarginBottom(20));

        document.add(createSignaturesSection(contract));

        document.close();
        return baos.toByteArray();
    }

    private Table createHeader(ContractResponse contract) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{100})).useAllAvailableWidth();

        String contractNumber = "CONTRATO DE ARRENDAMIENTO NÚMERO: " +
                (contract.contractId() != null ? contract.contractId().toString().substring(0, Math.min(12, contract.contractId().toString().length())).toUpperCase() : "N/A");

        Paragraph contractNumberPara = new Paragraph(contractNumber)
                .setFont(boldFont).setFontSize(10).setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerCell = new Cell().add(contractNumberPara)
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

        Paragraph mainTitle = new Paragraph("CONTRATO DE ARRENDAMIENTO")
                .setFont(boldFont).setFontSize(18).setFontColor(BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subTitle = new Paragraph("DE INMUEBLE URBANO")
                .setFont(regularFont).setFontSize(11).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        titleDiv.add(mainTitle);
        titleDiv.add(subTitle);
        titleDiv.setMarginBottom(5);

        return titleDiv;
    }

    private Div createCelebrationDate(ContractResponse contract) {
        Div dateDiv = new Div();

        String city = "Cartagena de Indias, D.T. y C.";
        String dateStr = contract.createdAt() != null ?
                contract.createdAt().format(DATETIME_FORMATTER) :
                LocalDate.now().format(DATETIME_FORMATTER);

        Paragraph datePara = new Paragraph("En la ciudad de " + city + ", a los " + dateStr)
                .setFont(regularFont).setFontSize(10).setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        dateDiv.add(datePara);
        dateDiv.setMarginBottom(5);

        return dateDiv;
    }

    private Div createPartiesSection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("COMPARECIENTES")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(5));

        Table partiesTable = new Table(UnitValue.createPercentArray(new float[]{48, 48})).useAllAvailableWidth();

        partiesTable.addCell(createPartyCell("ARRENDADOR", contract.ownerName(), contract.ownerCedula(), "Propietario del inmueble"));
        partiesTable.addCell(createPartyCell("ARRENDATARIO", contract.tenantName(), contract.tenantCedula(), "Arrendatario"));

        section.add(partiesTable);
        section.setMarginBottom(10);

        return section;
    }

    private Cell createPartyCell(String role, String name, String cedula, String description) {
        Div cellDiv = new Div();

        cellDiv.add(new Paragraph(role).setFont(boldFont).setFontSize(9).setFontColor(GRAY));
        cellDiv.add(new Paragraph(name.toUpperCase()).setFont(boldFont).setFontSize(11).setFontColor(BLACK).setMarginTop(3));
        cellDiv.add(new Paragraph(description).setFont(regularFont).setFontSize(8).setFontColor(GRAY).setMarginTop(2));

        if (cedula != null && !cedula.isEmpty()) {
            cellDiv.add(new Paragraph("Cédula: " + cedula).setFont(regularFont).setFontSize(8).setFontColor(DARK_GRAY).setMarginTop(5));
        } else {
            cellDiv.add(new Paragraph("Cédula: No registrada").setFont(regularFont).setFontSize(8).setFontColor(GRAY).setMarginTop(5));
        }

        return new Cell().add(cellDiv)
                .setBorder(new SolidBorder(BORDER_GRAY, 0.5f))
                .setPadding(10);
    }

    private Div createRecitalsSection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("EXPOSICIÓN")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(5));

        String ownerNameUpper = contract.ownerName() != null ? contract.ownerName().toUpperCase() : "ARRENDADOR";
        String tenantNameUpper = contract.tenantName() != null ? contract.tenantName().toUpperCase() : "ARRENDATARIO";
        String propertyTitle = contract.propertyTitle() != null ? contract.propertyTitle() : "el inmueble";

        String recitals =
                "PRIMERA. - " + ownerNameUpper + " (en adelante EL ARRENDADOR) es legítimo propietario del inmueble ubicado en " + propertyTitle +
                        ", con plena capacidad legal para celebrar el presente contrato.\n\n" +

                        "SEGUNDA. - " + tenantNameUpper + " (en adelante EL ARRENDATARIO) manifiesta su voluntad de tomar en arrendamiento el inmueble descrito, " +
                        "destinándolo exclusivamente para uso residencial, comprometiéndose a cumplir todas las obligaciones " +
                        "establecidas en la Ley 820 de 2003 y las cláusulas contenidas en este documento.\n\n" +

                        "TERCERA. - Las partes acuerdan regirse por las siguientes cláusulas, las cuales han sido revisadas " +
                        "y aceptadas en su totalidad, otorgándoles plena validez legal.";

        Paragraph recitalsPara = new Paragraph(recitals)
                .setFont(regularFont).setFontSize(9).setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.JUSTIFIED).setMultipliedLeading(1.4f);

        section.add(recitalsPara);
        section.setMarginBottom(10);

        return section;
    }

    private Div createPropertySection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("DESCRIPCIÓN DEL INMUEBLE")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(5));

        Table propertyTable = new Table(UnitValue.createPercentArray(new float[]{25, 75})).useAllAvailableWidth();
        propertyTable.setBorder(new SolidBorder(BORDER_GRAY, 0.5f));

        String propertyIdStr = contract.propertyId() != null ?
                contract.propertyId().toString().substring(0, Math.min(12, contract.propertyId().toString().length())).toUpperCase() : "N/A";

        addTableRow(propertyTable, "DIRECCIÓN:", contract.propertyTitle() != null ? contract.propertyTitle() : "No especificada");
        addTableRow(propertyTable, "MATRÍCULA:", propertyIdStr);
        addTableRow(propertyTable, "DESTINACIÓN ECONÓMICA:", "Residencial - Vivienda familiar");

        section.add(propertyTable);
        section.setMarginBottom(10);

        return section;
    }

    private void addTableRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(boldFont).setFontSize(9).setFontColor(DARK_GRAY))
                .setBorder(Border.NO_BORDER)
                .setPadding(8);
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(regularFont).setFontSize(9).setFontColor(BLACK))
                .setBorder(Border.NO_BORDER)
                .setPadding(8);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private Div createTermsSection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("TÉRMINOS DEL CONTRATO")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(5));

        Table termsTable = new Table(UnitValue.createPercentArray(new float[]{35, 65})).useAllAvailableWidth();
        termsTable.setBorder(new SolidBorder(BORDER_GRAY, 0.5f));

        String startDate = contract.startDate() != null ? contract.startDate().format(DATE_FORMATTER) : "No especificada";
        String endDate = contract.endDate() != null ? contract.endDate().format(DATE_FORMATTER) : "No especificada";
        String duration = calculateDuration(contract);
        String monthlyRent = formatCurrency(contract.periodRent());
        String paymentDay = getPaymentDayDescription(contract.startDate(), contract.paymentFrequency());
        String paymentFrequencyText = getPaymentFrequencyText(contract.paymentFrequency());

        addTableRow(termsTable, "FECHA DE INICIO:", startDate);
        addTableRow(termsTable, "FECHA DE TERMINACIÓN:", endDate);
        addTableRow(termsTable, "PLAZO DE DURACIÓN:", duration);
        addTableRow(termsTable, "CANON:", monthlyRent);
        addTableRow(termsTable, "FRECUENCIA DE PAGO:", paymentFrequencyText);
        addTableRow(termsTable, "DÍA/LIMITE DE PAGO:", paymentDay);
        addTableRow(termsTable, "FORMA DE PAGO:", "Transferencia bancaria");

        section.add(termsTable);
        section.setMarginBottom(10);

        return section;
    }

    private String getPaymentFrequencyText(PaymentFrequency frequency) {
        if (frequency == null) return "Mensual";
        return switch (frequency) {
            case MONTHLY -> "Mensual";
            case BIWEEKLY -> "Quincenal (cada 15 días)";
            case WEEKLY -> "Semanal (cada 7 días)";
        };
    }

    private String getPaymentDayDescription(LocalDate startDate, PaymentFrequency frequency) {
        if (startDate == null) return "Día a convenir entre las partes";
        if (frequency == null) return "Día " + startDate.getDayOfMonth() + " de cada mes";

        int dayOfMonth = startDate.getDayOfMonth();

        return switch (frequency) {
            case MONTHLY -> "Día " + dayOfMonth + " de cada mes";
            case BIWEEKLY -> "Cada 15 días, contados a partir del " + startDate.format(DATE_FORMATTER);
            case WEEKLY -> "Cada 7 días, contados a partir del " + startDate.format(DATE_FORMATTER);
        };
    }

    private Div createClausesSection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("CLÁUSULAS CONTRACTUALES")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(5));

        String monthlyRent = formatCurrency(contract.periodRent());
        String startDate = contract.startDate() != null ? contract.startDate().format(DATE_FORMATTER) : "la fecha pactada";
        String endDate = contract.endDate() != null ? contract.endDate().format(DATE_FORMATTER) : "la fecha pactada";
        String duration = calculateDuration(contract);
        String propertyAddress = contract.propertyTitle() != null ? contract.propertyTitle() : "el inmueble arrendado";
        String paymentRule = getPaymentRuleDescription(contract);
        String paymentFrequencyText = getPaymentFrequencyText(contract.paymentFrequency());
        String ownerNameUpper = contract.ownerName() != null ? contract.ownerName().toUpperCase() : "EL ARRENDADOR";
        String tenantNameUpper = contract.tenantName() != null ? contract.tenantName().toUpperCase() : "EL ARRENDATARIO";

        section.add(createClause("PRIMERA", "OBJETO DEL CONTRATO",
                "El presente contrato tiene por objeto el arrendamiento del inmueble ubicado en " + propertyAddress +
                        ", que " + ownerNameUpper + " (EL ARRENDADOR) entrega a " + tenantNameUpper + " (EL ARRENDATARIO) " +
                        "en perfectas condiciones de habitabilidad, para ser destinado exclusivamente como vivienda familiar."));

        section.add(createClause("SEGUNDA", "VIGENCIA",
                "El término de duración del presente contrato es de " + duration + ", contados a partir del " + startDate +
                        " hasta el " + endDate + ". El contrato entrará en vigencia a partir de la fecha de inicio pactada."));

        section.add(createClause("TERCERA", "CANON Y FORMA DE PAGO",
                "El canon de arrendamiento se fija en la suma de " + monthlyRent + ", que EL ARRENDATARIO pagará de manera " +
                        paymentFrequencyText.toLowerCase() + ". " + paymentRule + " El pago se realizará mediante transferencia bancaria " +
                        "a la cuenta designada por EL ARRENDADOR, y se considerará oportuno cuando se encuentre acreditado en ella."));

        section.add(createClauseWithList("CUARTA", "OBLIGACIONES DEL ARRENDATARIO",
                "Son obligaciones de " + tenantNameUpper + " (EL ARRENDATARIO):", new String[]{
                        "Pagar oportunamente el canon de arrendamiento en las fechas estipuladas",
                        "Destinar el inmueble exclusivamente para uso residencial",
                        "Mantener el inmueble en buen estado de conservación y aseo",
                        "No realizar modificaciones estructurales sin autorización escrita de " + ownerNameUpper,
                        "Responder por los daños y perjuicios causados al inmueble o sus instalaciones",
                        "Permitir las inspecciones de " + ownerNameUpper + " con previo aviso de veinticuatro (24) horas",
                        "No subarrendar ni ceder total o parcialmente el presente contrato"
                }));

        section.add(createClauseWithList("QUINTA", "OBLIGACIONES DEL ARRENDADOR",
                "Son obligaciones de " + ownerNameUpper + " (EL ARRENDADOR):", new String[]{
                        "Entregar el inmueble en condiciones óptimas de habitabilidad",
                        "Realizar las reparaciones locativas mayores que no sean imputables a " + tenantNameUpper,
                        "Garantizar el goce pacífico del inmueble durante la vigencia del contrato",
                        "Responder por los vicios ocultos de la propiedad",
                        "Pagar el impuesto predial y las expensas comunes de administración"
                }));

        section.add(createClause("SEXTA", "MORA Y TERMINACIÓN",
                "En caso de mora en el pago del canon de arrendamiento, se aplicará un interés de mora del uno punto cinco por ciento (1.5%) mensual " +
                        "sobre el valor adeudado. El incumplimiento en el pago de dos (2) obligaciones consecutivas dará lugar a la terminación inmediata del contrato."));

        section.add(createClause("SÉPTIMA", "CANCELACIÓN ANTICIPADA",
                "Cualquiera de las partes podrá dar por terminado el contrato de manera anticipada mediante comunicación escrita " +
                        "dirigida a la otra parte con una antelación mínima de treinta (30) días. En este caso, no habrá lugar a indemnización alguna."));

        section.add(createClause("OCTAVA", "SOLUCIÓN DE CONTROVERSIAS",
                "Las controversias que surjan con motivo del presente contrato se resolverán a través de conciliación extrajudicial " +
                        "ante la Cámara de Comercio de Cartagena. En caso de no llegar a un acuerdo, las partes se someterán a la jurisdicción " +
                        "ordinaria de los Jueces Civiles Municipales de Cartagena de Indias."));

        return section;
    }

    private String getPaymentRuleDescription(ContractResponse contract) {
        if (contract.startDate() == null) {
            return "El primer pago se realizará en la fecha de inicio del contrato, y los pagos sucesivos según la frecuencia pactada.";
        }

        LocalDate startDate = contract.startDate();
        String startDateStr = startDate.format(DATE_FORMATTER);

        if (contract.paymentFrequency() == null) {
            return "El primer pago se realizará en la fecha de inicio del contrato (" + startDateStr + "), y los pagos sucesivos el mismo día de cada mes subsiguiente.";
        }

        return switch (contract.paymentFrequency()) {
            case MONTHLY ->
                    "El primer pago se realizará en la fecha de inicio del contrato (" + startDateStr + "), " +
                            "y los pagos sucesivos el mismo día de cada mes subsiguiente.";
            case BIWEEKLY ->
                    "El primer pago se realizará en la fecha de inicio del contrato (" + startDateStr + "), " +
                            "y los pagos sucesivos cada quince (15) días contados a partir de dicha fecha.";
            case WEEKLY ->
                    "El primer pago se realizará en la fecha de inicio del contrato (" + startDateStr + "), " +
                            "y los pagos sucesivos cada siete (7) días contados a partir de dicha fecha.";
        };
    }

    private Div createClause(String number, String title, String content) {
        Div clause = new Div();
        clause.setMarginBottom(8);

        Paragraph header = new Paragraph("CLÁUSULA " + number + ". - " + title + ":")
                .setFont(boldFont).setFontSize(9).setFontColor(BLACK);

        Paragraph body = new Paragraph(content)
                .setFont(regularFont).setFontSize(9).setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.JUSTIFIED).setMultipliedLeading(1.4f)
                .setMarginLeft(15);

        clause.add(header);
        clause.add(body);

        return clause;
    }

    private Div createClauseWithList(String number, String title, String intro, String[] items) {
        Div clause = new Div();
        clause.setMarginBottom(8);

        Paragraph header = new Paragraph("CLÁUSULA " + number + ". - " + title + ":")
                .setFont(boldFont).setFontSize(9).setFontColor(BLACK);

        Paragraph introPara = new Paragraph(intro)
                .setFont(regularFont).setFontSize(9).setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        List list = new List()
                .setListSymbol("•")
                .setFont(regularFont)
                .setFontSize(9)
                .setFontColor(DARK_GRAY)
                .setMarginLeft(30);

        for (String item : items) {
            list.add(new ListItem(item));
        }

        clause.add(header);
        clause.add(introPara);
        clause.add(list);

        return clause;
    }

    private Div createSignaturesSection(ContractResponse contract) {
        Div section = new Div();

        Paragraph title = new Paragraph("ACEPTACIÓN")
                .setFont(boldFont).setFontSize(12).setFontColor(BLACK)
                .setBorderBottom(new SolidBorder(BLACK, 0.5f))
                .setPaddingBottom(5)
                .setTextAlignment(TextAlignment.CENTER);
        section.add(title);
        section.add(new Paragraph(" ").setMarginBottom(15));

        Table signaturesTable = new Table(UnitValue.createPercentArray(new float[]{48, 48})).useAllAvailableWidth();

        signaturesTable.addCell(createAcceptanceCell("EL ARRENDADOR", contract.ownerName(), contract.ownerCedula()));
        signaturesTable.addCell(createAcceptanceCell("EL ARRENDATARIO", contract.tenantName(), contract.tenantCedula()));

        section.add(signaturesTable);
        section.add(new Paragraph(" ").setMarginBottom(15));

        String dateStr = contract.createdAt() != null ?
                contract.createdAt().format(DATE_FORMATTER) :
                LocalDate.now().format(DATE_FORMATTER);

        Paragraph datePlace = new Paragraph("Cartagena de Indias, " + dateStr)
                .setFont(regularFont).setFontSize(9).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        section.add(datePlace);

        Paragraph electronicNote = new Paragraph("Documento generado electrónicamente - Válido sin firma autógrafa según Ley 527 de 1999")
                .setFont(regularFont).setFontSize(7).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        section.add(electronicNote);

        return section;
    }

    private Cell createAcceptanceCell(String role, String name, String cedula) {
        Div cellDiv = new Div();
        cellDiv.setMarginBottom(10);

        cellDiv.add(new Paragraph(role).setFont(boldFont).setFontSize(9).setFontColor(GRAY).setTextAlignment(TextAlignment.CENTER));
        cellDiv.add(new Paragraph(" ").setMarginBottom(10));

        cellDiv.add(new Paragraph(name != null ? name.toUpperCase() : "USUARIO").setFont(boldFont).setFontSize(10).setFontColor(BLACK).setTextAlignment(TextAlignment.CENTER));

        if (cedula != null && !cedula.isEmpty()) {
            cellDiv.add(new Paragraph("C.C. " + cedula).setFont(regularFont).setFontSize(8).setFontColor(DARK_GRAY).setTextAlignment(TextAlignment.CENTER));
        } else {
            cellDiv.add(new Paragraph("Cédula no registrada").setFont(regularFont).setFontSize(8).setFontColor(GRAY).setTextAlignment(TextAlignment.CENTER));
        }

        cellDiv.add(new Paragraph(" ").setMarginBottom(15));

        cellDiv.add(new Paragraph("✓ ACEPTACIÓN DIGITAL")
                .setFont(boldFont).setFontSize(8).setFontColor(GREEN)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5));

        return new Cell().add(cellDiv)
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
    }

    private String calculateDuration(ContractResponse contract) {
        if (contract.startDate() == null || contract.endDate() == null) {
            return "No especificado";
        }

        LocalDate start = contract.startDate();
        LocalDate end = contract.endDate();
        PaymentFrequency frequency = contract.paymentFrequency();

        long daysBetween = ChronoUnit.DAYS.between(start, end);

        if (frequency == null) {
            long months = ChronoUnit.MONTHS.between(start, end);
            return months == 1 ? "1 mes" : months + " meses";
        }

        return switch (frequency) {
            case MONTHLY -> {
                long months = ChronoUnit.MONTHS.between(start, end);
                yield months == 1 ? "1 mes" : months + " meses";
            }
            case BIWEEKLY -> {
                long quincenas = daysBetween / 15;
                yield quincenas + " quincenas (" + (quincenas / 2) + " meses)";
            }
            case WEEKLY -> {
                long semanas = daysBetween / 7;
                yield semanas + " semanas (" + (semanas / 4) + " meses)";
            }
        };
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "—";
        return "$ " + String.format(Locale.forLanguageTag("es-CO"), "%,.0f", amount) + " COP";
    }
}