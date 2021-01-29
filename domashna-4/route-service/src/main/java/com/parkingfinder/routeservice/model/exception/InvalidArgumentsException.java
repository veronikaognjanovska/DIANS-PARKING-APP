package com.parkingfinder.routeservice.model.exception;

/**
 * Exception class for invalid arguments
 * Extends RuntimeException
 * @author Veronika Ognjanovska and Veronika Stefanovska
 */
public class InvalidArgumentsException extends RuntimeException {

    /**
     * Method that represents the constructor of the InvalidArgumentsException
     */
    public InvalidArgumentsException() {
        super("Invalid arguments exception");
    }
}
