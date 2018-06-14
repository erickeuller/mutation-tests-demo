package com.example.demo.exception;

public class FieldMaxSizeException extends BadRequestException {

    public FieldMaxSizeException(String message) {
        super(message);
    }
}
