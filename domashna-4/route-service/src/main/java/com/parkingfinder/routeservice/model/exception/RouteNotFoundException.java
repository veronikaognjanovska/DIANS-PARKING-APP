package com.parkingfinder.routeservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class when route has not been found
 * Extends RuntimeException
 * @author Veronika Ognjanovska and Veronika Stefanovska
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RouteNotFoundException extends RuntimeException{

    /**
     * Constructor that represents the constructor of the RouteNotFoundException
     */
    public RouteNotFoundException() {
        super(String.format("Route was not found"));
    }

}
