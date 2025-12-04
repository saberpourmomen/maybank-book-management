package com.maybank.book_management.controller;

import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.service.ExternalBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalBookControllerTest {

    @Mock
    private ExternalBookService externalBookService;

    @InjectMocks
    private ExternalBookController externalBookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByIsbn_Success() throws Exception {
        String isbn = "1234567890";
        BookResponse mockResponse = new BookResponse();
        mockResponse.setTitle("Test Book");
        mockResponse.setIsbn(isbn);

        when(externalBookService.getByIsbn(isbn)).thenReturn(mockResponse);

        ResponseEntity<BookResponse> response = externalBookController.getByIsbn(isbn);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Book", response.getBody().getTitle());
        assertEquals(isbn, response.getBody().getIsbn());
        verify(externalBookService, times(1)).getByIsbn(isbn);
    }

    @Test
    void testGetByIsbn_NotFound() throws Exception {
        String isbn = "notfound";
        when(externalBookService.getByIsbn(isbn)).thenThrow(new RuntimeException("Book not found"));

        ResponseEntity<BookResponse> response = externalBookController.getByIsbn(isbn);

        assertEquals(404, response.getStatusCodeValue());
        verify(externalBookService, times(1)).getByIsbn(isbn);
    }

    @Test
    void testSearchByName() {
        String title = "Spring Boot";
        int page = 0;
        int size = 10;

        BookPageResponse<BookResponse> mockPageResponse = new BookPageResponse<>();
        mockPageResponse.setContent(Collections.singletonList(new BookResponse()));
        mockPageResponse.setPage(page);
        mockPageResponse.setSize(size);
        mockPageResponse.setTotalElements(1);

        when(externalBookService.getByTitle(title, page, size)).thenReturn(ResponseEntity.ok(mockPageResponse));

        ResponseEntity<BookPageResponse<BookResponse>> response =
                externalBookController.searchByName(title, page, size);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        verify(externalBookService, times(1)).getByTitle(title, page, size);
    }
}