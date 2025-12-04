package com.maybank.book_management.controller;

import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.dto.BookRequest;
import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/list")
    public ResponseEntity<BookPageResponse<BookResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
      return new ResponseEntity<>(bookService.getAll(page,size), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<BookResponse> save(@Valid @RequestBody BookRequest bookRequest){
        return new ResponseEntity<>(bookService.create(bookRequest), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BookResponse> update(@Valid @RequestBody BookRequest bookRequest,@PathVariable String id){
        return new ResponseEntity<>(bookService.update(bookRequest,id), HttpStatus.OK);
    }

}
