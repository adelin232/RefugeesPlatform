package com.project.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.models.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Service
@Slf4j
public class NewsService {

    private static final String[] sources = {"bbc-news", "cnn", "cbs-news", "google-news", "the-washington-post", "the-huffington-post", "independent"};

//    private static final String api_key = "28af95936b6e49b282989b1382346c28";
    private static final String api_key = "d5d4f81c17444ab69afb8018718729e6";

    @Getter
    @Setter
    private static class Article {
        JsonNode source;
        String author;
        String title;
        String description;
        String url;
        String urlToImage;
        String publishedAt;
        String content;
    }
    @Getter
    @Setter
    private static class RequestResult {
        String status;
        String totalResults;
        JsonNode articles;
    }

    public List<News> getNews(String topic) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        List<News> news_list = new ArrayList<>();

        for(String source: sources) {
            String request_url = "https://newsapi.org/v2/top-headlines?apiKey="+api_key+"&q="+topic+"&sources="+source;
            String json_string = restTemplate.getForObject(request_url, String.class);

            try {
                RequestResult request_result = mapper.readValue(json_string, RequestResult.class);
                log.info("Received " + request_result.getTotalResults() + " news articles from news-api.org");
                if (request_result.getArticles().isArray()) {
                    for (final JsonNode objNode : request_result.getArticles()) {
                        Article article = mapper.convertValue(objNode, Article.class);
                        news_list.add(new News(article.source.get("name").asText(), article.author, article.title, article.description, article.url, article.urlToImage, article.publishedAt));
                    }
                }
                else {
                    Article article = mapper.convertValue(request_result.getArticles(), Article.class);
                    news_list.add(new News(article.source.get("name").asText(), article.author, article.title, article.description, article.url, article.urlToImage, article.publishedAt));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                continue;
            }
        }


        return news_list;
    }
}
