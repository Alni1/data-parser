package com.parser.data_parser.controller;

import com.parser.data_parser.model.ParsedData;
import com.parser.data_parser.repository.ParsedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private ParsedDataRepository repository;

    @GetMapping("/apartments")
    public List<ParsedData> getAllApartments() {
        return repository.findAll();
    }

    @GetMapping("/apartments/search")
    public List<ParsedData> searchApartments(@RequestParam String query) {
        return repository.findByTitleContainingIgnoreCase(query);
    }
}