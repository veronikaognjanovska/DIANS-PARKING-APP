package com.parkingfinder.routeservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class RouteNotFoundException extends RuntimeException{

    public RouteNotFoundException() {
        super(String.format("Route was not found"));
    }

}