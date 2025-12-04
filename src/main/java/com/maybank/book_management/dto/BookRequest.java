package com.maybank.book_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String author;

    @NotBlank(message = "ISBN cannot be empty")
    private String isbn;
}
