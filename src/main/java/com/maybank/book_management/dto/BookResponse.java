package com.maybank.book_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookResponse {

    private String id;

    private String title;

    private String author;

    private String isbn;
}
