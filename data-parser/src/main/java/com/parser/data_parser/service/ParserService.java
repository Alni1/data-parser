package com.parser.data_parser.service;

import com.parser.data_parser.model.ParsedData;
import com.parser.data_parser.repository.ParsedDataRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {
    @Autowired
    private ParsedDataRepository repository;

    public List<ParsedData> parseOlx(String searchQuery) throws IOException {
        String encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        String url = "https://www.olx.ua/d/uk/list/q-" + encodedQuery + "/";
        List<ParsedData> dataList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .referrer("https://www.olx.ua")
                    .timeout(30000)
                    .get();

            for (Element element : doc.select("div[data-cy='l-card']")) {
                ParsedData data = new ParsedData();

                // Заголовок
                Element titleElement = element.selectFirst("h6");
                if (titleElement == null) titleElement = element.selectFirst("h4");
                data.setTitle(titleElement != null ? titleElement.text().trim() : "Назва відсутня");

                // Цена
                Element priceElement = element.selectFirst("p");
                if (priceElement != null) {
                    data.setPrice(parsePrice(priceElement.text()));
                }

                // Ссылка
                Element linkElement = element.selectFirst("a[href*='/obyavlenie/']");
                if (linkElement != null) {
                    String href = linkElement.attr("href");
                    String fullUrl = href.startsWith("/") ? "https://www.olx.ua" + href : href;
                    data.setUrl(fullUrl);

                    // Получение изображения с самой страницы объявления
                    data.setImageUrl(getFirstImageFromPage(fullUrl));
                }

                dataList.add(data);
            }

            if (!dataList.isEmpty()) {
                repository.saveAll(dataList);
            }

        } catch (IOException e) {
            throw new IOException("Помилка парсингу OLX: " + e.getMessage(), e);
        }

        return dataList;
    }

    private Double parsePrice(String text) {
        if (text == null || text.isEmpty()) return null;
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getFirstImageFromPage(String pageUrl) {
        try {
            Document doc = Jsoup.connect(pageUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .referrer("https://www.olx.ua")
                    .timeout(30000)
                    .get();
            
            Element imgElement = doc.selectFirst("img");
            return imgElement != null ? imgElement.attr("src") : "/static/no_image.jpg";
        } catch (IOException e) {
            return "/static/no_image.jpg";
        }
    }
}
