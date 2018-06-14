package com.example.demo.exception;

public class RequiredFieldException extends BadRequestException {

    public RequiredFieldException(String message) {
        super(message);
    }
}
