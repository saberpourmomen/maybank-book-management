package com.maybank.book_management.service;

import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.dto.BookRequest;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.mapper.BookMapper;
import com.maybank.book_management.model.Book;
import com.maybank.book_management.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setId("1");
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN1");

        Book book2 = new Book();
        book2.setId("2");
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN2");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Book> bookPage = new PageImpl<>(List.of(book1, book2), pageable, 2);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        // Act
        BookPageResponse<BookResponse> response = bookService.getAll(0, 2);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(0, response.getPage());
        assertEquals(2, response.getSize());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("New Author");
        request.setIsbn("NEWISBN");

        Book savedBook = BookMapper.mapToModel(request);
        savedBook.setId("123");

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        BookResponse response = bookService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals("New Book", response.getTitle());
        assertEquals("New Author", response.getAuthor());
        assertEquals("NEWISBN", response.getIsbn());

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertEquals("New Book", captor.getValue().getTitle());
    }

    @Test
    void testUpdateBook() {
        // Arrange
        String bookId = "123";
        BookRequest request = new BookRequest();
        request.setTitle("Updated Title");
        request.setAuthor("Updated Author");
        request.setIsbn("UpdatedISBN");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setIsbn("OldISBN");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BookResponse response = bookService.update(request, bookId);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Author", response.getAuthor());
        assertEquals("UpdatedISBN", response.getIsbn());

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void testUpdateBookNotFound() {
        // Arrange
        String bookId = "999";
        BookRequest request = new BookRequest();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.update(request, bookId));
        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }
}