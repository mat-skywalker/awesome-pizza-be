package org.example.awesomepizzabe.error.exceptions;

public class NoPendingOrdersException extends RuntimeException{
    public NoPendingOrdersException(String message) {
        super(message);
    }
}
