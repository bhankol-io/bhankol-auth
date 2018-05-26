package com.bhankol.application.exceptions;


@SuppressWarnings("serial")
public class DuplicateResourceExistsException extends RuntimeException {

    public DuplicateResourceExistsException() {
        super();
    }

    public DuplicateResourceExistsException(String message) {
        super(message);
    }
}
