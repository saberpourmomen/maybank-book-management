package com.maybank.book_management.service;

import com.maybank.book_management.config.ExternalApiProperties;
import com.maybank.book_management.config.WebClientConfiguration;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.dto.BookPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalBookService {

    private final WebClient bookServiceWebClient;

    public BookResponse getByIsbn(String isbn) {
        log.info("Calling external API getByIsbn for ISBN: {}", isbn);
        return bookServiceWebClient
                .get()
                .uri("/isbn/{isbn}", isbn)
                .retrieve()
                .bodyToMono(BookResponse.class)
                .block();
    }

    public BookPageResponse<BookResponse> getByTitle(String title, int page, int size) {
        log.info("Calling external API search for title: '{}' page: {} size: {}", title, page, size);

        return bookServiceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("title", title)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BookPageResponse<BookResponse>>() {})
                .block();
    }

}
