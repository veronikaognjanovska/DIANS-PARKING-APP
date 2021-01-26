package com.parkingfinder.userservice.model.exception;

public class InvalidArgumentsException extends RuntimeException {

    public InvalidArgumentsException() {
        super("Invalid arguments exception");
    }
}