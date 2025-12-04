package com.maybank.book_management.service;

import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.dto.BookRequest;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.mapper.BookMapper;
import com.maybank.book_management.model.Book;
import com.maybank.book_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookPageResponse<BookResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        log.info("getting all books from repository");
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookResponse> bookList= books.stream().map(BookMapper::mapToResponse).toList();
        Page<BookResponse> BookResponsePage =
                new PageImpl<>(bookList, pageable, books.getTotalElements());
        return new BookPageResponse<>(BookResponsePage);
    }

    @Transactional
    public BookResponse create(BookRequest bookRequest){
        Book book= BookMapper.mapToModel(bookRequest);
        log.info("saving book:[{}]", bookRequest.toString());
        return BookMapper.mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse update(BookRequest bookRequest, String id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        log.info("update book:[{}]", bookRequest.toString());
        book = bookRepository.save(book);
        return BookMapper.mapToResponse(book);
    }

}
