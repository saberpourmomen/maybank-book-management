package com.maybank.book_management.controller;

import com.maybank.book_management.dto.BookResponse;
import com.maybank.book_management.dto.BookPageResponse;
import com.maybank.book_management.service.ExternalBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/external-book")
@RequiredArgsConstructor
public class ExternalBookController {

    private final ExternalBookService externalBookService;

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(externalBookService.getByIsbn(isbn));
    }

    @GetMapping("/search")
    public ResponseEntity<BookPageResponse<BookResponse>> searchByName(@RequestParam String title,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(externalBookService.getByTitle(title,page,size));
    }
}
