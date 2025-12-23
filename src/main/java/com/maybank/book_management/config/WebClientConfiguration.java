package com.maybank.book_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final ExternalApiProperties externalApiProperties;

    @Bean
    public WebClient bookServiceWebClient(WebClient.Builder builder) {

        ExternalApiProperties.BookService bookService = externalApiProperties.getBookService();

        return builder
                .baseUrl(bookService.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofSeconds(bookService.getTimeout()))
                ))
                .build();
    }
}
