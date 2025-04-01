package com.parser.data_parser.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyService {
    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public double getUsdRate() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        
        for (JsonNode node : root) {
            if ("USD".equals(node.get("ccy").asText())) {
                return node.get("sale").asDouble();
            }
        }
        throw new Exception("Курс USD не знайдено");
    }
}