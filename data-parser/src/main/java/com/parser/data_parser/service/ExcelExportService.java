package com.parser.data_parser.service;

import com.parser.data_parser.model.ParsedData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {
    public String exportToExcel(List<ParsedData> dataList) throws IOException {
        String dir = "./data-parser/exports/";
        String fileName = "apartments_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        Path filePath = Paths.get(dir).resolve(fileName).normalize();
        
        Files.createDirectories(Paths.get(dir));
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Оголошення");
            
            // Заголовки
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Заголовок");
            headerRow.createCell(2).setCellValue("Ціна (грн)");
            headerRow.createCell(3).setCellValue("Посилання");
            headerRow.createCell(4).setCellValue("Фото");
            
            // Данные
            int rowNum = 1;
            for (ParsedData data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.getId());
                row.createCell(1).setCellValue(data.getTitle());
                
                Cell priceCell = row.createCell(2);
                if (data.getPrice() != null) {
                    priceCell.setCellValue(data.getPrice());
                } else {
                    priceCell.setCellValue("N/A");
                }
                
                row.createCell(3).setCellValue(data.getUrl() != null ? data.getUrl() : "");
                row.createCell(4).setCellValue(data.getImageUrl());
            }
            
            // Авторазмер колонок
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Сохранение
            try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                System.out.println("messsage");
                workbook.write(out);
            }
            workbook.close();
        }
        return fileName;
    }
}