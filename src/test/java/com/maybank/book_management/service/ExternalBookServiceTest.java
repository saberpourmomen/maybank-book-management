package com.maybank.book_management.service;

import com.maybank.book_management.dto.BookResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class ExternalBookServiceTest {
    private ExternalBookService externalBookService;

    @Test
    void testGetByIsbn() {
        String isbn = "1234567890";

        BookResponse mockResponse = new BookResponse();
        mockResponse.setIsbn(isbn);
        mockResponse.setTitle("Test Book");
        mockResponse.setAuthor("Test Author");

        // Mock HTTP response
        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("""
                        {
                          "isbn":"1234567890",
                          "title":"Test Book",
                          "author":"Test Author"
                        }
                        """)
                .build();

        ExchangeFunction exchangeFunction = request -> Mono.just(clientResponse);

        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        externalBookService = new ExternalBookService(webClient);

        BookResponse response = externalBookService.getByIsbn(isbn);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Test Book", response.getTitle());
        Assertions.assertEquals("Test Author", response.getAuthor());
        Assertions.assertEquals(isbn, response.getIsbn());
    }
}