package com.parkingfinder.routeservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class when user has not been found
 * Extends RuntimeException
 * @author Veronika Ognjanovska and Veronika Stefanovska
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    /**
     * Constructor that represents the constructor of the UserNotFoundException
     */
    public UserNotFoundException() {
        super(String.format("User was not found"));
    }

    /**
     * Parameterized constructor that represents the constructor of the UserNotFoundException
     * @param email - string representing user email
     */
    public UserNotFoundException(String email) {
        super(String.format("User with email: %s was not found", email));
    }

}

