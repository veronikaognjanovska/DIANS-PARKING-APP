package com.parkingfinder.webapp.rest;

import com.parkingfinder.webapp.controller.UserController;
import com.parkingfinder.webapp.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Simple rest controller for retrieving currently logged in user's email
 * */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private UserDetails currentUser;

    /**
     * Method for setting current logged in user
     * @param user - current logged in user
     * */
    public void setCurrentUser(UserDetails user) {
        this.currentUser = user;
    }

    /**
     * Returns the currently authenticated user's email or null if no authentication is present
     * */
    @GetMapping
    @ResponseBody
    public String currentUserName(Principal principal) {
        return currentUser==null ? null : currentUser.getUsername();
    }
}
