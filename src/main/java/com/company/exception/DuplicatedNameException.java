package com.company.exception;

public class DuplicatedNameException extends ServiceException {
    public DuplicatedNameException(String message) {
        super(message);
    }
}
