package com.parser.data_parser.controller;

import com.parser.data_parser.model.ParsedData;
import com.parser.data_parser.service.CurrencyService;
import com.parser.data_parser.service.ExcelExportService;
import com.parser.data_parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.List;

@Controller
public class ParserController {
    @Autowired
    private ParserService parserService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String searchQuery, Model model) {
        // Добавляем курс валют
        try {
            double usdRate = currencyService.getUsdRate();
            model.addAttribute("usdRate", usdRate);
        } catch (Exception e) {
            model.addAttribute("currencyError", "Не вдалося отримати курс USD");
        }
        
        if (searchQuery != null && !searchQuery.isEmpty()) {
            try {
                List<ParsedData> data = parserService.parseOlx(searchQuery);
                model.addAttribute("apartments", data);
            } catch (IOException e) {
                model.addAttribute("error", "Помилка парсингу: " + e.getMessage());
            }
        }
        return "index";
    }

    @GetMapping("/export-file")
    public String exportToFile(@RequestParam String searchQuery, Model model) {
        try {
            List<ParsedData> data = parserService.parseOlx(searchQuery);
            String fileName = excelExportService.exportToExcel(data);
            model.addAttribute("filename", fileName);
            model.addAttribute("success", "Файл успішно експортовано: " + fileName);
            model.addAttribute("apartments", data);
        } catch (IOException e) {
            model.addAttribute("error", "Помилка експорту: " + e.getMessage());
        }
        return "index";
    }
}