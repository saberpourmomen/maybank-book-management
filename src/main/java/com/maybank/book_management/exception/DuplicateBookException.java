package com.maybank.book_management.exception;

public class DuplicateBookException extends RuntimeException{
    public DuplicateBookException(String message){
        super(message);
    }
}
