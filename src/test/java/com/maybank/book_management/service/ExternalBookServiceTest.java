package com.maybank.book_management.service;

import com.maybank.book_management.config.ExternalApiProperties;
import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.dto.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ExternalBookServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalApiProperties externalApiProperties;

    @InjectMocks
    private ExternalBookService externalBookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByIsbn() {
        String isbn = "1234567890";
        String baseUrl = "http://external-api.com";
        BookResponse mockResponse = new BookResponse();
        mockResponse.setTitle("Test Book");
        mockResponse.setAuthor("Test Author");
        mockResponse.setIsbn(isbn);

        when(externalApiProperties.getBaseUrl()).thenReturn(baseUrl);
        when(restTemplate.getForObject(baseUrl + "/isbn/" + isbn, BookResponse.class))
                .thenReturn(mockResponse);

        BookResponse response = externalBookService.getByIsbn(isbn);

        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals("Test Author", response.getAuthor());
        assertEquals(isbn, response.getIsbn());

        verify(restTemplate).getForObject(baseUrl + "/isbn/" + isbn, BookResponse.class);
    }

    @Test
    void testGetByTitle() {
        String title = "Test Title";
        int page = 0;
        int size = 2;
        String baseUrl = "http://external-api.com";

        BookResponse book1 = new BookResponse();
        book1.setTitle("Book1");
        book1.setAuthor("Author1");

        BookResponse book2 = new BookResponse();
        book2.setTitle("Book2");
        book2.setAuthor("Author2");

        BookPageResponse<BookResponse> mockPageResponse = new BookPageResponse<>();
        mockPageResponse.setContent(List.of(book1, book2));
        mockPageResponse.setPage(page);
        mockPageResponse.setSize(size);
        mockPageResponse.setTotalElements(2);

        ResponseEntity<BookPageResponse<BookResponse>> responseEntity =
                new ResponseEntity<>(mockPageResponse, HttpStatus.OK);

        when(externalApiProperties.getBaseUrl()).thenReturn(baseUrl);

        when(restTemplate.exchange(
                eq(UriComponentsBuilder.fromUriString(baseUrl + "/search")
                        .queryParam("title", title)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .toUriString()),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<BookPageResponse<BookResponse>>>any()
        )).thenReturn(responseEntity);

        ResponseEntity<BookPageResponse<BookResponse>> response =
                externalBookService.getByTitle(title, page, size);

        assertNotNull(response);
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Book1", response.getBody().getContent().get(0).getTitle());

        verify(restTemplate).exchange(
                eq(UriComponentsBuilder.fromUriString(baseUrl + "/search")
                        .queryParam("title", title)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .toUriString()),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<BookPageResponse<BookResponse>>>any()
        );
    }
}