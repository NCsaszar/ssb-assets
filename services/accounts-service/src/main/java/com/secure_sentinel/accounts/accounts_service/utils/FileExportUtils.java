package com.secure_sentinel.accounts.accounts_service.utils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
import com.secure_sentinel.accounts.accounts_service.model.AccountProgram;
import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileExportUtils {
    public static byte[] exportToCSV(List<TransactionDTO> transactions) throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Account", "Type", "Amount"
                , "Date"));
        if (transactions.isEmpty()) {
            csvPrinter.printRecord("No transactions found for the specified period");
        } else {
            for (TransactionDTO transaction : transactions) {
                csvPrinter.printRecord(transaction.getTransactionId(), transaction.getAccountNumber(),
                        transaction.getTransactionType(), transaction.getAmount(), transaction.getDateTime());
            }
        }
        csvPrinter.flush();
        return writer.toString().getBytes();
    }

    public static byte[] exportToPDF(List<TransactionDTO> transactions, AccountType accountType,
                                     AccountProgram accountProgram) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        DecimalFormat df = new DecimalFormat("#.00");
        PdfFont calibri;
        PdfFont calibriBold;

        try (InputStream calibriStream = FileExportUtils.class.getClassLoader().getResourceAsStream("calibri.ttf");
             InputStream calibriBoldStream = FileExportUtils.class.getClassLoader().getResourceAsStream("calibrib" +
                     ".ttf")) {

            if (calibriStream == null || calibriBoldStream == null) {
                throw new IOException("Font files not found in resources");
            }

            calibri = PdfFontFactory.createFont(calibriStream.readAllBytes(),
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            calibriBold = PdfFontFactory.createFont(calibriBoldStream.readAllBytes(),
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        }
        document.setFont(calibri);

        try (InputStream logoStream = FileExportUtils.class.getClassLoader().getResourceAsStream("logo.png")) {
            if (logoStream == null) {
                throw new IOException("Logo image not found in resources");
            }

            ImageData imageData = ImageDataFactory.create(logoStream.readAllBytes());
            Image logo = new Image(imageData);
            logo.setWidth(UnitValue.createPercentValue(50));

            // Bank name
            Paragraph bankName = new Paragraph("Secure Sentinel Bank")
                    .setFont(calibriBold)
                    .setFontSize(25)
                    .setFontColor(new DeviceRgb(222, 191, 99)) // #debf63
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(-30);

            // Header
            float[] columnWidths = {1, 3, 3};

            Table headerTable = new Table(UnitValue.createPercentArray(columnWidths));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            Cell logoCell =
                    new Cell().add(logo).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.TOP);
            logoCell.setPaddingRight(0);
            Cell nameCell =
                    new Cell().add(bankName).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.TOP);
            nameCell.setPaddingLeft(0);
            nameCell.setPaddingRight(0);
            Cell metadataCell;
            if (transactions.isEmpty()) {
                metadataCell = new Cell().add(new Paragraph("Account Number: " + "XXXX" +
                                "\n" +
                                "Account Type: " + accountType + "\n" +
                                "Account Program: " + accountProgram.getProgramName() + "\n" +
                                "Name: " + "Dante Martin" + "\n" +
                                "Address: " + "123 Main Street").setFont(calibri))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
            } else {
                metadataCell =
                        new Cell().add(new Paragraph("Account Number: " + transactions.get(0).getAccountNumber() +
                                        "\n" +
                                        "Account Type: " + accountType + "\n" +
                                        "Account Program: " + accountProgram.getProgramName() + "\n" +
                                        "Name: " + "Dante Martin" + "\n" +
                                        "Address: " + "123 Main Street").setFont(calibri))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setVerticalAlignment(VerticalAlignment.MIDDLE);
            }


            headerTable.addCell(logoCell);
            headerTable.addCell(nameCell);
            headerTable.addCell(metadataCell);
            document.add(headerTable);

            // line separator
            SolidLine solidLine = new SolidLine();
            solidLine.setColor(ColorConstants.BLACK);
            LineSeparator lineSeparator = new LineSeparator(solidLine);
            lineSeparator.setMarginTop(10);
            lineSeparator.setMarginBottom(10);

            document.add(lineSeparator);

            // table
            float[] columnWidthsTransactions = {2, 3, 4, 3, 3, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidthsTransactions));
            table.setWidth(UnitValue.createPercentValue(100));


            table.addHeaderCell(createHeaderCell("Date"));
            table.addHeaderCell(createHeaderCell("Type"));
            table.addHeaderCell(createHeaderCell("Description"));
            table.addHeaderCell(createHeaderCell("Credit"));
            table.addHeaderCell(createHeaderCell("Debit"));
            table.addHeaderCell(createHeaderCell("Closing Balance"));


            // Date formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

            // table data
            boolean isEvenRow = false;
            if (transactions.isEmpty()) {
                document.add(new Paragraph("No transactions found for the specified period").setFont(calibri).setFontSize(12));
            } else {
                for (TransactionDTO transaction : transactions) {
                    Color backgroundColor = isEvenRow ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE;
                    table.addCell(createCell(transaction.getDateTime().toLocalDateTime().format(formatter),
                            backgroundColor));
                    table.addCell(createCell(transaction.getTransactionType().toString(), backgroundColor));
                    table.addCell(createCell(transaction.getDescription(), backgroundColor));
                    if (transaction.getIsCredit()) {
                        table.addCell(createCell("-" + transaction.getAmount().toString(), backgroundColor));
                        table.addCell(createCell("", backgroundColor));
                    } else {
                        table.addCell(createCell("", backgroundColor));
                        table.addCell(createCell(transaction.getAmount().toString(), backgroundColor));
                    }
                    table.addCell(createCell(df.format(transaction.getClosingBalance()), backgroundColor));
                    isEvenRow = !isEvenRow;
                }

                document.add(table);
            }

            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("Error generating PDF file: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to generate PDF file", e);
        }
    }


    public static byte[] exportToExcel(List<TransactionDTO> transactions) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if (transactions.isEmpty()) {
                Sheet sheet = workbook.createSheet("Transactions");
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("No transactions found for the specified period");
            } else {
                SXSSFSheet sheet = workbook.createSheet("Transactions");
                sheet.trackAllColumnsForAutoSizing();
                createHeaderRow(sheet);
                fillDataRows(transactions, sheet);
                autoSizeColumns(sheet, 5);
            }

            workbook.write(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("Error generating Excel file: " + e);
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }


    private static void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Account", "Type", "Amount", "Date"};
        for (int i = 0; i < columnHeaders.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
    }

    private static void fillDataRows(List<TransactionDTO> transactions, Sheet sheet) {
        int rowIndex = 1;
        for (TransactionDTO transaction : transactions) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(transaction.getTransactionId());
            row.createCell(1).setCellValue(transaction.getAccountNumber());
            row.createCell(2).setCellValue(transaction.getTransactionType().toString());
            row.createCell(3).setCellValue(transaction.getAmount());
            row.createCell(4).setCellValue(transaction.getDateTime().toString());
        }
    }

    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static Cell createHeaderCell(String content) {
        return new Cell().add(new Paragraph(content))
                .setBackgroundColor(new DeviceRgb(222, 191, 99))
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell createCell(String content, Color backgroundColor) {
        return new Cell().add(new Paragraph(content).setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(backgroundColor)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderBottom(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER);
    }

    private static Cell createMetadataCell(String content, PdfFont font) {
        return new Cell().add(new Paragraph(content).setFont(font))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }
}