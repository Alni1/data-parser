package com.parser.data_parser.repository;

import com.parser.data_parser.model.ParsedData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParsedDataRepository extends JpaRepository<ParsedData, Long> {
    // Добавляем метод для поиска
    List<ParsedData> findByTitleContaining(String title);

    List<ParsedData> findByTitleContainingIgnoreCase(String query);
}