package com.example.demo.exception;

public class InvalidFieldFormatException extends BadRequestException{

    public InvalidFieldFormatException(String message) {
        super(message);
    }
}
