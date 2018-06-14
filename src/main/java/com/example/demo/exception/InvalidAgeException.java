package com.example.demo.exception;

public class InvalidAgeException extends BadRequestException{

    public InvalidAgeException(String message) {
        super(message);
    }
}
