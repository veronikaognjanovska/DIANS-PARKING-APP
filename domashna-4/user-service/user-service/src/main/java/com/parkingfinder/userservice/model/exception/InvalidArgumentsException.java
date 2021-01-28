package com.parkingfinder.userservice.model.exception;

/**
 * Exception class for invalid arguments
 * Extends RuntimeException
 * @author Anastasija Petrovska
 */
public class InvalidArgumentsException extends RuntimeException {

    /**
     * Method that represents the constructor of the InvalidArgumentsException
     */
    public InvalidArgumentsException() {
        super("Invalid arguments exception");
    }
}