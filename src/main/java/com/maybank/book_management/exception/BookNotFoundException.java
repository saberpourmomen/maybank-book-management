package com.maybank.book_management.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String message){
        super(message);
    }
}
