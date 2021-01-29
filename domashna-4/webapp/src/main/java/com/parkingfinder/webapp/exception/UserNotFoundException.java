package com.parkingfinder.webapp.exception;

import javax.naming.AuthenticationException;

/**
 * User not found exception
 * @extends RuntimeException
 * */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
