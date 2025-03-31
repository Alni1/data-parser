package com.parser.data_parser.controller;

import com.parser.data_parser.model.ParsedData;
import com.parser.data_parser.repository.ParsedDataRepository;
import com.parser.data_parser.service.ExcelExportService;
import com.parser.data_parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
public class ExcelController {
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private ParsedDataRepository repository;
    @Autowired
    private ParserService parserService;

    @GetMapping("/export")
    public ResponseEntity<String> exportExcel(@RequestParam String searchQuery) {
        try {
            List<ParsedData> dataList = repository.findAll();
            System.out.println(dataList.get(1).getTitle());
            String fileName = excelExportService.exportToExcel(dataList);
            return ResponseEntity.ok("Файл збережено: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Помилка експорту: " + e.getMessage());
        }
    }
}