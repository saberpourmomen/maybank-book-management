package com.maybank.book_management.service;

import com.maybank.book_management.config.ExternalApiProperties;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.dto.BookPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalBookService {

    private final RestTemplate restTemplate;
    private final ExternalApiProperties externalApiProperties;

    public BookResponse getByIsbn(String isbn) {
        String url = externalApiProperties.getBaseUrl() + "/isbn/" + isbn;
        log.info("calling external api getByIsbn URL: [{}]", url);
        return restTemplate.getForObject(url, BookResponse.class);
    }

    public ResponseEntity<BookPageResponse<BookResponse>> getByTitle(String title, int page, int size) {

        String url = externalApiProperties.getBaseUrl() + "/search";

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("title", title)
                .queryParam("page", page)
                .queryParam("size", size);

        log.info("calling external search api URL: [{}] title:[{}] page: [{}] size:[{}]", url,title,page,size);

        ResponseEntity<BookPageResponse<BookResponse>> response =
                restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BookPageResponse<BookResponse>>() {}
                );

        return ResponseEntity.ok(response.getBody());
    }

}
