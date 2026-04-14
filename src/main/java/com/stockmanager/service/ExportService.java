package com.stockmanager.service;

import com.itextpdf.text.Font;
import com.stockmanager.util.DateUtils;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;

import com.stockmanager.model.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.stockmanager.model.StockMovement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExportService {

    private final ProductService productService;
    private final StockMovementService stockMovementService;

    public void exportProductsToExcel(String filePath) throws IOException {
        List<Product> products = productService.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Produits");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nom");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Prix achat");
        header.createCell(4).setCellValue("Prix vente");
        header.createCell(5).setCellValue("Stock");
        header.createCell(6).setCellValue("Seuil alerte");
        header.createCell(7).setCellValue("Catégorie");

        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDescription());
            row.createCell(3).setCellValue(product.getPurchasePrice());
            row.createCell(4).setCellValue(product.getSalePrice());
            row.createCell(5).setCellValue(product.getStockQuantity());
            row.createCell(6).setCellValue(product.getAlertThreshold());
            row.createCell(7).setCellValue(
                    product.getCategory() != null ? product.getCategory().getName() : ""
            );
        }

        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public void exportMovementsToPdf(String filePath) throws IOException {
        List<StockMovement> movements = stockMovementService.findAll();

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Mouvements de stock", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("Produit");
            table.addCell("Type");
            table.addCell("Quantité");
            table.addCell("Date");
            table.addCell("Raison");

            for (StockMovement movement : movements) {
                table.addCell(movement.getProduct() != null ? movement.getProduct().getName() : "");
                table.addCell(movement.getType() != null ? movement.getType().getLabel() : "");
                table.addCell(String.valueOf(movement.getQuantity()));
                table.addCell(DateUtils.format(movement.getDate()));
                table.addCell(movement.getReason() != null ? movement.getReason() : "");
            }

            document.add(table);
        } catch (DocumentException e) {
            throw new IOException("Erreur lors de la création du PDF", e);
        } finally {
            document.close();
        }
    }
}
