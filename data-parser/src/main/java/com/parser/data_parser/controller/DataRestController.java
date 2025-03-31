package com.parser.data_parser.controller;

import com.parser.data_parser.model.ParsedData;
import com.parser.data_parser.repository.ParsedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataRestController {
    @Autowired
    private ParsedDataRepository repository;

    @GetMapping
    public List<ParsedData> getAllData() {
        return repository.findAll();
    }
}