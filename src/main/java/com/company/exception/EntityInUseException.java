package com.company.exception;

public class EntityInUseException  extends ServiceException {
    public EntityInUseException(String message) {
        super(message);
    }
}
