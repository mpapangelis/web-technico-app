package com.example.webtechnico.exceptions;

public class MissingInputException extends RuntimeException {
    public MissingInputException(String message) {
        super(message);
    }
}
