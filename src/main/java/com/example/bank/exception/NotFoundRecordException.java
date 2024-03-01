package com.example.bank.exception;

public class NotFoundRecordException extends Exception{
    public NotFoundRecordException(String message) {
        super(message);
    }
}
