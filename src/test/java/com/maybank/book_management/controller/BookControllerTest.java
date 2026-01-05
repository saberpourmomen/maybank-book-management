package com.maybank.book_management.controller;

import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.dto.BookRequest;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        int page = 0;
        int size = 10;

        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("Test Book");
        bookResponse.setAuthor("Author");
        bookResponse.setIsbn("ISBN123");

        BookPageResponse<BookResponse> mockPageResponse = new BookPageResponse<>();
        mockPageResponse.setContent(Collections.singletonList(bookResponse));
        mockPageResponse.setPage(page);
        mockPageResponse.setSize(size);
        mockPageResponse.setTotalElements(1);

        when(bookService.getAll(page, size)).thenReturn(mockPageResponse);

        ResponseEntity<BookPageResponse<BookResponse>> response = bookController.getAll(page, size);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Test Book", response.getBody().getContent().get(0).getTitle());

        verify(bookService, times(1)).getAll(page, size);
    }

    @Test
    void testSave() {
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("Author");
        request.setIsbn("ISBN123");

        BookResponse responseBody = new BookResponse();
        responseBody.setTitle("New Book");
        responseBody.setAuthor("Author");
        responseBody.setIsbn("ISBN123");

        when(bookService.create(request)).thenReturn(responseBody);

        ResponseEntity<BookResponse> response = bookController.save(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("New Book", response.getBody().getTitle());
        assertEquals("ISBN123", response.getBody().getIsbn());

        verify(bookService, times(1)).create(request);
    }

    @Test
    void testUpdate() {
        String id = "1";
        BookRequest request = new BookRequest();
        request.setTitle("Updated Book");
        request.setAuthor("Updated Author");
        request.setIsbn("UpdatedISBN");

        BookResponse responseBody = new BookResponse();
        responseBody.setTitle("Updated Book");
        responseBody.setAuthor("Updated Author");
        responseBody.setIsbn("UpdatedISBN");

        when(bookService.update(request, id)).thenReturn(responseBody);

        ResponseEntity<BookResponse> response = bookController.update(request, id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Book", response.getBody().getTitle());
        assertEquals("UpdatedISBN", response.getBody().getIsbn());

        verify(bookService, times(1)).update(request, id);
    }
}