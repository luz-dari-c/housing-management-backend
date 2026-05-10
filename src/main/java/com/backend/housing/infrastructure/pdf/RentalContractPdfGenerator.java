package com.backend.housing.infrastructure.pdf;

import com.backend.housing.application.dto.response.rentalcontracts.ContractResponse;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
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
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
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

    // ==================== PALETA DE COLORES PREMIUM ====================
    private static final DeviceRgb NAVY_DEEP = new DeviceRgb(10, 22, 40);
    private static final DeviceRgb NAVY_PRIMARY = new DeviceRgb(11, 27, 48);
    private static final DeviceRgb GOLD_CLASSIC = new DeviceRgb(197, 160, 89);
    private static final DeviceRgb GOLD_SHINE = new DeviceRgb(212, 175, 55);
    private static final DeviceRgb STONE_GRAY = new DeviceRgb(74, 85, 104);
    private static final DeviceRgb IVORY_BG = new DeviceRgb(248, 246, 240);
    private static final DeviceRgb IVORY_BG_ALT = new DeviceRgb(245, 243, 237);
    private static final DeviceRgb TEXT_DARK = new DeviceRgb(28, 30, 36);
    private static final DeviceRgb TEXT_SOFT = new DeviceRgb(72, 78, 90);
    private static final DeviceRgb TEXT_LIGHT = new DeviceRgb(120, 126, 140);
    private static final DeviceRgb BORDER_GOLD_LIGHT = new DeviceRgb(212, 185, 110);
    private static final DeviceRgb BORDER_SUBTLE = new DeviceRgb(220, 218, 210);

    // Colores de estado
    private static final DeviceRgb STATUS_ACTIVE = new DeviceRgb(16, 110, 70);
    private static final DeviceRgb STATUS_PENDING = new DeviceRgb(217, 119, 6);
    private static final DeviceRgb STATUS_DANGER = new DeviceRgb(185, 28, 28);
    private static final DeviceRgb STATUS_MUTED = new DeviceRgb(100, 100, 110);

    // ==================== CONFIGURACIÓN DE MÁRGENES ====================
    private static final float MARGIN_TOP = 70f;
    private static final float MARGIN_BOTTOM = 60f;
    private static final float MARGIN_LEFT = 80f;
    private static final float MARGIN_RIGHT = 60f;
    private static final float INNER_MARGIN = 20f;
    private static final float SIDEBAR_GOLD_WIDTH = 1.5f;
    private static final float SIDEBAR_NAVY_WIDTH = 3f;

    // ==================== FORMATOS ====================
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy 'a las' HH:mm", new Locale("es", "CO"));

    private PdfFont titleFont;
    private PdfFont subtitleFont;
    private PdfFont bodyFont;
    private PdfFont lightFont;

    public byte[] generate(ContractResponse contract) {
        try {
            titleFont = PdfFontFactory.createFont("Times-Roman");
            subtitleFont = PdfFontFactory.createFont("Times-Bold");
            bodyFont = PdfFontFactory.createFont("Helvetica");
            lightFont = PdfFontFactory.createFont("Helvetica-Oblique");
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar las fuentes", e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(MARGIN_TOP, MARGIN_RIGHT + INNER_MARGIN, MARGIN_BOTTOM, MARGIN_LEFT + INNER_MARGIN);

        // Handlers
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new GoldSidebarHandler());
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new PremiumFooterHandler(contract, pdfDoc));
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new WatermarkHandler());

        // Encabezado
        createLuxuryHeader(document, contract);
        document.add(new Paragraph("\n"));

        // Título principal
        createMainTitle(document);
        document.add(new Paragraph("\n"));

        // Fecha
        createCelebrationDate(document, contract);

        // Secciones
        createPartiesSection(document, contract);
        document.add(new Paragraph("\n"));

        createExpositionSection(document, contract);
        document.add(new Paragraph("\n"));

        createPropertySection(document, contract);
        document.add(new Paragraph("\n"));

        createTermsSection(document, contract);
        document.add(new Paragraph("\n"));

        createStatusBadge(document, contract);
        document.add(new Paragraph("\n"));

        createClausesSection(document, contract);

        createDecorativeSeparator(document);

        createEnhancedSignatureSection(document, contract);

        document.add(new Paragraph("\n"));
        createLegalFooter(document, contract);

        document.close();
        return baos.toByteArray();
    }

    // ==================== MÉTODOS DE CONSTRUCCIÓN ====================

    private void createLuxuryHeader(Document document, ContractResponse contract) {
        Table topBorder = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        topBorder.addCell(new Cell()
                .setHeight(2)
                .setBackgroundColor(GOLD_CLASSIC)
                .setBorder(Border.NO_BORDER)
                .setPadding(0));
        document.add(topBorder);

        document.add(new Paragraph(" ").setMarginBottom(10));

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{60, 40})).useAllAvailableWidth();
        headerTable.setMarginBottom(8);

        Cell brandCell = new Cell()
                .add(new Paragraph("NESTLY").setFont(titleFont).setFontSize(28).setFontColor(NAVY_DEEP).setMargin(0))
                .add(new Paragraph("GESTIÓN INMOBILIARIA DE ALTA GAMA")
                        .setFont(lightFont).setFontSize(7).setFontColor(STONE_GRAY).setMargin(0))
                .setBorder(Border.NO_BORDER).setPadding(0);
        headerTable.addCell(brandCell);

        String contractNum = contract.getContractId().toString().substring(0, 12).toUpperCase();
        Cell certCell = new Cell()
                .add(new Paragraph("CONTRATO N°")
                        .setFont(lightFont).setFontSize(6).setFontColor(STONE_GRAY).setTextAlignment(TextAlignment.RIGHT))
                .add(new Paragraph(contractNum)
                        .setFont(subtitleFont).setFontSize(12).setFontColor(GOLD_CLASSIC).setTextAlignment(TextAlignment.RIGHT))
                .add(new Paragraph("CERTIFICADO DIGITAL")
                        .setFont(lightFont).setFontSize(5).setFontColor(GOLD_CLASSIC).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(new DashedBorder(GOLD_CLASSIC, 0.5f))
                .setPadding(8).setMargin(2)
                .setBackgroundColor(IVORY_BG);
        headerTable.addCell(certCell);
        document.add(headerTable);

        SolidLine thickGoldLine = new SolidLine(1.2f);
        thickGoldLine.setColor(GOLD_CLASSIC);
        LineSeparator goldLine = new LineSeparator(thickGoldLine);
        goldLine.setMarginBottom(3);
        document.add(goldLine);

        SolidLine thinDarkLine = new SolidLine(0.5f);
        thinDarkLine.setColor(NAVY_PRIMARY);
        LineSeparator darkLine = new LineSeparator(thinDarkLine);
        darkLine.setMarginBottom(16);
        document.add(darkLine);
    }

    private void createMainTitle(Document document) {
        Paragraph crownDeco = new Paragraph("✦ • ✦ • ✦")
                .setFont(lightFont).setFontSize(8).setFontColor(GOLD_CLASSIC)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(8);
        document.add(crownDeco);

        Paragraph mainTitle = new Paragraph("CONTRATO DE ARRENDAMIENTO")
                .setFont(titleFont).setFontSize(26).setFontColor(NAVY_DEEP)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6);
        document.add(mainTitle);

        Paragraph subTitle = new Paragraph("DE INMUEBLE URBANO - RÉGIMEN DE PROPIEDAD HORIZONTAL")
                .setFont(lightFont).setFontSize(9).setFontColor(STONE_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(subTitle);

        document.add(new Paragraph(" ").setMarginBottom(12));
    }

    private void createCelebrationDate(Document document, ContractResponse contract) {
        String city = "Cartagena de Indias, D.T. y C.";
        String dateStr = contract.getCreatedAt().format(DATE_FORMATTER);
        String celebrationDate = String.format("%s, %s", city, dateStr);

        Div dateBox = new Div();
        dateBox.add(new Paragraph(celebrationDate)
                .setFont(bodyFont).setFontSize(9).setFontColor(TEXT_SOFT)
                .setTextAlignment(TextAlignment.CENTER));
        dateBox.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 0.5f));
        dateBox.setBorderTop(new SolidBorder(GOLD_CLASSIC, 0.5f));
        dateBox.setPadding(8);
        dateBox.setWidth(UnitValue.createPercentValue(60)); // Este sí acepta UnitValue
        dateBox.setMarginBottom(25); // float directo
        dateBox.setMarginLeft(70); // Valor en puntos, ajusta según necesites
        dateBox.setMarginRight(70); // Valor en puntos, ajusta según necesites

        document.add(dateBox);
    }
    private void createPartiesSection(Document document, ContractResponse contract) {
        Div sectionDiv = new Div();
        sectionDiv.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        sectionDiv.setBackgroundColor(IVORY_BG);
        sectionDiv.setMarginBottom(22);
        sectionDiv.setPadding(0);

        Div headerDiv = new Div();
        headerDiv.add(new Paragraph("COMPARECIENTES")
                .setFont(subtitleFont).setFontSize(10).setFontColor(NAVY_DEEP));
        headerDiv.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1.5f));
        headerDiv.setPadding(12);
        headerDiv.setPaddingBottom(8);
        sectionDiv.add(headerDiv);

        Div contentDiv = new Div();
        contentDiv.setPadding(16);

        Table partiesTable = new Table(UnitValue.createPercentArray(new float[]{48, 48})).useAllAvailableWidth();
        partiesTable.setMarginBottom(10);

        partiesTable.addCell(createPremiumPartyCard("ARRENDADOR", contract.getOwnerName(), "Legítimo propietario"));
        partiesTable.addCell(createPremiumPartyCard("ARRENDATARIO", contract.getTenantName(), "Arrendatario responsable"));

        contentDiv.add(partiesTable);
        sectionDiv.add(contentDiv);
        document.add(sectionDiv);
    }

    private Cell createPremiumPartyCard(String role, String name, String subtitle) {
        Div card = new Div();
        card.setBorder(new SolidBorder(GOLD_CLASSIC, 0.8f));
        card.setBackgroundColor(IVORY_BG);
        card.setPadding(14);

        card.add(new Paragraph(role)
                .setFont(subtitleFont).setFontSize(8).setFontColor(GOLD_CLASSIC));
        card.add(new Paragraph(name)
                .setFont(titleFont).setFontSize(13).setFontColor(NAVY_DEEP)
                .setMarginTop(6).setMarginBottom(4));
        card.add(new Paragraph(subtitle)
                .setFont(lightFont).setFontSize(7).setFontColor(TEXT_SOFT));
        card.add(new Paragraph("C.C. _________________")
                .setFont(bodyFont).setFontSize(7).setFontColor(TEXT_LIGHT)
                .setMarginTop(8));

        return new Cell().add(card).setBorder(Border.NO_BORDER).setPadding(5);
    }

    private void createExpositionSection(Document document, ContractResponse contract) {
        Div sectionDiv = new Div();
        sectionDiv.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        sectionDiv.setBackgroundColor(IVORY_BG);
        sectionDiv.setMarginBottom(22);
        sectionDiv.setPadding(0);

        Div headerDiv = new Div();
        headerDiv.add(new Paragraph("EXPOSICIÓN")
                .setFont(subtitleFont).setFontSize(10).setFontColor(NAVY_DEEP));
        headerDiv.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1.5f));
        headerDiv.setPadding(12);
        headerDiv.setPaddingBottom(8);
        sectionDiv.add(headerDiv);

        Div contentDiv = new Div();
        contentDiv.setPadding(16);

        String exposition = "PRIMERA.- El ARRENDADOR es legítimo propietario del inmueble ubicado en " +
                contract.getPropertyTitle() + ", con plena capacidad para celebrar el presente contrato.\n\n" +
                "SEGUNDA.- El ARRENDATARIO manifiesta su voluntad de tomar en arrendamiento el inmueble descrito, " +
                "destinándolo exclusivamente para uso residencial, comprometiéndose a cumplir todas las " +
                "obligaciones establecidas en la Ley 820 de 2003 y las cláusulas contenidas en este documento.\n\n" +
                "TERCERA.- Las partes acuerdan regirse por las siguientes cláusulas, las cuales han sido revisadas " +
                "y aceptadas a través de la plataforma digital Nestly, otorgándoles plena validez legal conforme " +
                "a la Ley 527 de 1999 sobre mensajes de datos.";

        Paragraph expositionPara = new Paragraph(exposition)
                .setFont(bodyFont).setFontSize(9).setFontColor(TEXT_DARK)
                .setTextAlignment(TextAlignment.JUSTIFIED).setMultipliedLeading(1.5f);
        contentDiv.add(expositionPara);
        sectionDiv.add(contentDiv);
        document.add(sectionDiv);
    }

    private void createPropertySection(Document document, ContractResponse contract) {
        Div sectionDiv = new Div();
        sectionDiv.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        sectionDiv.setBackgroundColor(IVORY_BG);
        sectionDiv.setMarginBottom(22);
        sectionDiv.setPadding(0);

        Div headerDiv = new Div();
        headerDiv.add(new Paragraph("DETALLES DE LA PROPIEDAD")
                .setFont(subtitleFont).setFontSize(10).setFontColor(NAVY_DEEP));
        headerDiv.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1.5f));
        headerDiv.setPadding(12);
        headerDiv.setPaddingBottom(8);
        sectionDiv.add(headerDiv);

        Div contentDiv = new Div();
        contentDiv.setPadding(16);

        Table propertyTable = new Table(UnitValue.createPercentArray(new float[]{28, 72})).useAllAvailableWidth();
        propertyTable.setBorder(new SolidBorder(BORDER_SUBTLE, 1));

        addStyledTableRow(propertyTable, "DIRECCIÓN", contract.getPropertyTitle());
        addStyledTableRow(propertyTable, "ID PROPIEDAD", contract.getPropertyId().toString().substring(0, 12).toUpperCase());
        addStyledTableRow(propertyTable, "MATRÍCULA INMOBILIARIA", "NST-" + contract.getPropertyId().toString().substring(0, 8));
        addStyledTableRow(propertyTable, "DESTINACIÓN", "Residencial - Uso exclusivo vivienda");

        contentDiv.add(propertyTable);
        sectionDiv.add(contentDiv);
        document.add(sectionDiv);
    }

    private void addStyledTableRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(subtitleFont).setFontSize(8).setFontColor(STONE_GRAY))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(IVORY_BG_ALT)
                .setPadding(10);
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(bodyFont).setFontSize(9).setFontColor(NAVY_PRIMARY))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void createTermsSection(Document document, ContractResponse contract) {
        Div sectionDiv = new Div();
        sectionDiv.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        sectionDiv.setBackgroundColor(IVORY_BG);
        sectionDiv.setMarginBottom(22);
        sectionDiv.setPadding(0);

        Div headerDiv = new Div();
        headerDiv.add(new Paragraph("TÉRMINOS CONTRACTUALES")
                .setFont(subtitleFont).setFontSize(10).setFontColor(NAVY_DEEP));
        headerDiv.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1.5f));
        headerDiv.setPadding(12);
        headerDiv.setPaddingBottom(8);
        sectionDiv.add(headerDiv);

        Div contentDiv = new Div();
        contentDiv.setPadding(16);

        Table termsTable = new Table(UnitValue.createPercentArray(new float[]{32, 68})).useAllAvailableWidth();
        termsTable.setBorder(new SolidBorder(BORDER_SUBTLE, 1));

        String startDate = contract.getStartDate().format(DATE_FORMATTER);
        String endDate = contract.getEndDate().format(DATE_FORMATTER);
        String duration = calculateDuration(contract.getStartDate(), contract.getEndDate());
        String monthlyRent = formatCurrency(contract.getMonthlyRent());
        String createdAt = contract.getCreatedAt().format(DATETIME_FORMATTER);

        addStyledTableRow(termsTable, "FECHA DE INICIO", startDate);
        addStyledTableRow(termsTable, "FECHA DE TERMINACIÓN", endDate);
        addStyledTableRow(termsTable, "PLAZO DE DURACIÓN", duration);
        addStyledTableRow(termsTable, "CANON MENSUAL", monthlyRent);
        addStyledTableRow(termsTable, "DÍA DE PAGO", "Primeros 5 días hábiles de cada mes");
        addStyledTableRow(termsTable, "FORMA DE PAGO", "Transferencia bancaria / Plataforma Nestly");
        addStyledTableRow(termsTable, "FECHA DE SUSCRIPCIÓN", createdAt);

        contentDiv.add(termsTable);
        sectionDiv.add(contentDiv);
        document.add(sectionDiv);
    }

    private void createStatusBadge(Document document, ContractResponse contract) {
        String statusText = translateStatus(contract.getStatus().name());
        DeviceRgb statusColor = getStatusColor(contract.getStatus());

        Div badge = new Div();
        badge.add(new Paragraph(statusText)
                .setFont(subtitleFont).setFontSize(9).setFontColor(statusColor)
                .setTextAlignment(TextAlignment.CENTER));
        badge.setBorder(new SolidBorder(statusColor, 1));
        badge.setBackgroundColor(IVORY_BG); // Usar color fijo en lugar de calcular
        badge.setWidth(UnitValue.createPercentValue(25));
        badge.setMarginTop(15);
        badge.setMarginBottom(20);
        badge.setPadding(6);
        badge.setMarginLeft(37.5f);

        document.add(badge);
    }

    private void createClausesSection(Document document, ContractResponse contract) {
        Div sectionDiv = new Div();
        sectionDiv.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        sectionDiv.setBackgroundColor(IVORY_BG);
        sectionDiv.setMarginBottom(22);
        sectionDiv.setPadding(0);

        Div headerDiv = new Div();
        headerDiv.add(new Paragraph("CLÁUSULAS CONTRACTUALES")
                .setFont(subtitleFont).setFontSize(10).setFontColor(NAVY_DEEP));
        headerDiv.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1.5f));
        headerDiv.setPadding(12);
        headerDiv.setPaddingBottom(8);
        sectionDiv.add(headerDiv);

        Div contentDiv = new Div();
        contentDiv.setPadding(16);

        String monthlyRent = formatCurrency(contract.getMonthlyRent());
        String duration = calculateDuration(contract.getStartDate(), contract.getEndDate());
        String startDate = contract.getStartDate().format(DATE_FORMATTER);
        String endDate = contract.getEndDate().format(DATE_FORMATTER);
        String propertyAddress = contract.getPropertyTitle();

        contentDiv.add(createClauseCard("PRIMERA", "OBJETO DEL CONTRATO",
                "El presente contrato tiene por objeto el arrendamiento del inmueble ubicado en " + propertyAddress +
                        ", que el ARRENDADOR entrega al ARRENDATARIO en perfectas condiciones de habitabilidad y con todos los servicios " +
                        "públicos funcionando, para ser destinado exclusivamente como vivienda familiar."));

        contentDiv.add(createClauseCard("SEGUNDA", "VIGENCIA",
                "El término de duración del presente contrato es de " + duration + ", contados a partir del " + startDate +
                        " hasta el " + endDate + ". El contrato entrará en vigencia una vez se haya realizado el pago del primer canon " +
                        "de arrendamiento, momento en el cual la plataforma actualizará automáticamente el estado del contrato a ACTIVO."));

        contentDiv.add(createClauseCard("TERCERA", "CANON DE ARRENDAMIENTO",
                "El canon de arrendamiento se fija en la suma de " + monthlyRent + ", que el ARRENDATARIO pagará por mensualidades " +
                        "anticipadas dentro de los primeros cinco (5) días hábiles de cada mes, a través de los medios de pago habilitados " +
                        "en la plataforma Nestly. El pago se considerará oportuno cuando se encuentre registrado en el sistema antes de la " +
                        "fecha límite establecida."));

        contentDiv.add(createClauseCardWithList("CUARTA", "OBLIGACIONES DEL ARRENDATARIO",
                "Son obligaciones del ARRENDATARIO:", new String[]{
                        "Pagar oportunamente el canon de arrendamiento en las fechas estipuladas",
                        "Destinar el inmueble exclusivamente para uso residencial",
                        "Mantener el inmueble en buen estado de conservación y aseo",
                        "No realizar modificaciones estructurales sin autorización escrita del ARRENDADOR",
                        "Responder por los daños y perjuicios causados al inmueble o sus instalaciones",
                        "Permitir las inspecciones del ARRENDADOR con previo aviso de mínimo 24 horas",
                        "No subarrendar ni ceder total o parcialmente el presente contrato"
                }));

        contentDiv.add(createClauseCardWithList("QUINTA", "OBLIGACIONES DEL ARRENDADOR",
                "Son obligaciones del ARRENDADOR:", new String[]{
                        "Entregar el inmueble en condiciones óptimas de habitabilidad",
                        "Realizar las reparaciones locativas mayores que no sean imputables al ARRENDATARIO",
                        "Garantizar el goce pacífico del inmueble durante la vigencia del contrato",
                        "Responder por los vicios ocultos de la propiedad",
                        "Pagar el impuesto predial y las expensas comunes de administración"
                }));

        contentDiv.add(createClauseCard("SEXTA", "MORA Y TERMINACIÓN",
                "En caso de mora en el pago del canon de arrendamiento, se aplicará un interés de mora del 1.5% mensual " +
                        "sobre el valor adeudado. El incumplimiento en el pago de dos (2) mensualidades consecutivas dará lugar a la " +
                        "terminación inmediata del contrato y la plataforma marcará automáticamente el estado como EXPIRADO, " +
                        "liberando la propiedad sin necesidad de requerimiento judicial previo."));

        contentDiv.add(createClauseCard("SÉPTIMA", "CANCELACIÓN ANTICIPADA",
                "Cualquiera de las partes podrá solicitar la cancelación anticipada del contrato con un preaviso mínimo de treinta (30) días. " +
                        "Una vez solicitada, el contrato pasará a estado CANCELACIÓN PENDIENTE y seguirá generando obligaciones de pago hasta la " +
                        "fecha efectiva de cancelación. La cancelación anticipada sin justa causa podrá generar indemnizaciones conforme a lo " +
                        "dispuesto en la Ley 820 de 2003."));

        contentDiv.add(createClauseCard("OCTAVA", "SOLUCIÓN DE CONTROVERSIAS",
                "Las controversias que surjan con motivo del presente contrato se resolverán a través de la conciliación extrajudicial " +
                        "ante la Cámara de Comercio de Cartagena. En caso de no llegar a un acuerdo, las partes se someterán a la jurisdicción " +
                        "ordinaria de los Jueces Civiles Municipales de Cartagena de Indias, de conformidad con lo dispuesto en la Ley 820 de 2003."));

        sectionDiv.add(contentDiv);
        document.add(sectionDiv);
    }

    private Div createClauseCard(String number, String title, String content) {
        Div card = new Div();
        card.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        card.setMarginBottom(12);
        card.setPadding(0);

        Table clauseTable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();

        Cell headerCell = new Cell()
                .add(new Paragraph("CLÁUSULA " + number + " — " + title)
                        .setFont(subtitleFont).setFontSize(9).setFontColor(NAVY_DEEP))
                .setBorderBottom(new SolidBorder(GOLD_CLASSIC, 0.5f))
                .setBackgroundColor(IVORY_BG)
                .setPadding(10);
        clauseTable.addCell(headerCell);

        Cell contentCell = new Cell()
                .add(new Paragraph(content)
                        .setFont(bodyFont).setFontSize(9).setFontColor(TEXT_SOFT)
                        .setTextAlignment(TextAlignment.JUSTIFIED).setMultipliedLeading(1.5f))
                .setPadding(12);
        clauseTable.addCell(contentCell);

        card.add(clauseTable);
        return card;
    }

    private Div createClauseCardWithList(String number, String title, String intro, String[] items) {
        Div card = new Div();
        card.setBorder(new SolidBorder(BORDER_SUBTLE, 0.5f));
        card.setMarginBottom(12);

        Table clauseTable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();

        Cell headerCell = new Cell()
                .add(new Paragraph("CLÁUSULA " + number + " — " + title)
                        .setFont(subtitleFont).setFontSize(9).setFontColor(NAVY_DEEP))
                .setBorderBottom(new SolidBorder(GOLD_CLASSIC, 0.5f))
                .setBackgroundColor(IVORY_BG)
                .setPadding(10);
        clauseTable.addCell(headerCell);

        Div contentDiv = new Div();
        contentDiv.add(new Paragraph(intro)
                .setFont(bodyFont).setFontSize(9).setFontColor(TEXT_SOFT)
                .setMarginBottom(8));

        List list = new List();
        list.setListSymbol("▹ ");
        list.setFont(bodyFont);
        list.setFontSize(9);
        list.setFontColor(TEXT_SOFT);
        list.setMarginLeft(20);

        for (String item : items) {
            list.add(new ListItem(item));
        }
        contentDiv.add(list);

        Cell contentCell = new Cell().add(contentDiv).setPadding(12);
        clauseTable.addCell(contentCell);

        card.add(clauseTable);
        return card;
    }

    private void createDecorativeSeparator(Document document) {
        Div separator = new Div();
        separator.setMarginTop(20);
        separator.setMarginBottom(20);

        Paragraph sepLine = new Paragraph("◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈ ◈")
                .setFont(lightFont).setFontSize(6).setFontColor(GOLD_CLASSIC)
                .setTextAlignment(TextAlignment.CENTER);
        separator.add(sepLine);

        document.add(separator);
    }

    private void createEnhancedSignatureSection(Document document, ContractResponse contract) {
        Div sigTitle = new Div();
        sigTitle.add(new Paragraph("ACEPTACIÓN Y FIRMAS")
                .setFont(subtitleFont).setFontSize(11).setFontColor(NAVY_DEEP)
                .setTextAlignment(TextAlignment.CENTER));
        sigTitle.setBorderBottom(new SolidBorder(GOLD_CLASSIC, 1));
        sigTitle.setMarginBottom(20);
        sigTitle.setPaddingBottom(8);
        document.add(sigTitle);

        Table signaturesTable = new Table(UnitValue.createPercentArray(new float[]{48, 48})).useAllAvailableWidth();
        signaturesTable.setMarginBottom(15);

        signaturesTable.addCell(createPremiumSignatureBlock("ARRENDADOR", contract.getOwnerName()));
        signaturesTable.addCell(createPremiumSignatureBlock("ARRENDATARIO", contract.getTenantName()));

        document.add(signaturesTable);

        Table legalSeal = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        legalSeal.setMarginTop(10);

        Cell sealCell = new Cell()
                .add(new Paragraph("⬙ ESTE DOCUMENTO ES VÁLIDO LEGALMENTE ⬙")
                        .setFont(subtitleFont).setFontSize(7).setFontColor(STONE_GRAY)
                        .setTextAlignment(TextAlignment.CENTER))
                .add(new Paragraph("Verificación: NST-" + contract.getContractId().toString().substring(0, 8).toUpperCase())
                        .setFont(lightFont).setFontSize(6).setFontColor(TEXT_LIGHT)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(new DashedBorder(GOLD_CLASSIC, 0.5f))
                .setPadding(8)
                .setBackgroundColor(IVORY_BG);

        legalSeal.addCell(sealCell);
        document.add(legalSeal);
    }

    private Cell createPremiumSignatureBlock(String role, String name) {
        Div block = new Div();
        block.setBorder(new SolidBorder(BORDER_SUBTLE, 1));
        block.setBackgroundColor(IVORY_BG);
        block.setPadding(15);

        block.add(new Paragraph(role)
                .setFont(subtitleFont).setFontSize(8).setFontColor(GOLD_CLASSIC)
                .setTextAlignment(TextAlignment.CENTER));

        Div signatureLine = new Div();
        signatureLine.setBorderBottom(new SolidBorder(STONE_GRAY, 0.8f));
        signatureLine.setWidth(UnitValue.createPercentValue(90));
        signatureLine.setMarginTop(20); // float
        signatureLine.setMarginBottom(5); // float
        signatureLine.setMarginLeft(5); // float (5 puntos, no porcentaje)
        block.add(signatureLine);

        block.add(new Paragraph(name)
                .setFont(subtitleFont).setFontSize(9).setFontColor(NAVY_DEEP)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5));

        block.add(new Paragraph("Firma electrónica certificada")
                .setFont(lightFont).setFontSize(6).setFontColor(TEXT_LIGHT)
                .setTextAlignment(TextAlignment.CENTER));

        return new Cell().add(block).setBorder(Border.NO_BORDER).setPadding(8);
    }
    private void createLegalFooter(Document document, ContractResponse contract) {
        String verificationCode = "NST-VER-" + contract.getContractId().toString().substring(0, 8).toUpperCase();

        Div footer = new Div();
        footer.setBorderTop(new SolidBorder(GOLD_CLASSIC, 0.5f));
        footer.setMarginTop(15);
        footer.setPaddingTop(12);

        Paragraph legalText = new Paragraph("Documento electrónico generado automáticamente a través de la plataforma NESTLY. " +
                "Este contrato cumple con los requisitos establecidos en la Ley 527 de 1999 sobre mensajes de datos. " +
                "Código de verificación: " + verificationCode)
                .setFont(bodyFont).setFontSize(6).setFontColor(TEXT_LIGHT)
                .setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(1.3f);
        footer.add(legalText);

        document.add(footer);
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    private String calculateDuration(LocalDate start, LocalDate end) {
        long months = ChronoUnit.MONTHS.between(start, end);
        if (months == 0) return "Un mes";
        if (months == 1) return "Un (1) mes";
        if (months < 12) return months + " meses";
        long years = months / 12;
        long remainingMonths = months % 12;
        if (remainingMonths == 0) return years + (years == 1 ? " año" : " años");
        return years + (years == 1 ? " año" : " años") + " y " + remainingMonths + " meses";
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "—";
        return "$ " + String.format(Locale.forLanguageTag("es-CO"), "%,.0f", amount) + " COP";
    }

    private String translateStatus(String status) {
        switch (status) {
            case "PAYMENT_PENDING": return "PENDIENTE DE PAGO";
            case "ACTIVE": return "ACTIVO ✓";
            case "CANCELLED": return "CANCELADO";
            case "CANCELLATION_PENDING": return "CANCELACIÓN PENDIENTE";
            case "TERMINATED": return "TERMINADO";
            case "EXPIRED": return "EXPIRADO";
            default: return status;
        }
    }

    private DeviceRgb getStatusColor(ContractStatus status) {
        switch (status.name()) {
            case "ACTIVE": return STATUS_ACTIVE;
            case "PAYMENT_PENDING":
            case "CANCELLATION_PENDING": return STATUS_PENDING;
            case "EXPIRED":
            case "CANCELLED":
            case "TERMINATED": return STATUS_DANGER;
            default: return STATUS_MUTED;
        }
    }

    // ==================== HANDLERS DE EVENTOS ====================

    private static class GoldSidebarHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle pageSize = page.getPageSize();

            canvas.setFillColor(NAVY_PRIMARY)
                    .rectangle(0, 0, SIDEBAR_NAVY_WIDTH, pageSize.getHeight())
                    .fill();

            canvas.setFillColor(GOLD_CLASSIC)
                    .rectangle(SIDEBAR_NAVY_WIDTH, 0, SIDEBAR_GOLD_WIDTH, pageSize.getHeight())
                    .fill();

            canvas.release();
        }
    }

    private static class PremiumFooterHandler implements IEventHandler {
        private final ContractResponse contract;
        private final PdfDocument pdfDoc;

        public PremiumFooterHandler(ContractResponse contract, PdfDocument pdfDoc) {
            this.contract = contract;
            this.pdfDoc = pdfDoc;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            int pageNum = pdfDoc.getPageNumber(page);
            int totalPages = pdfDoc.getNumberOfPages();

            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle pageSize = page.getPageSize();

            try {
                float yPos = pageSize.getBottom() + MARGIN_BOTTOM - 30;

                if (pageNum % 2 != 0) {
                    canvas.beginText()
                            .setFontAndSize(PdfFontFactory.createFont("Helvetica"), 6)
                            .setColor(STONE_GRAY, true)
                            .moveTo(MARGIN_LEFT, yPos)
                            .showText("NESTLY GESTIÓN INMOBILIARIA S.A.S. | NIT: 901.234.567-8")
                            .moveTo(MARGIN_LEFT, yPos - 8)
                            .showText("Calle 35 # 4-85, Edificio Fontana, Oficina 702 - Cartagena")
                            .moveTo(MARGIN_LEFT, yPos - 16)
                            .showText("Tel: +57 (605) 678 9012 | Email: legal@nestly.com.co")
                            .endText();
                } else {
                    canvas.beginText()
                            .setFontAndSize(PdfFontFactory.createFont("Helvetica-Oblique"), 5)
                            .setColor(TEXT_LIGHT, true)
                            .moveTo(MARGIN_LEFT, yPos)
                            .showText("CONFIDENCIAL: Este documento contiene información legalmente vinculante.")
                            .moveTo(MARGIN_LEFT, yPos - 8)
                            .showText("Código de verificación: NST-" + contract.getContractId().toString().substring(0, 8))
                            .endText();
                }

                float pageWidth = pageSize.getWidth();
                canvas.beginText()
                        .setFontAndSize(PdfFontFactory.createFont("Helvetica"), 7)
                        .setColor(GOLD_CLASSIC, true)
                        .moveTo(pageWidth / 2 - 30, yPos)
                        .showText("Página " + pageNum + " de " + totalPages)
                        .endText();

            } catch (IOException e) {
                throw new RuntimeException("Error al crear el footer", e);
            }

            canvas.release();
        }
    }

    private static class WatermarkHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle pageSize = page.getPageSize();

            try {
                PdfExtGState gState = new PdfExtGState();
                gState.setFillOpacity(0.03f);

                canvas.saveState()
                        .setExtGState(gState)
                        .setFillColor(NAVY_PRIMARY)
                        .beginText()
                        .setFontAndSize(PdfFontFactory.createFont("Times-Bold"), 70)
                        .moveTo(pageSize.getWidth() / 2 - 70, pageSize.getHeight() / 2 - 30)
                        .showText("NESTLY")
                        .endText()
                        .restoreState();
            } catch (IOException e) {
                // Silently fail for watermark
            }

            canvas.release();
        }
    }
}