package com.maybank.book_management.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable=false)
    private String title;

    private String author;

    @Column(nullable=false,unique = true)
    private String isbn;
}
