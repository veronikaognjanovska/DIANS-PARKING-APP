package com.parkingfinder.webapp.rest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple rest controller for retrieving currently logged in user's email
 * */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    /**
     * Returns the currently authenticated user's email or null if no authentication is present
     * */
    @GetMapping
    public String authentication() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
